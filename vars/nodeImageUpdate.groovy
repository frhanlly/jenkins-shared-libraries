def call(){
    container('alpine'){
        sh '''

        NAME=$(echo $JOB_NAME | cut -d"/" -f1)
        FULL_NAME="${NAME}:dev-${GIT_COMMIT:0:10}"

        VALUES_FILE="charts/values.yaml"

        apk add git openssh
        ssh-keyscan github.com > ${HOME}/.ssh/known_hosts
        git clone git@github.com:frhanlly/simple-app-helm.git
        cd simple-app-helm
        git switch development
        
        git config --global user.email "jenkins@hanlly.local"
        git config --global user.name "Jenkins pipeline"

        sed -i -E 's/tag:.+/tag: dev-${GIT_COMMIT:0:10}/' ${VALUES_FILE}

        git commit -a -m "update image TAG to dev-${GIT_COMMIT:0:10}"
        git push origin

        '''
    }


}