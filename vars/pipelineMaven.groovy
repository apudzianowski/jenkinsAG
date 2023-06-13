def call (Map config = [testInstall : 1, testApp : 1]) {
    defaultMap = [moduleName: 'works', enviroment: 'bar', repoName: 'jenkinsAG']
    config = defaultMap << config

    node{
        stage ('Download source code'){
            // checkout ([$class: 'GitSCM', branches: [[name: 'main']], userRemoteConfigs:[[url: "https://github.com/apudzianowski/spring-petclinic"]]])
            checkout scm
        }
        stage ('Building source code'){
                sh 'mvn -B -DskipTests clean package'
            }
        stage ('Test app') {
            if ($defaultMap.testApp==0){
                sh 'mvn verify'
                junit 'target/surefire-reports/*.xml'
            }
        }

        //     post {
        //         always{
        //             junit 'target/surefire-reports/*.xml'
        //         }
        //     }
        // }
        stage ('Installing Artifacts') {
            if ($defaultMap.testInstall==0){
            sh 'mvn install -DskipTests'
            }
        }
    }
} 