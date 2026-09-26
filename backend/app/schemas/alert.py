from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class PriceAlertCreate(BaseModel):
    device_token: str
    stock_symbol: str
    condition: str  # ABOVE, BELOW
    target_price: float

class TechnicalAlertCreate(BaseModel):
    device_token: str
    stock_symbol: str
    indicator: str  # RSI, SMA20, SMA50, SMA200, EMA20, MACD, VOLUME_SPIKE
    condition: str  # ABOVE, BELOW, CROSS_ABOVE, CROSS_BELOW
    value: float

class PriceAlertSchema(BaseModel):
    id: int
    stock_symbol: str
    condition: str
    target_price: float
    is_active: bool
    created_at: datetime

    class Config:
        from_attributes = True

class TechnicalAlertSchema(BaseModel):
    id: int
    stock_symbol: str
    indicator: str
    condition: str
    value: float
    is_active: bool
    created_at: datetime

    class Config:
        from_attributes = True

class AlertsResponse(BaseModel):
    price_alerts: List[PriceAlertSchema]
    technical_alerts: List[TechnicalAlertSchema]
