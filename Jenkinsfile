pipeline {
  agent none
  stages {
    stage("Prepare container") {
      agent {
        docker {
          image 'openjdk:21-jdk-oracle'
          args '-v $HOME/.m2:/root/.m2'
        }
      }
      stages {
        stage('Build') {
            steps {
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
