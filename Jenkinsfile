pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar'
                }
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
