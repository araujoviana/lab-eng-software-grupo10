def repoUrl = System.getenv('GITHUB_REPO_URL') ?: 'https://github.com/araujoviana/lab-eng-software-grupo10.git'
def repoBranch = System.getenv('GITHUB_REPO_BRANCH') ?: 'main'

pipelineJob('zelodesk-ci') {
  description('CI pipeline for ZeloDesk. Managed by Job DSL.')

  logRotator {
    numToKeep(20)
    artifactNumToKeep(10)
  }

  triggers {
    githubPush()
  }

  definition {
    cpsScm {
      scm {
        git {
          remote {
            url(repoUrl)
          }
          branches("*/${repoBranch}")
        }
      }
      scriptPath('Jenkinsfile')
      lightweight(true)
    }
  }
}
