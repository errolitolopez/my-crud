pipeline {
	agent any

	environment {
		IMAGE_NAME = 'my-crud'
	}

	stages {
		stage('1. Build Image') {
			steps {
				sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                """
			}
		}

		stage('2. Deploy via Compose') {
			steps {
				sh """
            		BUILD_NUMBER=${BUILD_NUMBER} docker compose up -d --build
                """
			}
		}

		stage('3. Reload Nginx') {
			steps {
				sh "docker exec nginx nginx -s reload"
			}
		}
	}

	post {
		success {
			echo "Deployment #${BUILD_NUMBER} success"
			sh "docker compose ps"
		}
		failure {
			echo "Deployment failed"
			sh "docker compose logs --tail=50"
		}
	}
}