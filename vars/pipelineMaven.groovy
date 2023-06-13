def call (Map config = [:]) {
    defaultMap = [moduleName: 'works', enviroment: 'bar', repoName: 'jenkinsAG']
    config = defaultMap << config

    def skipTest = config.get('skipTest', false)
    def skipInstall = config.get('skipInstall', false)

    node{
        stage ('Download source code'){
            // checkout ([$class: 'GitSCM', branches: [[name: 'main']], userRemoteConfigs:[[url: "https://github.com/apudzianowski/spring-petclinic"]]])
            checkout scm
        }
        stage ('Building source code'){
                sh 'mvn -B -DskipTests clean package'
            }
        stage ('Test app') {
            if (!skipTest){
                sh 'mvn verify'
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage ('Installing Artifacts') {
            if (!skipInstall){
            sh 'mvn install -DskipTests'
            }
        }
    }
} 