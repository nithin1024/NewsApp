from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from app.core.config import settings
from app.db.database import init_db
from app.routes import news, markets, stocks, watchlist, alerts, notifications, system

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup actions
    print("Initializing NewsTelugu Backend Database...")
    try:
        await init_db()
    except Exception as e:
        print(f"Database initialization info: {e}")
    print("NewsTelugu Backend Started Successfully.")
    yield
    # Shutdown actions
    print("NewsTelugu Backend Shutting Down.")

app = FastAPI(
    title=settings.APP_NAME,
    openapi_url=f"{settings.API_V1_STR}/openapi.json",
    lifespan=lifespan
)

# Enable CORS for Android client requests
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register System / Health endpoints
app.include_router(system.router)

# Register V1 API Routes
app.include_router(news.router, prefix=settings.API_V1_STR)
app.include_router(markets.router, prefix=settings.API_V1_STR)
app.include_router(stocks.router, prefix=settings.API_V1_STR)
app.include_router(watchlist.router, prefix=settings.API_V1_STR)
app.include_router(alerts.router, prefix=settings.API_V1_STR)
app.include_router(notifications.router, prefix=settings.API_V1_STR)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
