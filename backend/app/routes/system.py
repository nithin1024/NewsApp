from fastapi import APIRouter, HTTPException
from datetime import datetime
from app.schemas.system import HealthCheckResponse, SystemStatusResponse, HomeFeedResponse
from app.routes.news import get_news, get_breaking_news
from app.routes.markets import get_indian_markets, get_global_markets

router = APIRouter(tags=["System"])

@router.get("/health", response_model=HealthCheckResponse)
async def health_check():
    return HealthCheckResponse(status="ok")

@router.get("/api/v1/system/status", response_model=SystemStatusResponse)
async def get_system_status():
    return SystemStatusResponse(
        news_provider="UP",
        market_provider="UP",
        translation_engine="UP",
        ai_service="UP",
        database="UP"
    )

@router.get("/api/v1/home", response_model=HomeFeedResponse)
async def get_home_feed():
    try:
        indian = await get_indian_markets()
    except Exception as e:
        print(f"Error loading indian markets: {e}")
        indian = []

    try:
        globals_data = await get_global_markets()
    except Exception as e:
        print(f"Error loading global markets: {e}")
        globals_data = []

    try:
        breaking = await get_breaking_news()
    except Exception as e:
        print(f"Error loading breaking news: {e}")
        breaking = []

    try:
        latest = (await get_news(limit=10)).articles
    except Exception as e:
        print(f"Error loading latest news: {e}")
        latest = []

    categories = ["Stock Market", "Business", "Banking", "Economy", "Technology"]
    cat_feed = {}
    for cat in categories:
        try:
            cat_feed[cat] = (await get_news(category=cat, limit=5)).articles
        except Exception:
            cat_feed[cat] = []

    return HomeFeedResponse(
        indian_markets=indian,
        global_markets=globals_data,
        breaking_news=breaking,
        latest_news=latest,
        category_news=cat_feed
    )
