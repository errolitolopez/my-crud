pipeline {
	agent any

	environment {
		APP_NAME = 'my-crud'
		IMAGE_NAME = 'my-crud'
		NETWORK = 'my-network'
		DB_URL = 'jdbc:postgresql://postgres:5432/my_db'
		DB_USER = 'root'
		DB_PASS = '123qwe'
	}

	stages {
		stage('1. Build Image') {
			steps {
				git branch: 'master',
				url: 'https://github.com/errolitolopez/my-crud.git',
				credentialsId: 'github-creds'

				sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} ."
				echo "Image built: ${IMAGE_NAME}:${BUILD_NUMBER}"
			}
		}

		stage('2. Rolling Update') {
			steps {
				script {
					def ports = [8091, 8092]

					for (p in ports) {
						def containerName = "${APP_NAME}-${p}"
						def previousImage = sh(
							script: "docker inspect --format='{{.Config.Image}}' ${containerName} 2>/dev/null || echo ''",
							returnStdout: true
						).trim()

						echo ">>> Rolling update: ${containerName} on port ${p}"
						echo ">>> Previous image: ${previousImage ?: 'none'}"

						sh "docker stop ${containerName} || true"
						sh "docker rm ${containerName} || true"

						sh """
                     docker run -d \\
                         --name ${containerName} \\
                         --network ${NETWORK} \\
                         --restart unless-stopped \\
                         -p ${p}:8080 \\
                         -e INSTANCE_PORT=${p} \\
                         -e SPRING_DATASOURCE_URL=${DB_URL} \\
                         -e SPRING_DATASOURCE_USERNAME=${DB_USER} \\
                         -e SPRING_DATASOURCE_PASSWORD=${DB_PASS} \\
                         ${IMAGE_NAME}:${BUILD_NUMBER}
                 """

						echo "Started ${containerName}, waiting for health check..."

						def healthy = false
						for (int i = 1; i <= 15; i++) {
							sleep(time: 5, unit: 'SECONDS')
							def status = sh(
								script: "docker exec ${containerName} wget -qO- http://localhost:8080/actuator/health || echo 'down'",
								returnStdout: true
							).trim()

							if (status.contains('UP')) {
								echo "${containerName} is healthy after ${i * 5}s"
								healthy = true
								break
							}

							echo "Waiting... attempt ${i}/15 (${i * 5}s elapsed)"
						}

						if (!healthy) {
							echo "!!! Health check failed — rolling back ${containerName} to ${previousImage ?: 'nothing'}"
							sh "docker stop ${containerName} || true"
							sh "docker rm ${containerName} || true"

							if (previousImage) {
								sh """
                           docker run -d \\
                               --name ${containerName} \\
                               --network ${NETWORK} \\
                               --restart unless-stopped \\
                               -p ${p}:8080 \\
                               -e INSTANCE_PORT=${p} \\
                               -e SPRING_DATASOURCE_URL=${DB_URL} \\
                               -e SPRING_DATASOURCE_USERNAME=${DB_USER} \\
                               -e SPRING_DATASOURCE_PASSWORD=${DB_PASS} \\
                               ${previousImage}
                       """
								echo "Rolled back ${containerName} to ${previousImage}"
							}

							error("${containerName} failed health check — rolled back. Aborting pipeline.")
						}

						echo "<<< ${containerName} updated successfully. Moving to next instance...\n"
					}
				}
			}
		}

		stage('3. Reload Nginx') {
			steps {
				sh "docker exec nginx nginx -s reload"
				echo "Nginx reloaded — traffic flowing to new containers"
			}
		}
	}

	post {
		success {
			echo "Rolling deployment #${BUILD_NUMBER} succeeded!"
			sh "docker ps --format 'table {{.Names}}\\t{{.Ports}}\\t{{.Status}}'"
		}
		failure {
			echo "Rolling deployment failed — check logs above"
			sh "docker ps -a --format 'table {{.Names}}\\t{{.Ports}}\\t{{.Status}}'"
		}
		always {
			sh "docker image prune -f || true"
		}
	}
}