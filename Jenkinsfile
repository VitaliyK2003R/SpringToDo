pipeline {
    agent none
    stages {
        stage("Checkout") {
            agent any
            steps {
                checkout scm
            }
        }
        stage("Prepare container") {
            agent {
                docker {
                    image 'openjdk:21-jdk-oracle'
                    args '-v $HOME/.m2:/root/.m2'
                    reuseNode true
                }
            }
            stages {
                stage('Build') {
                    steps {
                        sh 'ls -la'
                        sh 'chmod +x mvnw'
                        sh './mvnw compile'
                    }
                }
                stage('Test') {
                    steps {
                        sh './mvnw test'
                    }
                }
            }
        }
    }
}