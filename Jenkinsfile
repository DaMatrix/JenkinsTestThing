def branch = BRANCH_NAME
def version
pipeline {
    agent any
    try {
        tools {
            maven 'Maven 3'
            jdk 'jdk8'
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn clean package'
                }
                post {
                    success {
                        archiveArtifacts artifacts: 'target/*.jar'
                    }
                }
            }
        }

        post {
            always {
                deleteDir()
            }
        }
    } catch (err) {
        //currentBuild.result = 'FAILURE'
    } finally {
        stage('Discord notify') {
            def artifactUrl = env.BUILD_URL + "artifact/"
            def msg = "**Status:** " + currentBuild.currentResult.toLowerCase() + "\n"
            msg += "**Branch:** ${branch}\n"
            msg += "**Changes:**\n"
            if (!currentBuild.changeSets.isEmpty()) {
                currentBuild.changeSets.first().getLogs().each {
                    msg += "- `" + it.getCommitId().substring(0, 8) + "` *" + it.getComment().substring(0, Math.min(64, it.getComment().length() - 1)) + (it.getComment().length() - 1 > 64 ? "..." : "") + "*\n"
                }
            } else {
                msg += "- no changes\n"
            }

            def filename
            msg += "\n**Artifacts:**\n"
            currentBuild.rawBuild.getArtifacts().each {
                filename = it.getFileName()
                msg += "- [${filename}](${artifactUrl}${it.getFileName()})\n"
            }

            if (msg.length() > 2048) {
                msg.take(msg.length() - 2048)
            }

            withCredentials([string(credentialsId: 'discord_webhook', variable: 'discordWebhook')]) {
                discordSend thumbnail: "https://static.miraheze.org/valkyrienskieswiki/6/63/Logo_128.png", successful: currentBuild.resultIsBetterOrEqualTo('SUCCESS'), description: "${msg}", link: env.BUILD_URL, title: "JenkinsTestThing:${branch} #${BUILD_NUMBER}", webhookURL: "${discordWebhook}"
            }
        }
    }
}