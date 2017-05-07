pipeline {
    tools {
        maven 'Maven'
        jdk 'Oracle JDK'
    }
    agent any
    stages {
        stage('Build & Package') {
            steps {
                git(url: 'git@github.com:donaldsiziba/train-timetables.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
                withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                    sh 'mvn -U -B -Dversion.number=1.0.0.${BUILD_NUMBER} clean verify'
                }
                junit '**/target/surefire-reports/*.xml'
                archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
            }
        }
        stage('Deploy 2 Dev') {
            steps {
                parallel(
                        "Deploy 2 Dev": {
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn -f deploy.xml -Dglassfish.domain=domain1 -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=15048 -Dglassfish.http.port=15080 -Dversion.number=1.0.0.${BUILD_NUMBER} verify'
                            }
                        },
                        "OWASP Dependency Check": {
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn org.owasp:dependency-check-maven:1.4.5:check'
                            }
                            publishHTML target: [
                                    reportDir  : 'target',
                                    reportFiles: 'dependency-check-report.html',
                                    reportName : 'OWASP Dependency Check'
                            ]
                        },
                        "Sonarqube Analysis": {
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'
                            }

                        }
                )
            }
        }
        stage('Acceptance Tests') {
            steps {
                withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                    sh "mvn -P acceptance-tests -Dport=15080 verify"
                }

            }
        }
        stage('Deploy 2 Test') {
            steps {
                parallel(
                        "Deploy 2 Test": {
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn -f deploy.xml -Dglassfish.domain=test -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=20048 -Dglassfish.http.port=20080 -Dversion.number=1.0.0.${BUILD_NUMBER} verify'
                            }

                        },
                        "Deploy 2 QoS": {
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn -f deploy.xml -Dglassfish.domain=qos -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=21048 -Dglassfish.http.port=21080 -Dversion.number=1.0.0.${BUILD_NUMBER} verify'
                            }
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
                            git(url: 'git@github.com:donaldsiziba/train-timetables-qos.git', branch: 'master', credentialsId: 'bec1ce9f-5db8-44df-8f83-a3d59daba71e')
                            withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                                sh 'mvn verify'
                            }
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