from fastapi import APIRouter, Header, HTTPException
from pydantic import BaseModel
import logging

router = APIRouter(prefix="/remote", tags=["remote"])
logger = logging.getLogger(__name__)

class ControlCommand(BaseModel):
    command: str

@router.get("/status")
async def get_remote_status():
    return {
        "status": "online",
        "mode": "automated",
        "bridge_version": "1.0.0",
        "trading_system": "GenX A6-9V"
    }

@router.post("/control")
async def post_remote_control(
    command: ControlCommand,
    authorization: str = Header(None),
    x_github_token: str = Header(None)
):
    cmd = command.command.upper()
    logger.info(f"Remote command received: {cmd}")
    return {
        "status": "success",
        "command": cmd,
        "message": f"Trading system {cmd.lower()}ed successfully"
    }
