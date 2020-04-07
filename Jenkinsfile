String getDiscordMessage() {
    def msg = "**Status:** " + currentBuild.currentResult.toLowerCase() + "\n"
    msg += "**Branch:** ${BRANCH_NAME}\n"
    msg += "**Changes:**\n"
    if (!currentBuild.changeSets.isEmpty()) {
        currentBuild.changeSets.first().getLogs().each {
            msg += "- `" + it.getCommitId().substring(0, 8) + "` *" + it.getComment().substring(0, Math.min(64, it.getComment().length() - 1)) + (it.getComment().length() - 1 > 64 ? "..." : "") + "*\n"
        }
    } else {
        msg += "- no changes\n"
    }

    msg += "\n**Artifacts:**\n"
    currentBuild.rawBuild.getArtifacts().each {
        msg += "- [" + it.getDisplayPath() + "](" + env.BUILD_URL + "artifact/" + it.getHref() + ")\n"
    }

    if (msg.length() > 2048) {
        msg.take(msg.length() - 2048)
    }

    return msg.length() > 2048 ? msg.substring(0, 2048) : msg
}

pipeline {
    agent any
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

            withCredentials([string(credentialsId: 'discord_webhook', variable: 'discordWebhook')]) {
                discordSend thumbnail: "https://www.daporkchop.net/toembed/profilepic-256p.png",
                        result: currentBuild.currentResult,
                        description: getDiscordMessage(),
                        link: env.BUILD_URL,
                        title: "JenkinsTestThing:${BRANCH_NAME} #${BUILD_NUMBER}",
                        webhookURL: "${discordWebhook}"
            }
        }
    }
}