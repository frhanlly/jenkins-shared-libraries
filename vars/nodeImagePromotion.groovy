def call(){

    container('crane'){
        sh '''
            NAME=$(echo $JOB_NAME | cut -d"/" -f1)
            if [ $(echo $TAG_NAME | egrep "^v.+") ] ; then
                OLD_NAME="${NAME}:release-${GIT_COMMIT:0:10}"
                FULL_NAME="${TAG_NAME}"
            elif [ $( echo $GIT_BRANCH | egrep "^release" ) ] ; then
                echo "teste + $GIT_PREVIOUS_COMMIT"
                OLD_NAME="${NAME}:dev-${GIT_COMMIT:0:10}"
                FULL_NAME="release-${GIT_COMMIT:0:10}"
            fi
            crane tag --insecure harbor.hanlly.local/apps/${OLD_NAME} ${FULL_NAME}
            
            

        '''

    }

}