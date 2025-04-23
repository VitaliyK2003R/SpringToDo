pipeline {
    agent any
    tools {
        jdk 'Java21'
        maven 'Maven3'
    }
    stages {

        stage("Cleanup") {
            steps {
                cleanWs()
            }
        }
        stage("Checkout") {
            steps {
                git branch: 'develop', credentialsId: 'github', url: 'https://github.com/VitaliyK2003R/SpringToDo.git'
            }
        }
        stage("Build") {
            steps {
                sh 'mvn clean package -u root'
            }
        }
        stage("Test") {
            steps {
                sh 'mvn test -u root'
            }
        }
    }
}