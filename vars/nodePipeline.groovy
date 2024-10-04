def call(body) {
  pipeline {
    agent {
      kubernetes {
yaml '''
apiVersion: v1
kind: Pod
spec:
  volumes:
    - name: registry-credentials
      secret:
        secretName: "harbor-credentials"
        items:
        - key: ".dockerconfigjson"
          mode: 0777
          path: "config.json"
    - name: sshkey
      secret:
        secretName: ssh-github
        items:
        - key: "ssh_github"
          mode: 0600
          path: "ssh_github"
  containers:
  - name: alpine
    image: alpine
    command:
    - sleep
    args:
    - infinity
    volumeMounts:
    - mountPath: /root/.ssh/id_rsa
      name: sshkey
      subPath: ssh_github
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - sleep
    args:
    - infinity
    volumeMounts:
    - mountPath: /kaniko/.docker/config.json
      name: registry-credentials
      subPath: config.json
  - name: crane
    image: gcr.io/go-containerregistry/crane:debug
    command:
    - sleep
    args:
    - infinity
    volumeMounts:
    - mountPath: /root/.docker/config.json
      name: registry-credentials
      subPath: config.json
  hostAliases:
  - ip: 192.168.0.155
    hostnames:
    - harbor.hanlly.local
    - argocd.hanlly.local

'''

      }
    }


    stages {
      stage("build and push image to registry"){
        steps{
          nodeBuild()
        }

        when {
          anyOf {
            branch 'development';
          }
        }
      } 


      stage("Artifact Promotion"){
        steps{
          nodeImagePromotion()
        }

        when {
          anyOf {
            branch 'main';
            branch 'release'
          }
        }
      } 
    
      stage("Updating image version in DEVELOPMENT"){
        steps{
          nodeImageUpdate()
        }

        when {
          anyOf {
            branch 'development';
          }
        }
      }

    } 


  }
}
