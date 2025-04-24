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

        stage("Prepare Maven") {
            steps {
                sh '''
                    mkdir -p ${WORKSPACE}/.m2
                    cp -n /var/jenkins_home/.m2/settings.xml ${WORKSPACE}/.m2/ || true
                '''
            }
        }

        stage("Build") {
            steps {
                sh 'mvn clean package -Dmaven.repo.local=${WORKSPACE}/.m2/repository'
            }
        }

        stage("Test") {
            steps {
                sh 'mvn test -Dmaven.repo.local=${WORKSPACE}/.m2/repository'
            }
        }

    }
}