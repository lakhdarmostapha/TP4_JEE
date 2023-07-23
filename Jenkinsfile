pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                git 'https://github.com/lakhdarmostapha/TP4_JEE.git'
                sh './mvnw clean compile'
                // bat '.\\mvnw clean compile'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
                // bat '.\\mvnw test'
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
    }
}
@lakhdarmostapha
 
