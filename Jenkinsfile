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
                    args '-v $HOME/.m2:/var/maven/.m2 -u 1000:1000'
                    reuseNode true
                }
            }
            environment {
                MAVEN_OPTS = "-Duser.home=/var/maven"
            }
            steps {
                sh 'mvn compile'
                sh 'mvn test'
            }
        }
    }
}