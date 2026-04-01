pipeline {
	agent any

	environment {
		IMAGE_NAME = 'my-crud-app'
	}

	stages {
		stage('1. Build Image') {
			steps {
				git branch: 'master',
				url: 'https://github.com/errolitolopez/my-crud.git',
				credentialsId: 'github-creds'

				sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
				sh "docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest"
			}
		}

		stage('2. Deploy via Compose') {
			steps {
				sh """
					docker compose -f app.yml down
					docker compose -f app.yml up -d --build
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