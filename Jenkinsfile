  stages {
    stage('Checkout') { ... }

    stage('Build product-service') { ... }

    stage('Build ui-service') { ... }

    stage('SonarQube analysis') { ... }

    stage('Quality Gate') { ... }

    stage('Archive artifacts') {
      steps {
        archiveArtifacts artifacts: 'product-service/target/*.jar, ui-service/target/*.jar', fingerprint: true
      }
    }

    stage('Deploy (simple)') {
      steps {
        sh '''
          mkdir -p /var/jenkins_home/deploy
          cp product-service/target/*.jar /var/jenkins_home/deploy/product-service.jar
          cp ui-service/target/*.jar /var/jenkins_home/deploy/ui-service.jar
        '''
      }
    }
  }
