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
