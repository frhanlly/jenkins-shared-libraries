def call() {
    container('kaniko'){
              
    
        sh '''
        NAME=$(echo $JOB_NAME | cut -d"/" -f1)
        FULL_NAME="${NAME}:dev-${GIT_COMMIT:0:10}"
        ls /kaniko/.docker/config.json
        cat /kaniko/.docker/config.json
        echo "Fazendo build e push da imagem --------- ${TAG_NAME}" ; pwd
        /kaniko/executor --skip-tls-verify -d harbor.hanlly.local/apps/$FULL_NAME -c $(pwd)
            

        '''
    }

}
