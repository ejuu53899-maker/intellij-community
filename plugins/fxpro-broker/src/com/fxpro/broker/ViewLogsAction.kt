package com.fxpro.broker

import com.intellij.ide.actions.ShowLogAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ViewLogsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ShowLogAction.showLog()
    }
}
