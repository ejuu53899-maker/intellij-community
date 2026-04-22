package com.fxpro.broker

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.ui.Messages
import java.io.File

class StartBackendAction : AnAction() {
    private val LOG = Logger.getInstance(StartBackendAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val basePath = project.basePath ?: return

        LOG.info("Attempting to start FXPRO Backend...")

        val scriptName = if (SystemInfo.isWindows) "start-backend.bat" else "start-backend.sh"
        val scriptPath = "$basePath/plugins/fxpro-broker/scripts/$scriptName"
        val scriptFile = File(scriptPath)

        if (!scriptFile.exists()) {
            val errorMsg = "Startup script not found at: $scriptPath"
            LOG.error(errorMsg)
            Messages.showErrorDialog(project, errorMsg, "FXPRO Error")
            return
        }

        try {
            val processBuilder = if (SystemInfo.isWindows) {
                ProcessBuilder("cmd.exe", "/c", scriptPath)
            } else {
                ProcessBuilder("/bin/bash", scriptPath)
            }

            processBuilder.directory(File(basePath))
            processBuilder.start()

            LOG.info("FXPRO Backend startup script triggered successfully.")
            Messages.showInfoMessage(project, "FXPRO Backend startup sequence initiated.", "FXPRO Status")
        } catch (ex: Exception) {
            LOG.error("Failed to execute FXPRO Backend startup script", ex)
            Messages.showErrorDialog(project, "Failed to start backend: ${ex.message}", "FXPRO Error")
        }
    }
}
