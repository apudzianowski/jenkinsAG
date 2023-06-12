def call (Map config = [:]) {
    defaultMap = [moduleName: 'works', enviroment: 'bar', repoName: 'jenkinsAG']
    config = defaultMap << config

    node{
        stage ('Download source code'){
            // checkout ([$class: 'GitSCM', branches: [[name: 'main']], userRemoteConfigs:[[url: "https://github.com/apudzianowski/spring-petclinic"]]])
            checkout scm
        }
        stage ('Building source code'){
                sh 'mvn -B -DskipTests '
            }
        stage ('Test app') {
                sh 'mvn verify'
                junit 'target/surefire-reports/*.xml'
            // post {
            //     always{
            //         junit 'target/surefire-reports/*.xml'
            //     }
            // }
        }
        stage ('Installing Artifacts') {
            sh 'mvn install -DskipTests'
        }
    }
}