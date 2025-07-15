pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello world!"'
            }
        }

        stage('Notify - Success') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
				publishChecks(name: 'success')
            }
        }
    }

    post {
        failure {
			publishChecks(name: 'failure')
        }
        unstable {
			publishChecks(name: 'unstable')
        }
    }
}
