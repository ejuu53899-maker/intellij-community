from fastapi import FastAPI
from api.routers import remote

app = FastAPI(title="GenX All-in-One Backend")

app.include_router(remote.router)

@app.get("/")
async def root():
    return {"message": "GenX Backend is running"}
