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
                sh 'echo "Is it working for PRs?"'
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
