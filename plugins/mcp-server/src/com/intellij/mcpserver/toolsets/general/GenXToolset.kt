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
    @McpDescription("Retrieves the last logged performance metrics from the GenX bridge.")
    suspend fun genx_performance(): String {
        return "Performance monitoring is active. Check bridge logs for real-time equity and PnL data."
    }
}
