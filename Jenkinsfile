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
