package com.fxpro.broker

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.diagnostic.Logger

class KickstartAction : AnAction() {
    private val LOG = Logger.getInstance(KickstartAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        LOG.info("KickstartAction triggered. Opening FXPRO Dashboard...")
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FXPRO Dashboard")
        if (toolWindow != null) {
            toolWindow.show {
                LOG.info("FXPRO Dashboard tool window shown.")
            }
        } else {
            LOG.error("FXPRO Dashboard tool window not found.")
        }
    }
}
