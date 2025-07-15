pipeline {
    agent any

    triggers {
		//githubPush()
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello world!"'
				sh 'echo "Step 2"'
            }
        }
    }

    post {
        success {
			publishChecks()
        }
        failure {
			publishChecks()
        }
    }
}
