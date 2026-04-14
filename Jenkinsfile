pipeline {
	agent any

	environment {
		IMAGE_NAME = 'my-crud'
		SONARQUBE = 'SonarQube'
	}

	stages {

        stage('1. Build & Test') {
            steps {
                sh "mvn clean verify"
            }
        }

        stage('2. Sonar Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn sonar:sonar"
                }
            }
        }

        stage('3. Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

		stage('4. Build Image') {
			steps {
				sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                """
			}
		}

		stage('5. Deploy via Compose') {
			steps {
				sh """
                    export BUILD_NUMBER=${BUILD_NUMBER}
            		docker compose up -d --build
                """
			}
		}

		stage('6. Reload Nginx') {
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

            sh """
                docker compose logs --tail=50 || true
            """
		}
	}
}