pipeline {
    agent any

    triggers {
        // Trigger on GitHub PR events via webhook
        githubPush()
    }

	environment {
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello world!"'
				sh 'echo "Step 2"'
            }
        }

        // NOTE: the 'githubNotify' commands below will likely not work without further setup
        stage('Notify - Success') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                //githubNotify context: 'Jenkins CI', status: 'SUCCESS', description: 'Build passed!'
				publishChecks(name: 'success')
            }
        }
    }

    post {
        failure {
            //githubNotify context: 'Jenkins CI', status: 'FAILURE', description: 'Build failed.'
			publishChecks(name: 'failure')
        }
        unstable {
            //githubNotify context: 'Jenkins CI', status: 'ERROR', description: 'Build unstable or with test errors.'
			publishChecks(name: 'unstable')
        }
    }
}
