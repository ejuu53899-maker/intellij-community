package com.intellij.mcpserver.toolsets.general

import com.intellij.mcpserver.McpToolset
import com.intellij.mcpserver.annotations.McpDescription
import com.intellij.mcpserver.annotations.McpTool
import com.intellij.mcpserver.mcpFail
import com.intellij.mcpserver.settings.McpServerSettings
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Toolset for interacting with the GenX FX Trading System bridge.
 * Provides tools to monitor and control trading operations.
 */
internal class GenXToolset : McpToolset {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "http://localhost:8000"

    @McpTool
    @McpDescription("Fetches the current operational state of the GenX trading system.")
    suspend fun genx_status(): String {
        return try {
            val response: HttpResponse = client.get("$baseUrl/remote/status")
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "Error: Bridge returned ${response.status}"
            }
        } catch (e: Exception) {
            mcpFail("Failed to connect to GenX bridge at $baseUrl: ${e.message}")
        }
    }

    @McpTool
    @McpDescription("Sends remote control commands (START, STOP, PAUSE) to the GenX bridge.")
    suspend fun genx_command(
        @McpDescription("The command to send: START, STOP, or PAUSE") command: String
    ): String {
        val settings = McpServerSettings.getInstance()
        val apiKey = settings.genXApiKey ?: mcpFail("JULES_API_KEY_V4 is not configured in MCP settings.")
        val githubToken = settings.githubToken ?: mcpFail("GITHUB_TOKEN_PUSH is not configured in MCP settings.")

        return try {
            val response: HttpResponse = client.post("$baseUrl/remote/control") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                header("X-GitHub-Token", githubToken)
                contentType(ContentType.Application.Json)
                setBody(mapOf("command" to command.uppercase()))
            }
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                mcpFail("GenX bridge returned error ${response.status}: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            if (e is com.intellij.mcpserver.McpExpectedError) throw e
            mcpFail("Failed to send command to GenX bridge: ${e.message}")
        }
    }

    @McpTool
    @McpDescription("Retrieves real-time performance metrics and account summary from the live trading account.")
    suspend fun genx_performance(): String {
        val settings = McpServerSettings.getInstance()
        val apiKey = settings.genXApiKey ?: mcpFail("JULES_API_KEY_V4 is not configured.")
        val githubToken = settings.githubToken ?: mcpFail("GITHUB_TOKEN_PUSH is not configured.")

        return try {
            val response: HttpResponse = client.get("$baseUrl/remote/performance") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                header("X-GitHub-Token", githubToken)
            }
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "Error: Performance bridge returned ${response.status}. Ensure live account is connected."
            }
        } catch (e: Exception) {
            mcpFail("Failed to fetch performance data: ${e.message}")
        }
    }

    @McpTool
    @McpDescription("Pushes the current trading performance and logs to the remote repository using the configured GitHub token.")
    suspend fun genx_push_performance(): String {
        val settings = McpServerSettings.getInstance()
        val apiKey = settings.genXApiKey ?: mcpFail("JULES_API_KEY_V4 is not configured.")
        val githubToken = settings.githubToken ?: mcpFail("GITHUB_TOKEN_PUSH is not configured.")

        return try {
            val response: HttpResponse = client.post("$baseUrl/remote/push-performance") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                header("X-GitHub-Token", githubToken)
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                "Performance data successfully pushed to remote repository. 🚀"
            } else {
                mcpFail("Failed to push performance: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            mcpFail("Push operation failed: ${e.message}")
        }
    }
}
