pipeline {

    agent any

    stages {

        stage('Initialize') {
            steps {
                echo "PATH = ${PATH}"
                sudo sh pwd
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
