pipeline {

    agent any

    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk11'
    }

    stages {

        stage('Initialize') {
            steps {
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
            }

        }

        stage('Build') {
            steps {
                echo "Build Stage"
            }
        }

        /*
        stage('Test') {
        }

        stage('package') {

        }
        */
    }
}
