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
    }

    stages {
        stage('Build app') {
            steps {
                script {
                    echo 'building the app'
                }
            }
        }
        stage('build iamge') {
            steps {
                script {
                    echo "Building the image.."
                }
            }
        }
        stage('Deplou') {
            steps {
                script {
                    echo "Deploy to eks"
                    
                    sh "aws eks update-kubeconfig --name ${CLUSTER_NAME} --region ${CLUSTER_REGION}"

                    sh 'kubectl create deployment nginx-deployment --image=nginx'
                }
            }
        }
    }
}