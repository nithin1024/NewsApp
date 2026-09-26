from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class DeviceTokenRegister(BaseModel):
    token: str
    device_id: Optional[str] = None
    platform: str = "android"

class NotificationHistorySchema(BaseModel):
    id: int
    type: str  # POSITIVE, NEGATIVE, NEUTRAL, BREAKING, PRICE, TECHNICAL, MARKET, WATCHLIST
    title: str
    message: str
    article_id: Optional[str] = None
    stock_symbol: Optional[str] = None
    is_read: bool = False
    created_at: datetime

    class Config:
        from_attributes = True

class NotificationListResponse(BaseModel):
    notifications: List[NotificationHistorySchema]
