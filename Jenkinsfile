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
		
		stage('Verify') {
			steps {
				echo 'Compilando projeto, executando testes e gerando cobertura...'
				dir('ZeloDesk - BackEnd/zelodesk') {
					sh 'mvn clean verify'
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

					// Publica relatório HTML do JaCoCo
					publishHTML(target: [
						allowMissing: false,
						alwaysLinkToLastBuild: true,
						keepAll: true,
						reportDir: 'ZeloDesk - BackEnd/zelodesk/target/site/jacoco',
						reportFiles: 'index.html',
						reportName: 'JaCoCo Coverage Report'
					])
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
