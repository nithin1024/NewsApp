from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class MarketIndexSchema(BaseModel):
    symbol: str
    name: str
    region: str = "INDIA"
    current_value: float
    change: float
    percent_change: float
    market_status: str = "CLOSED"
    is_available: bool = True
    last_updated: datetime

    class Config:
        from_attributes = True

class MarketsOverviewResponse(BaseModel):
    indian_markets: List[MarketIndexSchema]
    global_markets: List[MarketIndexSchema]
    last_updated: datetime
