from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class NewsSentimentSchema(BaseModel):
    sentiment: str = "NEUTRAL"
    score: float = 0.0
    market_relevance: str = "MEDIUM"
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
    category: str
    published_at: datetime
    is_breaking: bool = False

    # Translation & Sentiment
    telugu_title: Optional[str] = None
    telugu_description: Optional[str] = None
    telugu_summary: Optional[str] = None
    sentiment: Optional[NewsSentimentSchema] = None

    class Config:
        from_attributes = True

class NewsListResponse(BaseModel):
    articles: List[NewsArticleSchema]
    total: int
    category: Optional[str] = None
