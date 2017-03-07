#!/usr/bin/env groovy

node {
 try {

     stage 'Build and Deploy'
        docker.image('dr.gopaktor.com/paktor/build-image:latest').inside {
        checkout scm

        def function_name = "neo4jCounterReport"
        def region = "ap-southeast-1"
        def file_path = "./lambda-function/target/lambda-function.jar"

        sh "mvn package"

        sh "aws configure set default.region ${region}"

        // uploads jar to AWS lambda
        sh "aws lambda update-function-code --function-name ${function_name} --zip-file fileb://${file_path}"

        // takes a snapshot of the code+config, generates a version number
        def version = sh(script:"aws lambda publish-version \
                           --function-name ${function_name}  \
                           --description \"Deployed by Jenkins. Build ${build_number}.\" | jq -r .Version", returnStdout: true).trim()

        // Updating the PROD alias so it points to the new function
        sh "aws lambda update-alias --function-name ${function_name} --function-version ${version} --name PROD
     }

     currentBuild.result = 'SUCCESS'

    } catch (Exception e) {
            echo "$e"

            currentBuild.result = 'FAILURE'
    }

    stage('Slack Notification') {
       slackNotify(currentBuild, "#server-side", "paktor", "slack-paktor-token")
    }
}
