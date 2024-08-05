#!/usr/bin/env groovy

pipeline {

    agent any
    environment {
        ECR_REPO_URL = '381491975963.dkr.ecr.us-east-2.amazonaws.com'
        IMAGE_NAME = "${ECR_REPO_URL}/java-app"
        IMAGE_TAG = "1.0-${BUILD_NUMBER}"
        CLUSTER_NAME = "my-cluster"
        CLUSTER_REGION = "us-east-2"
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
                    echo 'Building the docker image...'
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "aws ecr get-login-password --region ${CLUSTER_REGION} | docker login --username AWS --password-stdin ${ECR_REPO_URL}"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
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