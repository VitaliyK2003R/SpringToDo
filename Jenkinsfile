pipeline {
    agent any
    tools {
        jdk "Java21"
        maven "Maven3"
    }
    environment {
        APP_NAME = "SpringToDo"
        RELEASE = "1.0.0"
        DOCKER_USER = "vkontakte001"
        DOCKER_PASS = credentials("dockerhub")
        IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
    }
    stages {

        stage("Cleanup") {
            steps {
                cleanWs()
            }
        }

        stage("Checkout") {
            steps {
                git branch: "develop", credentialsId: "github", url: "https://github.com/VitaliyK2003R/SpringToDo.git"
            }
        }

        stage("Prepare Maven") {
            steps {
                sh """
                    mkdir -p ${WORKSPACE}/.m2
                    cp -n /var/jenkins_home/.m2/settings.xml ${WORKSPACE}/.m2/ || true
                """
            }
        }

        stage("Build") {
            steps {
                sh "mvn clean package -Dmaven.repo.local=${WORKSPACE}/.m2/repository"
            }
        }

        stage("Test") {
            steps {
                sh "mvn test -Dmaven.repo.local=${WORKSPACE}/.m2/repository"
            }
        }

        stage("Build and push Docker image") {
            steps {
                script {
                    sh """
                        docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                    """
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}").push()
                    docker.build("${IMAGE_NAME}:latest").push()
                }
            }
        }
    }
}
