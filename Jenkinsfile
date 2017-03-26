stage "Build"

node {
    git url: 'git@awesomatic.local:train-timetables.git', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5', branch: 'develop'
    withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
        sh 'mvn -U -B -Dversion.number=1.0.0.${BUILD_NUMBER} clean install'
    }
    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.war', fingerprint:true])
    step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}

stage "Sonar Analysis"

node {
    git url: 'git@awesomatic.local:train-timetables.git', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5', branch: 'develop'
    withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
        sh '''
            ls -lrta
            mvn sonar:sonar
        '''
    }
}

stage "Deploy 2 Dev"

node {
    git url: 'git@awesomatic.local:train-timetables-deploy', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5'

    withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
        sh 'mvn -Dglassfish.domain=domain1 -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=15048 -Dglassfish.http.port=15080 -Dversion.number=1.0.0.${BUILD_NUMBER} clean verify'
    }
}

stage "Acceptance Testing"

node {
    git url: 'git@awesomatic.local:train-timetables.git', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5', branch: 'develop'
    withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
        sh "mvn -P acceptance-tests -Dport=15080 clean verify"
    }
}

stage "QA"

parallel (
        'Manual Usability and Exploratory Tests': {
            node {
                git url: 'git@awesomatic.local:train-timetables-deploy', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5'
                withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                    sh 'mvn -Dglassfish.domain=test -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=20048 -Dglassfish.http.port=20080 -Dversion.number=1.0.0.${BUILD_NUMBER} clean verify'
                }
            }
            node {
                sh 'echo Manual Usability and Exploratory Tests'
            }
        },
        'QoS Tests': {
            node {
                git url: 'git@awesomatic.local:train-timetables-deploy', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5'
                withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                    sh 'mvn -Dglassfish.domain=qos -Dglassfish.home=/Library/Glassfish -Dglassfish.admin.port=21048 -Dglassfish.http.port=21080 -Dversion.number=1.0.0.${BUILD_NUMBER} clean verify'
                }
            }
            node {
                git url: 'git@awesomatic.local:train-timetables-qos', credentialsId: '42c4bd47-1df0-4a06-906f-a74fe86425b5'
                withMaven(jdk: 'Oracle JDK', maven: 'Maven') {
                    sh "mvn clean verify"
                }
            }
        }
)

stage "Deploy 2 Prod"

node {
    sh 'echo Deploy 2 Production...'
}