pipeline {
    agent { label 'master' }
    stages {
        stage('构建') {
            steps {
                sh '''
                cd codes
                mvn clean package -Dmaven.test.skip=true
                '''
            }
        }
    }
}
