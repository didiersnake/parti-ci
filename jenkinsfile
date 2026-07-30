pipeline {

    agent any

    stages {
        stage('Cleanup') {
            steps {
                echo 'Cleanup...'
                sh 'docker compose down'
            }
        }
        stage('Build & Deploy') {
            steps {
                echo 'Deploying....'
                sh 'docker compose up -d --build'
            }
        }
    }

    post {
        always {
            echo 'pipeline finished'
        }

    }

}