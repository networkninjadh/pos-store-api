pipeline {

    agent any

    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk11'
    }

    stages {

        stage('Initialize') {
            echo "PATH = ${PATH}"
            echo "M2_HOME = ${M2_HOME}"
        }

        stage('Build') {
            echo "Build Stage"
        }

        stage('Test') {
            echo "Test Stage"
        }

        stage('package') {

        }
    }
}
