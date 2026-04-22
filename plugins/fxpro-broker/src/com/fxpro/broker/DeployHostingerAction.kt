package com.fxpro.broker

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.Messages
import java.io.File

class DeployHostingerAction : AnAction() {
    private val LOG = Logger.getInstance(DeployHostingerAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val basePath = project.basePath ?: return

        val result = Messages.showYesNoDialog(
            project,
            "This will prepare the deployment script for Hostinger VPS. Do you want to proceed?",
            "Deploy to Hostinger",
            Messages.getQuestionIcon()
        )

        if (result != Messages.YES) return

        val scriptPath = "$basePath/plugins/fxpro-broker/scripts/deploy-hostinger.sh"
        val scriptFile = File(scriptPath)

        if (!scriptFile.exists()) {
            Messages.showErrorDialog(project, "Deployment script not found.", "FXPRO Error")
            return
        }

        Messages.showInfoMessage(
            project,
            "Deployment script is ready at: $scriptPath\n\nYou can run this on your Hostinger VPS to set up the environment.",
            "Deployment Ready"
        )
    }
}
