#!/usr/bin/env groovy

@Library('jenkins-shared-library')_

pipeline {

    agent any
    
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
                    buildImage 'chinmayapradhan/java-mysql-app:1.0'
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
