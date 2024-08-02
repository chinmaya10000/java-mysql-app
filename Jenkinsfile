#!/usr/bin/env groovy

@Library('jenkins-shared-library')_

pipeline {

    agent any
    environment {
        IMAGE_NAME = 'chinmayapradhan/java-mysql-app:1.0'
    }
    
    stages {
        stage('build app') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    buildImage(env.IMAGE_NAME)
                    dockerlogin()
                    dockerPush(env.IMAGE_NAME)
                }
            }
        }
        stage('Deploy the app') {
            steps {
                script {
                    echo 'deploying docker image to EC2..'
                }
            }
        }
    }
}
