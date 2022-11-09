//VARIABLES
def url = "http://ec2-52-200-245-199.compute-1.amazonaws.com:8080"
node{
    stage('checkout'){
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'git@bitbucket.org:mei-isep/odsoft-22-23-ncf-g202.git']]])
    }

    stage('build'){
        if (isUnix()){
            sh './gradlew clean build "-Pvaadin.productionMode" war'
        }else{
            bat './gradlew clean build "-Pvaadin.productionMode" war'
        }
    }

    stage('javadoc'){
        if (isUnix()){
            sh './gradlew javadoc'
        }else{
            bat './gradlew javadoc'
        }
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/javadoc/', reportFiles: 'index.html', reportName: 'Javadoc', reportTitles: '', useWrapperFileDirectly: true])
    }

    stage('integrationReport'){
        echo 'Running Integration Tests...'
        if (isUnix()){
            sh './gradlew integrationTest'
        }else{
            bat './gradlew integrationTest'
        }
        echo 'Generating Integration Test HTML Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/integration', reportFiles: 'index.html', reportName: 'IntegrationTests Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Integration Test HTML Report!'
    }

    stage('integrationReportCoverage'){
        echo 'Generating Integration Test Coverage Report...'
        if (isUnix()){
            sh './gradlew jacocoIntegrationReport'
        }else{
            bat './gradlew jacocoIntegrationReport'
        }
        echo 'Generated Integration Test Coverage Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/jacocoIntegrationReport/html', reportFiles: 'index.html', reportName: 'IntegrationTests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Integration Test Coverage Report!'    
    }

    stage('unitReport'){
        echo 'Running Unit Tests...'
        if (isUnix()){
            sh './gradlew unitTest'
        }else{
            bat './gradlew unitTest'
        }
        echo 'Generating Unit Test HTML Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/htmlReports/junitReports/unit', reportFiles: 'index.html', reportName: 'UnitTests Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Unit Test HTML Report!'
    }

    stage('unitReportCoverage'){
        echo 'Generating Unit Test Coverage Report...'
        if (isUnix()){
            sh './gradlew jacocoUnitReport'
        }else{
            bat './gradlew jacocoUnitReport'
        }
        echo 'Generated Unit Test Coverage Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/jacocoUnitReport/html', reportFiles: 'index.html', reportName: 'UnitTests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Unit Test Coverage Report!'    
    }

    stage('mutationReportCoverage'){
        echo 'Generating Mutation Test Coverage Report...'
        if (isUnix()){
            sh './gradlew pitest'
        }else{
            bat './gradlew pitest'
        }
        echo 'Generated Mutation Test Coverage Report and started Publishing...'
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/pitest', reportFiles: 'index.html', reportName: 'Mutation Tests Coverage Report', reportTitles: '', useWrapperFileDirectly: true])
        echo 'Published Mutation Test Coverage Report!'    
    }

    stage('staging'){
        echo "Starting Staging Fase..."
        if (isUnix()){
            sh "./gradlew copyArtifact"
        }else{
            bat "./gradlew copyArtifact"
        }
        echo "Deploying to environment..."
        deploy adapters: [tomcat9(credentialsId: "odsoft", path: "", url: "$url")], contextPath: "crm", war: "flowcrmtutorial-0.0.1-SNAPSHOT.war"
        echo "Stage deployed!"
    }
}