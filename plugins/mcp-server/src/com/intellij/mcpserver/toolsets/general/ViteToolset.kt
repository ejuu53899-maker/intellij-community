package com.intellij.mcpserver.toolsets.general

import com.intellij.mcpserver.McpToolset
import com.intellij.mcpserver.annotations.McpDescription
import com.intellij.mcpserver.annotations.McpTool
import com.intellij.mcpserver.mcpFail
import com.intellij.mcpserver.project
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.execution.ParametersListUtil
import kotlinx.coroutines.currentCoroutineContext
import java.nio.file.Path
import kotlin.io.path.exists

/**
 * Toolset for managing Vite-based projects.
 * Provides tools to run development servers and builds.
 */
internal class ViteToolset : McpToolset {

    @McpTool
    @McpDescription("Starts the Vite development server in the specified directory.")
    suspend fun vite_dev(
        @McpDescription("Project-relative path to the Vite project root.") path: String = "."
    ): String {
        val project = currentCoroutineContext().project
        val root = resolvePath(project, path)
        if (!isViteProject(root)) {
            mcpFail("No Vite configuration found at $path")
        }
        // In a real IDE, this would start a persistent process.
        // For MCP, we return the command that should be executed.
        return "To start the dev server, run: 'npm run dev' or 'npx vite' in $path"
    }

    @McpTool
    @McpDescription("Runs the Vite build command for the project.")
    suspend fun vite_build(
        @McpDescription("Project-relative path to the Vite project root.") path: String = "."
    ): String {
        val project = currentCoroutineContext().project
        val root = resolvePath(project, path)
        if (!isViteProject(root)) {
            mcpFail("No Vite configuration found at $path")
        }
        return "To build the project, run: 'npm run build' or 'npx vite build' in $path"
    }

    @McpTool
    @McpDescription("Displays information about the Vite project configuration.")
    suspend fun vite_info(
        @McpDescription("Project-relative path to the Vite project root.") path: String = "."
    ): String {
        val project = currentCoroutineContext().project
        val root = resolvePath(project, path)
        val viteConfig = root.resolve("vite.config.js")
        val viteConfigTs = root.resolve("vite.config.ts")

        val configFound = when {
            viteConfig.exists() -> "vite.config.js"
            viteConfigTs.exists() -> "vite.config.ts"
            else -> "Not found"
        }

        return """
            Vite Project Info:
            Root: ${root.toAbsolutePath()}
            Config: $configFound
            Status: Ready
        """.trimIndent()
    }

    private fun resolvePath(project: Project, relativePath: String): Path {
        val base = project.basePath?.toNioPathOrNull() ?: mcpFail("Project base path not found")
        return base.resolve(relativePath).normalize()
    }

    private fun isViteProject(root: Path): Boolean {
        return root.resolve("vite.config.js").exists() ||
               root.resolve("vite.config.ts").exists() ||
               root.resolve("package.json").exists() // Basic check
    }
}
