def version = createVersion()
def url = "http://localhost:8082"
def job_console = "http://localhost:8085/job/${env.JOB_NAME}/${env.BUILD_NUMBER}/console"
def user_input
pipeline{
    agent any;
    options{
        timestamps()
        parallelsAlwaysFailFast() //When one of the stages of the parallel fails, everything fails.
    }
    environment {
        DB_HOST='localhost'
        DB_PORT='5433'
    }
    stages{
        stage("Checkout"){
            steps{
                script{
                    try{
                        checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git']]])
                    }catch (error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }

        stage("Build"){
            steps{
                script{
                    try{
                        if (isUnix()){
                            //building and generating artifacts
                            sh "./gradlew clean build '-Pvaadin.productionMode' -x test -x check war"
                            sh './gradlew renameDeployFile'
                            sh 'docker build -t odsoft-image .'

                        }else{
                            //building and generating artifacts
                            bat "./gradlew clean build -Pvaadin.productionMode -x test -x check war"
                            bat './gradlew renameDeployFile'
                            bat "docker build -t odsoft-image ."

                        }
                        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhubcredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        // Access the values of the credential within the block
                            if (isUnix()){
                                sh "docker login -u ${env.USERNAME} -p ${env.PASSWORD}"
                                sh 'docker tag odsoft-image 1220257/odsoft-image'
                                sh 'docker push 1220257/odsoft-image'
                            }else{
                                bat "docker login -u ${env.USERNAME} -p ${env.PASSWORD}"
                                bat 'docker tag odsoft-image 1220257/odsoft-image'
                                bat 'docker push 1220257/odsoft-image'                                
                            }
                        }
                        archiveArtifacts artifacts: 'build/libs/flowcrmtutorial-0.0.1-SNAPSHOT.war', followSymlinks: false
                    }catch (error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }
        stage("Parallel 1"){
            parallel{
            //START OF PARALLEL 1
                stage("Check"){
                    steps {
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew check'
                                }else{
                                    bat './gradlew check'
                                }
                            }catch (error){
                                currentBuild.result =  'FAILURE'
                                throw error
                            }
                            recordIssues(enabledForFailure:  true,  aggregatingResults:  false,
                            tools:  [
                                java (reportEncoding:  'UTF-8'),
                                checkStyle(pattern:  '**/checkstyle/main.xml',  reportEncoding:  'UTF-8'),
                                spotBugs(pattern:  '**/spotbugs/main.xml',  reportEncoding:  'UTF-8')],
                            healthy:  10,  unhealthy:  400,
				            qualityGates:  [[threshold:  20,  type:  'TOTAL',  unstable:  true],  [threshold:  400,  type:  'TOTAL',  unstable:  false]]    
                            )
                        }
                    }
                }
                stage("InitializingStagingEnv"){ //put in parallel with mutation and unit tests
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh 'docker-compose -f docker-compose-staging.yml up -d'                         
                                }else{
                                    bat 'docker-compose -f docker-compose-staging.yml up -d'
                                }
                            }catch(error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage("unitTest"){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew unitTest'
                                    sh './gradlew jacocoUnitReport'
                                }else{
                                    bat './gradlew unitTest'
                                    bat './gradlew jacocoUnitReport'
                                }
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/unit', reportFiles: 'index.html', reportName: 'UnitTests Report', reportTitles: '', useWrapperFileDirectly: true])
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/jacocoUnitReport/html', reportFiles: 'index.html', reportName: 'UnitTests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
                            }catch(error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage('mutationTest'){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew pitest'
                                }else{
                                    bat './gradlew pitest'
                                }
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/pitest', reportFiles: 'index.html', reportName: 'Mutation Tests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
                            }catch(error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
            //END OF PARRALEL 1
            }
        }
        stage('SmoketestStaging'){
            steps{
                script{
                    try{
                        if (isUnix()){
                            httpCode = sh( script: "curl -s -o /dev/null -w '%{http_code}' $url/login", returnStdout: true ).trim()
                            echo httpCode
                        }else{
                            httpCode = bat( script: "curl -s -o ./response -w %%{http_code} $url/login", returnStdout: true).trim()
                            httpCode = httpCode.readLines().drop(1).join(" ")//windows returns full command plus the response, but the response is at a new line so we can drop the first line and remove spaces and we get only the http code
                        }
                                //checking if the http code was ok(200) or found(302)
                        if (httpCode == "200" || httpCode == "302"){
                            echo 'The application is responding!'
                        }else{
                            currentBuild.result = 'FAILURE'
                            error('The application is not responding...')
                        }
                    }catch(error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }

        stage('updateStagingDatabase'){
            steps{
                script{
                    try{
                        if (isUnix()){
                            sh './gradlew tag -PliquibaseCommandValue="1"'
                            sh './gradlew update'
                        }else{
                            bat './gradlew tag -PliquibaseCommandValue="1"'
                            bat './gradlew update'
                        }
                    }catch(error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }
        stage('ManualAcceptanceTest'){
            steps{
                script{
                    try{
                        emailext body: "Greetings developer,\n I'm here to tell you that the application is up and running! Now you should manually test it to confirm if it meets your standarts\n The link is: $url/login\n After that please proceed to manually confirm that you want to proceed or abort with the following link: $job_console \n This is an automated message from your Jenkins job.", subject: "Job Manual Test of Build#${env.BUILD_NUMBER}", to: "1220257@isep.ipp.pt"
                        user_input = input(id: 'userInput',
                                message: 'Please Manually Test the application. Did all went ok?',
                                parameters: [
                                    [$class:'ChoiceParameterDefinition', choices: "Yes\nNo", name: 'Answer']
                                        ]
                        )
                        if (user_input == "No"){
                            if (isUnix()){
                                sh "./gradlew rollback -PliquibaseCommandValue='1'"
                            }else{
                                bat "./gradlew rollback -PliquibaseCommandValue='1'"
                            }
                        }
                    }catch(error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }
        stage("Parallel 2"){
            parallel{
                stage("integrationTest"){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew integrationTest'
                                    sh './gradlew jacocoIntegrationReport'
                                    sh './gradlew jacocoTestCoverageVerification'
                                }else{
                                    bat './gradlew integrationTest'
                                    bat './gradlew jacocoIntegrationReport'
                                    bat './gradlew jacocoTestCoverageVerification'
                                }
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/integration', reportFiles: 'index.html', reportName: 'IntegrationTests Report', reportTitles: '', useWrapperFileDirectly: true])
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/jacocoIntegrationReport/html', reportFiles: 'index.html', reportName: 'IntegrationTests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage('End2EndTests'){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew endToEnd'
                                }else{
                                    bat './gradlew endToEnd'
                                }
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/selenium/end2end/', reportFiles: 'index.html', reportName: 'End2End Report', reportTitles: '', useWrapperFileDirectly: true])
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }        
            }
        }
        stage("Parallel 3"){
            parallel{
                stage('jmeter'){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew jmRun'
                                }else{
                                    bat './gradlew jmRun'
                                }
                                perfReport errorFailedThreshold: 50, errorUnstableThreshold: 10, filterRegex: '', showTrendGraphs: true, sourceDataFiles: 'build/test-results/jmeter/test.jtl'
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage('Cucumber'){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh './gradlew cucumberTest'
                                }else{
                                    bat './gradlew cucumberTest'
                                }
                                cucumber buildStatus: 'null', customCssFiles: '', customJsFiles: '', failedFeaturesNumber: -1, failedScenariosNumber: -1, failedStepsNumber: -1, fileIncludePattern: '*.json', jsonReportDirectory: 'build/reports/tests/cucumber/', pendingStepsNumber: -1, reportTitle: 'Cucumber Report', skippedStepsNumber: -1, sortingMethod: 'ALPHABETICAL', undefinedStepsNumber: -1
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
            }
        }
        stage('Parallel 4'){
            parallel{
            //START OF PARALLEL
                stage("DeployProd"){
                    steps {
                        script{
                            try{
                                if (isUnix()){
                                    sh 'docker-compose -f docker-compose-staging.yml down'
                                    sh 'docker-compose up -d'
                                }else{
                                    bat 'docker-compose -f docker-compose-staging.yml down'
                                    bat 'docker-compose up -d'
                                }
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage("Javadoc"){
                    steps{
                        script{
                            try{
                                if (isUnix()){
                                    sh "./gradlew javadoc"
                                }else{
                                    bat "./gradlew javadoc"
                                }
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/javadoc/', reportFiles: 'index.html', reportName: 'Javadoc', reportTitles: '', useWrapperFileDirectly: true])
                            }catch (error){
                                currentBuild.result = 'FAILURE'
                                throw error
                            }
                        }
                    }
                }
                stage('ConvertMDtoPDF'){
                    steps{
                        script{
                            if (isUnix()){
                                sh './gradlew pdfConverter'
                            }else{
                                bat './gradlew pdfConverter'
                            }
                        }
                    }
                }
            }
            //END OF PARALLEL
        }
        stage('GenerateZipFile'){
            steps{
                script{
                    if (isUnix()){
                        sh './gradlew genZip'
                    }else{
                        bat './gradlew genZip'
                    }
                }
            }
        }
        stage('SmoketestProd'){
            steps{
                script{
                    try{
                        if (isUnix()){
                            httpCode = sh( script: "curl -s -o /dev/null -w '%{http_code}' $url/login", returnStdout: true ).trim()
                            echo httpCode
                        }else{
                            httpCode = bat( script: "curl -s -o ./response -w %%{http_code} $url/login", returnStdout: true).trim()
                            httpCode = httpCode.readLines().drop(1).join(" ")//windows returns full command plus the response, but the response is at a new line so we can drop the first line and remove spaces and we get only the http code
                        }
                        //checking if the http code was ok(200) or found(302)
                        if (httpCode == "200" || httpCode == "302"){
                            echo 'The application is responding!'
                        }else{
                            currentBuild.result = 'FAILURE'
                            error('The application is not responding...')
                        }
                    }catch(error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }
        stage('updateProdDatabase'){
            steps{
                script{
                    try{
                        if (user_input == "Yes"){
                            if (isUnix()){
                                sh './gradlew update'
                            }else{
                                bat './gradlew update'
                            }
                        }
                    }catch(error){
                        currentBuild.result = 'FAILURE'
                        throw error
                    }
                }
            }
        }
    }
    post {
        always {
            script{
            if (isUnix()) {
                sh "git tag -a Version#$version-Build#${env.BUILD_NUMBER}-${currentBuild.currentResult} -m \"Tag generated in jenkins job\""
                sh "git push git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git --tags"
            }else{
                bat "git tag -a Version#$version-Build#${env.BUILD_NUMBER}-${currentBuild.currentResult} -m \"Tag generated in jenkins job\""
                bat "git push git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git --tags"
            }
            }
        }
    }
}

String createVersion() {
    String versionName = new Date().format('yyyyMMddHHmm') + '-BUILD'
    currentBuild.description = versionName
    return versionName
}