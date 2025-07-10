pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello world!"'
				sh 'echo "See if this triggers automatically"'
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
