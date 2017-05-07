pipeline {
  agent any
  stages {
    stage('Build & Package') {
      steps {
        git(url: 'git@github.com:donaldsiziba/train-timetables.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
      }
    }
    stage('Deploy 2 Dev') {
      steps {
        parallel(
          "Deploy 2 Dev": {
            git(url: 'git@github.com:donaldsiziba/train-timetables-deploy.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
            
          },
          "OWASP Dependency Check": {
            sh 'mvn org.owasp:dependency-check-maven:1.4.5:check'
            
          },
          "Sonarqube Analysis": {
            sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'
            
          }
        )
      }
    }
    stage('Acceptance Tests') {
      steps {
        git(url: 'git@github.com:donaldsiziba/train-timetables.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
      }
    }
    stage('Deploy 2 Test') {
      steps {
        parallel(
          "Deploy 2 Test": {
            git(url: 'git@github.com:donaldsiziba/train-timetables-deploy.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
            
          },
          "Deploy 2 QoS": {
            git(url: 'git@github.com:donaldsiziba/train-timetables-deploy.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
            
          }
        )
      }
    }
    stage('Manual Tests') {
      steps {
        parallel(
          "Manual Tests": {
            sh 'echo Manual Usability and Exploratory Tests'
            
          },
          "Qos Tests": {
            git(url: 'git@github.com:donaldsiziba/train-timetables-qos.git', branch: 'master', credentialsId: '\'bec1ce9f-5db8-44df-8f83-a3d59daba71e\'')
            
          }
        )
      }
    }
    stage('Deploy 2 Prod') {
      steps {
        sh 'echo Deploy 2 Prod'
      }
    }
  }
}