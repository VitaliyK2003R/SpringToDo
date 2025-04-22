pipeline {
    agent none
    stages {
        stage("Checkout") {
            agent any
            steps {
                checkout scm
            }
        }
        stage("Build & Test") {
            agent {
                docker {
                    image 'maven:3.9.9-amazoncorretto-21-alpine'
                    args '-v $HOME/.m2:/var/maven/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
                                        reuseNode true
                }
            }
            environment {
                MAVEN_OPTS = "-Duser.home=/var/maven"
                TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE = "/var/run/docker.sock"  // Для Testcontainers
            }
            steps {
                sh 'mvn compile'
                sh 'mvn test'
            }
        }
    }
}