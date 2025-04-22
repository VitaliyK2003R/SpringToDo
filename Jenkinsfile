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
                    image 'maven:3.9.6-openjdk-21'
                    args '-v $HOME/.m2:/var/maven/.m2 -e MAVEN_OPTS="-Duser.home=/var/maven"'
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