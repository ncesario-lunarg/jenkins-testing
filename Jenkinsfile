pipeline {
    agent any

    triggers {
		githubPush()
    }

    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello world!"'
                sh 'echo "Is it working?"'
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
