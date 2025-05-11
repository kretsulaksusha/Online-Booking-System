pipeline {
  agent any
  environment {
    REGISTRY = 'your.docker.registry'
  }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build') {
      steps { sh './gradlew clean build -x test' }
    }
    stage('Test') {
      steps { sh './gradlew test' }
    }
    stage('Docker Build & Push') {
      steps {
        script {
          ['api-gateway','inventory-service','booking-service'].each { svc ->
            sh "docker build -t \$REGISTRY/\$svc:latest ./${svc}"
            sh "docker push \$REGISTRY/\$svc:latest"
          }
        }
      }
    }
    stage('Deploy') {
      steps { sh 'docker-compose up -d --remove-orphans' }
    }
  }
}
