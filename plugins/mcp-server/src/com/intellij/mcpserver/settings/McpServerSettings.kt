package com.intellij.mcpserver.settings

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@Service
@State(name = "McpServerSettings", storages = [Storage("mcpServer.xml")])
internal class McpServerSettings : SimplePersistentStateComponent<McpServerSettings.MyState>(MyState()) {
  companion object {
    @JvmStatic
    fun getInstance(): McpServerSettings = service()

    const val DEFAULT_MCP_PORT: Int = 64342
    const val DEFAULT_MCP_PRIVATE_PORT: Int = DEFAULT_MCP_PORT + 100

    private val GENX_API_KEY_ATTRIBUTES = CredentialAttributes(generateServiceName("MCP Server", "GenX API Key"))
    private val GITHUB_TOKEN_ATTRIBUTES = CredentialAttributes(generateServiceName("MCP Server", "GitHub Token"))
  }

  var genXApiKey: String?
    get() = PasswordSafe.instance.getPassword(GENX_API_KEY_ATTRIBUTES)
    set(value) {
      PasswordSafe.instance.set(GENX_API_KEY_ATTRIBUTES, Credentials(null, value))
    }

  var githubToken: String?
    get() = PasswordSafe.instance.getPassword(GITHUB_TOKEN_ATTRIBUTES)
    set(value) {
      PasswordSafe.instance.set(GITHUB_TOKEN_ATTRIBUTES, Credentials(null, value))
    }

  override fun loadState(state: MyState) {
    super.loadState(state)

  }

  internal class MyState : BaseState() {
    var enableBraveMode: Boolean by property(false)
    var enableMcpServer: Boolean by property(false)
    var mcpServerPort: Int by property(DEFAULT_MCP_PORT)
  }
}