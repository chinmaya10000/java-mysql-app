#!/usr/bin/env groovy

pipeline {

    agent any

    environment {
        ECR_REPO_URL = '381491975963.dkr.ecr.us-east-2.amazonaws.com'
        IMAGE_REPO = "${ECR_REPO_URL}/java-mysql-app"
        IMAGE_NAME = "1.0-${BUILD_NUMBER}"
        AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
        SLACK_CHANNEL = '#java-app'
    }

    stages {
        stage('build app') {
            steps {
                script {
                    echo 'Building the application..'
                    sh './gradlew clean build'
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo 'Build and Push Image..'
                    withCredentials([usernamePassword(credentialsId: 'ecr-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${IMAGE_REPO}:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin ${ECR_REPO_URL}"
                        sh "docker push ${IMAGE_REPO}:${IMAGE_NAME}"
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