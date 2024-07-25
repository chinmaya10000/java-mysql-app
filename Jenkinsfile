#!/usr/bin/env groovy

pipeline {

    agent any
    environment {
        ECR_REPO_URL = '381491975963.dkr.ecr.us-east-2.amazonaws.com'
        IMAGE_NAME = '${ECR_REPO_URL}/java-mysql-app'
        IMAGE_TAG = '1.0-${BUILD_NUMBER}'
        CLUSTER_NAME = 'my-cluster'
        CLUSTER_REGION = 'us-east-2'
        AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
        SLACK_CHANNEL = '#prometheus-alerts'
    }

    stages {
        stage('Build app') {
            steps {
                script {
                    echo 'Building the application..'
                    sh './gradlew clean build'
                }
            }
        }
        stage('build iamge') {
            steps {
                script {
                    echo "Building the Docker image.."
                    withCredentials([usernamePassword(credentialsId: 'ecr-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin ${ECR_REPO_URL}"
                        sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    }
                }
            }
        }
        stage('Deplou') {
            environment {
                APP_NAME = 'java-app'
                APP_NAMESPACE = 'my-app'
                DB_USER_SECRET = credentials('db_user')
                DB_PASS_SECRET = credentials('db_pass')
                DB_NAME_SECRET = credentials('db_name')
                DB_ROOT_PASS_SECRET = credentials('db_root_pass')
            }
            steps {
                script {
                    sh "aws eks update-kubeconfig --name ${CLUSTER_NAME} --region ${CLUSTER_REGION}"

                    env.DB_USER = sh(script: 'echo -n $DB_USER_SECRET | base64', returnStdout: true).trim()
                    env.DB_PASS = sh (script: 'echo -n $DB_PASS_SECRET | base64', returnStdout: true).trim()
                    env.DB_NAME = sh (script: 'echo -n $DB_NAME_SECRET | base64', returnStdout: true).trim()
                    env.DB_ROOT_PASS = sh (script: 'echo -n $DB_ROOT_PASS_SECRET | base64', returnStdout: true).trim()
                    
                    echo 'Deploying new releas to EKS..'
                    sh 'envsubst < ci-cd/db-config-cicd.yaml | kubectl apply -f -'
                    sh 'envsubst < ci-cd/db-secret-cicd.yaml | kubectl apply -f -'
                    sh 'envsubst < ci-cd/java-app-cicd.yaml | kubectl apply -f -'
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