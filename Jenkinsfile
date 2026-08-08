pipeline {

    agent any

    options {
        timestamps()
    }

    environment {
        ANDROID_HOME = '/opt/android-sdk'
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }

    stages {

        stage('Environment') {
            steps {
                sh '''
                    echo "===== Java ====="
                    java -version

                    echo "===== Android SDK ====="
                    echo $ANDROID_HOME

                    echo "===== Gradle ====="
                    chmod +x gradlew
                    ./gradlew --version
                '''
            }
        }

        stage('Unit Test') {
            steps {
                sh './gradlew testDebugUnitTest --stacktrace'
            }
        }

        stage('Lint') {
            steps {
                sh './gradlew lintDebug --stacktrace'
            }
        }

        stage('Build Debug APK') {
            steps {
                sh './gradlew assembleDebug --stacktrace'
            }
        }
    }

    post {

        success {
            echo 'BUILD SUCCESS'
        }

        failure {
            echo 'BUILD FAILED'
        }

        always {
            archiveArtifacts(
                artifacts: '**/build/outputs/apk/**/*.apk',
                allowEmptyArchive: true
            )
        }
    }
}