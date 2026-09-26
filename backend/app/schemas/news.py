from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class NewsSentimentSchema(BaseModel):
    sentiment: str = "NEUTRAL"
    score: float = 0.0
    market_relevance: str = "HIGH"
    related_company: Optional[str] = None
    related_symbol: Optional[str] = None
    related_sector: Optional[str] = None

class NewsArticleSchema(BaseModel):
    id: str
    title: str
    description: Optional[str] = None
    content: Optional[str] = None
    source_name: str
    source_url: str
    image_url: Optional[str] = None
    category: str  # STOCK MARKET, GLOBAL NEWS, or BUSINESS
    published_at: datetime
    is_breaking: bool = False

    # Telugu & Financial Analysis fields
    telugu_title: Optional[str] = None
    telugu_description: Optional[str] = None
    telugu_summary: Optional[str] = None
    key_facts: List[str] = []
    why_it_matters: Optional[str] = None
    market_impact: Optional[str] = "Positive"
    market_impact_reason: Optional[str] = None
    sentiment: Optional[NewsSentimentSchema] = None

    class Config:
        from_attributes = True

class NewsListResponse(BaseModel):
    articles: List[NewsArticleSchema]
    total: int
    category: Optional[str] = None
