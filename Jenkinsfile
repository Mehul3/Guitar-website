pipeline {
  agent any

  environment {
    SONAR_TOKEN = credentials('SONAR_TOKEN')   // from Jenkins credentials
  }

  tools {
    maven 'MAVEN_HOME'
    jdk 'JDK21'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build product-service') {
      steps {
        dir('product-service') {
          sh 'mvn -B clean package -DskipTests'
        }
      }
    }

    stage('Build ui-service') {
      steps {
        dir('ui-service') {
          sh 'mvn -B clean package -DskipTests'
        }
      }
    }

    stage('SonarQube analysis') {
      steps {
        dir('product-service') {
          withSonarQubeEnv('MySonarServer') {
            sh "mvn -B sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
          }
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 2, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('Archive artifacts') {
      steps {
        archiveArtifacts artifacts: 'product-service/target/*.jar, ui-service/target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      echo 'Pipeline finished'
    }
  }
}
