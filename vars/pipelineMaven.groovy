def call (Map config = [:]) {
    defaultMap = [moduleName: 'works', enviroment: 'bar', repoName: 'jenkinsAG']
    config = defaultMap << config

    def skipTest = config.get('skipTest', false)
    def skipInstall = config.get('skipInstall', false)

    node{
        wrap([$class: 'TimestamperBuildWrapper']) {
        stage ('Download source code'){
            // checkout ([$class: 'GitSCM', branches: [[name: 'main']], userRemoteConfigs:[[url: "https://github.com/apudzianowski/spring-petclinic"]]])
            echo '\033[34mDownloading\033[0m \033[34msource\033[0m \033[34mcode!\033[0m'
            checkout scm
        }
        stage ('Building source code'){
            echo '\033[33mBuilding\033[0m \033[33msource\033[0m \033[33mcode!\033[0m'
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
        
        stage ('Clean The Mess') {
        // Clean after build
            always {
                cleanWs()
        }
    }
        }
    }
    }
} 