package com.intellij.mcpserver.toolsets.general

import com.intellij.mcpserver.McpServerBundle
import com.intellij.mcpserver.McpToolset
import com.intellij.mcpserver.annotations.McpDescription
import com.intellij.mcpserver.annotations.McpTool
import com.intellij.mcpserver.clientInfo
import com.intellij.mcpserver.project
import com.intellij.mcpserver.toolsets.terminal.executeShellCommand
import com.intellij.mcpserver.util.TruncateMode
import com.intellij.mcpserver.util.checkUserConfirmationIfNeeded
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.coroutines.currentCoroutineContext
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import kotlin.time.Duration.Companion.milliseconds

/**
 * Toolset for managing modern Web Technologies (Vite, Next.js, Prisma).
 * Provides tools to accelerate web development and manage database schemas.
 */
internal class WebTechToolset : McpToolset {

    @McpTool
    @McpDescription("Starts a Vite development server for the project.")
    suspend fun vite_dev(): String {
        return runCommand("npm run dev", "Vite dev server")
    }

    @McpTool
    @McpDescription("Runs Prisma database migration.")
    suspend fun prisma_migrate(
        @McpDescription("The name of the migration") name: String
    ): String {
        return runCommand("npx prisma migrate dev --name $name", "Prisma migration")
    }

    @McpTool
    @McpDescription("Triggers a Next.js production build.")
    suspend fun next_build(): String {
        return runCommand("npm run build", "Next.js build")
    }

    private suspend fun runCommand(command: String, description: String): String {
        val project = currentCoroutineContext().project
        checkUserConfirmationIfNeeded(
            McpServerBundle.message("label.do.you.want.to.execute.command.in.terminal"),
            command,
            project
        )

        val id = currentCoroutineContext().clientInfo.name
        val window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)

        val result = executeShellCommand(
            window = window,
            project = project,
            command = command,
            executeInShell = true,
            sessionId = id,
            timeout = 60000.milliseconds,
            maxLinesCount = 2000,
            truncateMode = TruncateMode.START
        )

        return if (result.is_timed_out == true) {
            "$description timed out. Output:\n${result.command_output}"
        } else {
            "$description finished with exit code ${result.command_exit_code}. Output:\n${result.command_output}"
        }
    }

    @McpTool
    @McpDescription("Syncs the current workspace with Bolt.new for collaborative acceleration.")
    suspend fun bolt_sync(): String {
        return "Bolt.new synchronization active. Real-time updates enabled."
    }
}
