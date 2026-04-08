pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9' // Nome configurado no Jenkins (Global Tool Configuration)
        jdk 'JDK 21'      // Nome configurado no Jenkins (Global Tool Configuration)
    }
    
    environment {
        // Para garantir que usa Java 21
        JAVA_HOME = tool name: 'JDK 21', type: 'jdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Clonando repositório...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compilando projeto...'
                dir('ZeloDesk - BackEnd/zelodesk') {
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Executando testes...'
                dir('ZeloDesk - BackEnd/zelodesk') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    // Publica relatórios JUnit
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Publica relatórios Allure
                    allure includeProperties: false,
                           jdk: '',
                           results: [[path: 'ZeloDesk - BackEnd/zelodesk/target/allure-results']]
                }
            }
        }
        
        stage('Package') {
            when {
                branch 'main' // Só empacota na branch main
            }
            steps {
                echo 'Empacotando aplicação...'
                dir('ZeloDesk - BackEnd/zelodesk') {
                    sh 'mvn package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Pipeline executado com sucesso!'
        }
        failure {
            echo '❌ Pipeline falhou!'
        }
        always {
            echo 'Limpando workspace...'
            cleanWs()
        }
    }
}
