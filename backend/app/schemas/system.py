from pydantic import BaseModel
from typing import Dict, List
from app.schemas.news import NewsArticleSchema
from app.schemas.market import MarketIndexSchema

class HealthCheckResponse(BaseModel):
    status: str = "ok"

class SystemStatusResponse(BaseModel):
    news_provider: str = "UP"  # UP / DOWN
    market_provider: str = "UP"
    translation_engine: str = "UP"
    ai_service: str = "UP"
    database: str = "UP"

class HomeFeedResponse(BaseModel):
    indian_markets: List[MarketIndexSchema]
    global_markets: List[MarketIndexSchema]
    breaking_news: List[NewsArticleSchema]
    latest_news: List[NewsArticleSchema]
    category_news: Dict[str, List[NewsArticleSchema]]
