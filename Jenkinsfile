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
}