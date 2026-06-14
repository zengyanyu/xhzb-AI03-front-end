pipeline {
    agent any
    options {
        timestamps()
    }
    stages {
        stage('清除工作空间') {
            steps {
                cleanWs()
            }
        }
        stage('拉取Git代码') {
            steps {
                echo "正在拉取代码..."
                echo "当前分支:${GIT_TAG},当前服务:ruoyi-vue"
                checkout([$class: 'GitSCM',
                          branches: [[name: GIT_TAG]],
                          doGenerateSubmoduleConfigurations: false,
                          extensions: [],
                          submoduleCfg: [],
                          userRemoteConfigs: [[credentialsId: 'git', url: GIT_URL]]
                ])
                sh "pwd"
            }
        }
        stage('重新构建镜像') {
            steps {
                echo "当前打镜像tag:${DOCKER_TAG}"
                script {
                    sh "pwd"
                    sh "docker build -t zzyl-vue:${DOCKER_TAG} -f ./Dockerfile ."
                }
            }
        }
        stage('部署服务'){
            steps {
                script {
                    sh "pwd"
                    sh "cd `pwd`"
                    echo "部署升级:zzyl-vue前端服务"
                    sh "chmod +x ./deploy.sh && sh ./deploy.sh zzyl-vue ${DOCKER_TAG}"
                }
            }
        }
    }
}