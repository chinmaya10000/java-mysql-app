#!/usr/bin/env groovy

pipeline {

    agent any

    environment {
        IMAGE_REPO = "chinmayapradhan/java-mysql-app"
        IMAGE_NAME = "1.0-${BUILD_NUMBER}"
        SLACK_CHANNEL = '#java-app'
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