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
 * Toolset for MQL5-Google-Onedrive synchronization.
 * Provides tools to monitor and manage cloud synchronization of trading assets.
 */
internal class Mql5OnedriveToolset : McpToolset {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "http://localhost:8001"

    @McpTool
    @McpDescription("Checks the current synchronization status between MQL5 and cloud storage (Google Drive/OneDrive).")
    suspend fun mql5_sync_status(): String {
        return try {
            val response: HttpResponse = client.get("$baseUrl/sync/status")
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "Error: Sync bridge returned ${response.status}"
            }
        } catch (e: Exception) {
            mcpFail("Failed to connect to MQL5 sync bridge at $baseUrl: ${e.message}")
        }
    }

    @McpTool
    @McpDescription("Triggers a manual synchronization of MQL5 assets to cloud storage.")
    suspend fun mql5_sync_trigger(): String {
        val settings = McpServerSettings.getInstance()
        val apiKey = settings.genXApiKey ?: mcpFail("API Key is not configured in MCP settings.")

        return try {
            val response: HttpResponse = client.post("$baseUrl/sync/trigger") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                "Synchronization triggered successfully."
            } else {
                mcpFail("MQL5 sync bridge returned error ${response.status}: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            if (e is com.intellij.mcpserver.McpExpectedError) throw e
            mcpFail("Failed to trigger MQL5 sync: ${e.message}")
        }
    }

    @McpTool
    @McpDescription("Retrieves the last sync logs for MQL5 assets.")
    suspend fun mql5_sync_logs(): String {
        return try {
            val response: HttpResponse = client.get("$baseUrl/sync/logs")
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "Error: Unable to fetch logs (${response.status})"
            }
        } catch (e: Exception) {
            mcpFail("Failed to fetch MQL5 sync logs: ${e.message}")
        }
    }
}
