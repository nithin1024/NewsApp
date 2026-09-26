from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime
from app.schemas.stock import StockQuoteSchema

class WatchlistAddRequest(BaseModel):
    stock_symbol: str
    device_token: str

class WatchlistResponse(BaseModel):
    items: List[StockQuoteSchema]
