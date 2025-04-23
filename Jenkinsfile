pipeline {
    agent none
    environment {
        DOCKER_HOST = "tcp://docker:2376"
        DOCKER_TLS_VERIFY = "1"
        DOCKER_CERT_PATH = "/certs/client"
    }
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
                    args '-v $HOME/.m2:/var/maven/.m2'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn compile'
                sh 'mvn test'
            }
        }
    }
}