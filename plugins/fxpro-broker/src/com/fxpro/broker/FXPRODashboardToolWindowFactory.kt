package com.fxpro.broker

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.ResourceUtil
import javax.swing.JComponent

class FXPRODashboardToolWindowFactory : ToolWindowFactory {
    private val LOG = Logger.getInstance(FXPRODashboardToolWindowFactory::class.java)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        LOG.info("Creating FXPRO Dashboard content...")
        try {
            val dashboardView = FXPRODashboardView()
            val content = ContentFactory.getInstance().createContent(dashboardView.getComponent(), "", false)
            toolWindow.contentManager.addContent(content)
            LOG.info("FXPRO Dashboard content added successfully. JCEF is ready.")
        } catch (e: Exception) {
            LOG.error("Failed to create FXPRO Dashboard content. Ensure JCEF is supported in this environment.", e)
        }
    }

    override fun shouldBeAvailable(project: Project): Boolean = true
}

class FXPRODashboardView {
    private val browser = JBCefBrowser()

    init {
        val resource = ResourceUtil.getResource(javaClass.classLoader, "dashboard", "index.html")
        if (resource != null) {
            val url = resource.toExternalForm()
            browser.loadURL(url)
            Logger.getInstance(FXPRODashboardView::class.java).info("Dashboard loaded from: $url")
        } else {
            val errorMsg = "Error: Dashboard resources (index.html) not found in plugin package."
            browser.loadHTML("<html><body style='background:#1e1e1e;color:red;'><h1>$errorMsg</h1></body></html>")
            Logger.getInstance(FXPRODashboardView::class.java).error(errorMsg)
        }
    }

    fun getComponent(): JComponent {
        return browser.component
    }
}
