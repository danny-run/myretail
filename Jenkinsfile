#!/usr/bin/env groovy
import java.text.SimpleDateFormat

pipeline {


    tools {
        maven 'Maven3'
    }

    stages {

        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('check java') {
            steps {
                sh "java -version"
            }
        }

        stage('clean') {
            steps {
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
                sh "mvn clean"
            }

        }

        stage('backend tests') {
            steps {
                try {
                    sh "mvn verify"
                } catch (err) {
                    throw err
                } finally {
                }
            }
        }

        stage('packaging') {
            steps {
                sh "mvn package"
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }


        def dockerImage
        stage('build docker') {
            steps {
                sh "cp target/*.jar ."
                dockerImage = docker.build('danny/myretail', '.')
            }
        }

    }
}
