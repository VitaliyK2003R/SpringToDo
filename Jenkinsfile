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
                    args '-v $HOME/.m2:/var/maven/.m2'
                    reuseNode true
                }
            }
            steps {
                sh 'ls -la'
                sh 'chmod +x mvnw'
                sh './mvnw compile'
                sh './mvnw test'
            }
        }
    }
}