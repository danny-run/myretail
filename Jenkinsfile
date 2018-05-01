#!/usr/bin/env groovy
import java.text.SimpleDateFormat

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "mvn clean"
    }

    stage('backend tests') {
        try {
            sh "mvn verify"
        } catch(err) {
            throw err
        } finally {
        }
    }

    stage('packaging') {
        sh "mvn package"
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }


    def dockerImage
    stage('build docker') {
        sh "cp target/*.jar ."
        dockerImage = docker.build('danny/myretail', '.')
    }

/*    stage('publish docker') {
        def dateFormat = new SimpleDateFormat("yyyy.MM.dd")
        def date = new Date()
        docker.withRegistry('https://registry.hub.docker.com', 'dockerhub_jenac') {
            dockerImage.push("${dateFormat.format(date)}.${env.BUILD_NUMBER}")
            dockerImage.push 'latest'
        }
    }*/
}
