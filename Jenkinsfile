#!/usr/bin/env groovy

pipeline {

    agent any

    environment {
        IMAGE_REPO = "chinmayapradhan/java-mysql-app"
        IMAGE_NAME = "1.0-${BUILD_NUMBER}"
        SLACK_CHANNEL = '#java-app'
    }
    
    stages {
        stage('build app') {
            steps {
                script {
                    echo 'Building the app...'
                    sh './gradlew clean build'
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo 'Building and push Image..'
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${IMAGE_REPO}:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push ${IMAGE_REPO}:${IMAGE_NAME}"
                    }
                }
            }
        }
        stage('Deploy the app') {
            steps {
                script {
                    echo 'deploying docker image to EC2..'
                    def dockerComposeCmd = 'docker-compose up -d'
                    def ec2Instance = 'ec2-user@3.12.154.64'

                    sshagent([ec2-server-key]) {
                        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"
                        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${dockerComposeCmd}"
                    }
                }
            }
        }
    }
    post {
        success {
            slackSend(channel: "${env.SLACK_CHANNEL}", message: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) completed successfully.")
        }
        failure {
            slackSend(channel: "${env.SLACK_CHANNEL}", message: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) failed.")
        }
    }
}
