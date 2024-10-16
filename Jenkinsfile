pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                script {
                    git 'https://github.com/chinmaya10000/java-mysql-app.git'
                }
            }
        }
        stage('Secret Scanning with Gitleaks') {
            steps {
                // Run Gitleaks inside a Docker container
                script {
                    docker.image('zricethezav/gitleaks:latest').inside {
                        // Ensure .gitleaks.toml is in the correct path in your repository
                        sh '''
                           gitleaks detect --source=. --config-path=.gitleaks.toml -v --report-path=gitleaks-report.json
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            // Archive the Gitleaks report in Jenkins
            archiveArtifacts artifacts: 'gitleaks-report.json'
        }
        failure {
            echo 'Secrets detected by Gitleaks. Please check the report.'
        }
    }
}
