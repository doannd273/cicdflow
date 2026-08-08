# pipeline { ... }
# Đây là khối lớn nhất của Jenkinsfile.
#
# Nó mô tả toàn bộ quy trình CI/CD mà Jenkins sẽ thực hiện.
#
# Có thể hiểu đơn giản:
#
# Jenkins nhận source code Android
#        ↓
# kiểm tra môi trường
#        ↓
# chạy Unit Test
#        ↓
# chạy Android Lint
#        ↓
# build APK
#        ↓
# lưu file APK
#
pipeline {

    # ============================================================
    # 1. AGENT
    # ============================================================

    # agent cho Jenkins biết:
    #
    # "Pipeline này sẽ chạy ở máy nào?"
    #
    # agent any nghĩa là:
    #
    # Jenkins có thể chọn bất kỳ Jenkins Agent nào đang available
    # để chạy pipeline này.
    #
    # Trong trường hợp Jenkins của bạn đang chạy bằng Docker
    # và chỉ có một Jenkins node duy nhất,
    # thì toàn bộ lệnh bên dưới sẽ chạy trong Jenkins container đó.
    #
    # Ví dụ:
    #
    # MacBook
    #   |
    #   └── Docker
    #         |
    #         └── Jenkins Container
    #               |
    #               └── chạy pipeline Android
    #
    agent any


    # ============================================================
    # 2. OPTIONS
    # ============================================================

    options {

        # timestamps()
        #
        # Jenkins sẽ thêm thời gian vào đầu mỗi dòng log.
        #
        # Thay vì:
        #
        # > Task :app:compileDebugKotlin
        #
        # Jenkins sẽ hiển thị kiểu:
        #
        # 16:10:20 > Task :app:compileDebugKotlin
        #
        # Nó rất hữu ích khi muốn kiểm tra:
        #
        # - bước nào chạy lâu
        # - build mất bao nhiêu phút
        # - lỗi xảy ra lúc nào
        #
        timestamps()
    }


    # ============================================================
    # 3. ENVIRONMENT VARIABLES
    # ============================================================

    # environment dùng để khai báo các biến môi trường
    # sử dụng trong toàn bộ pipeline.
    #
    # Có thể hiểu tương tự như việc khai báo biến dùng chung.
    #
    environment {

        # --------------------------------------------------------
        # ANDROID_HOME
        # --------------------------------------------------------

        # ANDROID_HOME chỉ cho Gradle / Android Build Tools biết:
        #
        # "Android SDK đang được cài ở đâu?"
        #
        # Ở đây Android SDK nằm tại:
        #
        # /opt/android-sdk
        #
        # Đây thường là đường dẫn Android SDK mà bạn đã cài
        # bên trong Docker image Jenkins.
        #
        # Bên trong thư mục này thường có:
        #
        # /opt/android-sdk
        #      ├── build-tools
        #      ├── platforms
        #      ├── platform-tools
        #      └── cmdline-tools
        #
        # Ví dụ Gradle cần compile:
        #
        # compileSdk = 35
        #
        # thì nó sẽ tìm:
        #
        # /opt/android-sdk/platforms/android-35
        #
        ANDROID_HOME = '/opt/android-sdk'


        # --------------------------------------------------------
        # GRADLE_USER_HOME
        # --------------------------------------------------------

        # Gradle cần một thư mục để lưu:
        #
        # - dependency cache
        # - Gradle cache
        # - metadata
        # - các file Gradle tạm
        #
        # Bình thường trên máy local nó thường nằm ở:
        #
        # ~/.gradle
        #
        # Nhưng trên Jenkins chúng ta cho nó nằm trong:
        #
        # ${WORKSPACE}/.gradle
        #
        # WORKSPACE là thư mục Jenkins đang checkout source code.
        #
        # Ví dụ:
        #
        # /var/jenkins_home/workspace/android-project/
        #
        # thì:
        #
        # GRADLE_USER_HOME
        #
        # sẽ trở thành:
        #
        # /var/jenkins_home/workspace/android-project/.gradle
        #
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }


    # ============================================================
    # 4. STAGES
    # ============================================================

    # stages chứa toàn bộ các bước chính của pipeline.
    #
    # Một pipeline thường được chia thành nhiều stage:
    #
    # Environment
    #      ↓
    # Unit Test
    #      ↓
    # Lint
    #      ↓
    # Build APK
    #
    # Nếu một stage bị lỗi,
    # các stage phía sau thông thường sẽ không tiếp tục chạy.
    #
    stages {


        # ========================================================
        # STAGE 1: KIỂM TRA MÔI TRƯỜNG BUILD
        # ========================================================

        stage('Environment') {

            # steps chứa các command thực sự Jenkins sẽ chạy.
            steps {

                # sh nghĩa là:
                #
                # chạy Shell Command trên Linux/macOS.
                #
                # Vì Jenkins của chúng ta chạy trong Linux Docker container,
                # nên các command này chạy bên trong container Jenkins.
                #
                # ''' ... '''
                #
                # cho phép viết nhiều dòng shell command.
                #
                sh '''

                    echo "===== Java ====="

                    # Kiểm tra Java version đang được sử dụng.
                    #
                    # Android Gradle Plugin yêu cầu JDK phù hợp.
                    #
                    # Ví dụ có thể hiển thị:
                    #
                    # openjdk version "21.0.x"
                    #
                    java -version


                    echo "===== Android SDK ====="

                    # In ra đường dẫn Android SDK.
                    #
                    # Kết quả mong đợi:
                    #
                    # /opt/android-sdk
                    #
                    echo $ANDROID_HOME


                    echo "===== Gradle ====="

                    # gradlew là Gradle Wrapper của project Android.
                    #
                    # Khi clone source code từ Git,
                    # đôi khi file gradlew không có quyền execute.
                    #
                    # chmod +x gradlew
                    #
                    # sẽ cấp quyền chạy cho file gradlew.
                    #
                    chmod +x gradlew


                    # Kiểm tra Gradle Wrapper có chạy được không.
                    #
                    # Nó sẽ in ra thông tin như:
                    #
                    # Gradle 8.x
                    # Kotlin ...
                    # JVM ...
                    # OS ...
                    #
                    # Nếu command này chạy thành công,
                    # nghĩa là ít nhất:
                    #
                    # Java OK
                    # Gradle OK
                    # Source code OK
                    #
                    ./gradlew --version
                '''
            }
        }


        # ========================================================
        # STAGE 2: UNIT TEST
        # ========================================================

        stage('Unit Test') {

            steps {

                # Chạy Local Unit Test của Debug build.
                #
                # Command này tương đương khi bạn chạy trên Android Studio:
                #
                # ./gradlew :app:testDevDebugUnitTest
                # ./gradlew :app:testProdDebugUnitTest
                #
                # Nó chạy các test thường nằm trong:
                #
                # app/src/test/
                #
                # Ví dụ:
                #
                # app/src/test/java/...
                #
                # Những test này chạy trên JVM,
                # KHÔNG cần Android Emulator.
                #
                # Ví dụ:
                #
                # UserRepositoryTest
                # LoginViewModelTest
                # CalculatePriceUseCaseTest
                #
                # Nếu có một Unit Test fail:
                #
                # stage này FAIL
                #
                # pipeline sẽ được đánh dấu FAILURE.
                #
                #
                # --stacktrace
                #
                # yêu cầu Gradle in stacktrace chi tiết khi xảy ra lỗi.
                #
                # Nó giúp chúng ta debug dễ hơn trên Jenkins.
                #
                sh './gradlew :app:testDevDebugUnitTest --stacktrace'
                sh './gradlew :app:testProdDebugUnitTest --stacktrace'
            }
        }


        # ========================================================
        # STAGE 3: ANDROID LINT
        # ========================================================

        stage('Lint') {

            steps {

                # Android Lint là tool phân tích source code Android.
                #
                # Nó tìm các vấn đề như:
                #
                # - API sử dụng không đúng
                # - thiếu permission
                # - resource lỗi
                # - accessibility issue
                # - hardcoded text
                # - performance warning
                # - security warning
                #
                # lintDevDebug / lintProdDebug nghĩa là:
                #
                # chạy lint cho Debug Variant.
                #
                # Có thể hiểu:
                #
                # Source code
                #      ↓
                # Android Lint
                #      ↓
                # kiểm tra các vấn đề Android
                #
                sh './gradlew :app:lintDevDebug --stacktrace'
                sh './gradlew :app:lintProdDebug --stacktrace'
            }
        }


        # ========================================================
        # STAGE 4: BUILD DEBUG APK
        # ========================================================

        stage('Build Debug APK') {

            steps {

                # assembleDevDebug / assembleProdDebug sẽ build Debug APK.
                #
                # Nó gần tương đương việc trong Android Studio:
                #
                # Build
                # → Build APK(s)
                #
                #
                # Gradle sẽ thực hiện nhiều bước:
                #
                # Kotlin Source
                #      ↓
                # Compile Kotlin
                #      ↓
                # Compile Resources
                #      ↓
                # Merge Manifest
                #      ↓
                # D8 / Dex
                #      ↓
                # Package APK
                #
                #
                # Sau khi thành công,
                # APK thường nằm ở:
                #
                # app/build/outputs/apk/dev/debug/app-dev-debug.apk
                # app/build/outputs/apk/prod/debug/app-prod-debug.apk
                #
                sh './gradlew :app:assembleDevDebug --stacktrace'
                sh './gradlew :app:assembleProdDebug --stacktrace'
            }
        }
    }


    # ============================================================
    # 5. POST
    # ============================================================

    # post là các hành động Jenkins sẽ thực hiện
    # SAU KHI stages hoàn thành.
    #
    # Nó tương tự:
    #
    # try {
    #
    #     runPipeline()
    #
    # } finally {
    #
    #     doSomething()
    #
    # }
    #
    post {


        # ========================================================
        # PIPELINE THÀNH CÔNG
        # ========================================================

        success {

            # Chỉ chạy khi tất cả stage thành công.
            #
            # Ví dụ:
            #
            # Environment  ✅
            # Unit Test    ✅
            # Lint         ✅
            # Build APK    ✅
            #
            echo 'BUILD SUCCESS'
        }


        # ========================================================
        # PIPELINE THẤT BẠI
        # ========================================================

        failure {

            # Chạy khi pipeline bị fail.
            #
            # Ví dụ:
            #
            # Environment  ✅
            # Unit Test    ❌
            #
            # thì Jenkins sẽ đi vào failure.
            #
            echo 'BUILD FAILED'
        }


        # ========================================================
        # ALWAYS
        # ========================================================

        always {

            # always nghĩa là:
            #
            # bất kể pipeline SUCCESS hay FAILURE
            # Jenkins vẫn chạy đoạn code này.
            #
            # Ở đây chúng ta muốn Jenkins tìm và lưu APK.
            #
            archiveArtifacts(

                # ------------------------------------------------
                # artifacts
                # ------------------------------------------------

                # Jenkins sẽ tìm tất cả file .apk nằm trong:
                #
                # build/outputs/apk/
                #
                # Ví dụ:
                #
                # app/build/outputs/apk/debug/app-debug.apk
                #
                # hoặc project multi-module:
                #
                # app/build/outputs/apk/debug/app-debug.apk
                # demo/build/outputs/apk/debug/demo-debug.apk
                #
                # ** nghĩa là có thể match nhiều thư mục.
                #
                # *.apk nghĩa là tất cả file kết thúc bằng .apk
                #
                artifacts: '**/build/outputs/apk/**/*.apk',


                # ------------------------------------------------
                # allowEmptyArchive
                # ------------------------------------------------

                # Nếu không tìm thấy APK thì Jenkins
                # KHÔNG fail chỉ vì archive không có file.
                #
                # Điều này cần thiết vì giả sử:
                #
                # Unit Test FAIL
                #
                # thì stage Build APK chưa được chạy.
                #
                # Khi đó đương nhiên không có APK.
                #
                # Nếu đặt:
                #
                # allowEmptyArchive: false
                #
                # Jenkins có thể báo thêm lỗi archive artifact.
                #
                # true giúp tránh lỗi phụ không cần thiết.
                #
                allowEmptyArchive: true
            )
        }
    }
}
