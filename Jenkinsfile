pipeline {

    agent any

    stages {

        stage('Initialize') {
            steps {
                echo "PATH = ${PATH}"
            }

        }

        stage('Build') {
            steps {
                echo "Build Stage"
                sh 'mvn -Dmaven.test.failure.ignore=true install'
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
