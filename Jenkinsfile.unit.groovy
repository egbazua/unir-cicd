pipeline {
    agent {
        label 'docker'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/unit/*.xml'
            }
        }
        stage('API tests') {
            steps {
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/api/*.xml'
            }
        }
        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/e2e/*.xml'
            }
        }
    }
    post {
        always {
            junit 'results/**/*.xml'
            cleanWs()
        }
        failure {
            script {
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                echo "Sending failure notification for Job: ${jobName}, Build: ${buildNumber}"
                // mail to: 'example@example.com',
                //      subject: "Job '${jobName}' (${buildNumber}) failed",
                //      body: "Job '${jobName}' (${buildNumber}) failed. Please check Jenkins for more details."
            }
        }
    }
}
