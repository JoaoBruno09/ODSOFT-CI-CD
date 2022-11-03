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
}