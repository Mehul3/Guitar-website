pipeline {
  agent any

  environment {
    SONAR_TOKEN = credentials('SONAR_TOKEN')
    IMAGE_PREFIX = "guitarshop"   // change if you want your own registry/user
    PRODUCT_IMAGE = "${IMAGE_PREFIX}/product-service:latest"
    UI_IMAGE = "${IMAGE_PREFIX}/ui-service:latest"
  }

  tools {
    maven 'MAVEN_HOME'
    jdk 'JDK17'
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

    stage('Build Docker images') {
      steps {
        script {
          // product image
          dir('product-service') {
            sh "docker build -t ${PRODUCT_IMAGE} ."
          }
          // ui image
          dir('ui-service') {
            sh "docker build -t ${UI_IMAGE} ."
          }
        }
      }
    }

    stage('Deploy (docker)') {
      steps {
        script {
          // stop & remove previous containers if running (ignore errors)
          sh "docker rm -f guitar-product || true"
          sh "docker rm -f guitar-ui || true"

          // run product-service on host port 8081
          sh "docker run -d --name guitar-product -p 8081:8081 --restart unless-stopped ${PRODUCT_IMAGE}"

          // run ui-service on host port 8080
          sh "docker run -d --name guitar-ui -p 8080:8080 --restart unless-stopped ${UI_IMAGE}"
        }
      }
    }

    stage('Post-deploy check') {
      steps {
        // quick healthchecks (optional)
        sh '''
          echo "Product service endpoint:"
          docker logs guitar-product --tail 20 || true
          echo "UI service endpoint:"
          docker logs guitar-ui --tail 20 || true
        '''
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
