# IntelliJ Community & GenX FX Integration

This directory contains integration assets to connect the [GenX FX Trading System](https://github.com/ejuu53899-maker/A6-9V2) with the IntelliJ Community MCP server.

## Features

- **Real-time Monitoring**: Use `genx_status` to check the bridge state.
- **Remote Control**: Use `genx_command` to START, STOP, or PAUSE the EA from your IDE.
- **Secure Integration**: Support for `JULES_API_KEY_V4` and `GITHUB_TOKEN_PUSH` authentication.

## Setup Instructions

1.  **Configure IntelliJ MCP Server**:
    - Open IntelliJ Settings -> Tools -> MCP Server.
    - Enable the MCP Server.
    - In the "GenX FX Trading System Integration" group, enter your `JULES_API_KEY_V4` and `GITHUB_TOKEN_PUSH`.

2.  **Project Configuration**:
    - Ensure your `A6-9V2` project is open in IntelliJ.
    - The `GenXClient` will automatically detect the project and suggest configuration if it's not already set up.

3.  **Manual MCP Config**:
    - If you prefer manual setup, create a `.mcp.json` file in your project root with the following content:

```json
{
  "mcpServers": {
    "genx-bridge": {
      "command": "python3",
      "args": ["GenX_FX_V4/bridge.py"],
      "env": {
        "JULES_API_KEY_V4": "your_api_key_here",
        "GITHUB_TOKEN_PUSH": "your_github_token_here"
      }
    }
  }
}
```

## Using Tools

Once connected, you can use the following tools through any MCP-compatible AI client (like Jules or Claude Code):

- `genx_status()`: Returns the current status of the bridge.
- `genx_command(command="START")`: Starts the trading operations.
- `genx_performance()`: Placeholder for retrieving performance data.

---
*Integrated by Jules*
