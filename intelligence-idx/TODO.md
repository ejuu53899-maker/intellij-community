# Intelligence IDX: Follow-up TODO Plan

This document outlines future improvements and maintenance tasks for the Intelligence IDX and MCP Server integrations.

## MCP Server Enhancements
- [ ] **Configurable Bridge Ports:** Allow users to configure the GenX and MQL5 bridge ports (default 8000/8001) in the MCP settings UI.
- [ ] **Automated Bridge Management:** Implement logic to automatically detect or start the bridge servers if they are not running.
- [ ] **Credential Validation:** Add a 'Test Connection' button in settings to verify API keys and bridge connectivity.
- [ ] **Streaming Output for Terminal Tools:** Improve `WebTechToolset` to stream terminal output in real-time to the LLM instead of waiting for process completion.

## Infrastructure Improvements
- [ ] **Enhanced `production.sh`:** Add platform-specific build targets (macOS, Windows, Linux) and artifact signing.
- [ ] **Bolt.new Real-time Sync:** Implement actual file-watchers in `bolt-setup.sh` to synchronize changes with the Bolt.new platform instantly.
- [ ] **Prisma UI Integration:** Add a tool to open the Prisma Studio in the IDE's built-in browser.

## Documentation
- [ ] **Tutorial Videos:** Create brief walkthroughs for setting up the GenX bridge and using the `intelligence-idx` scripts.
- [ ] **API Reference:** Generate a formal API reference for the new MCP toolsets.

---
*Maintained by Jules*
