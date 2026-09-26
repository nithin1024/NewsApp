from sqlalchemy import Column, String, Text, DateTime, ForeignKey, Integer, Boolean
from datetime import datetime
from app.db.database import Base

class NotificationHistory(Base):
    __tablename__ = "notification_history"

    id = Column(Integer, primary_key=True, autoincrement=True)
    device_token = Column(String, nullable=True, index=True)
    type = Column(String, nullable=False)  # POSITIVE, NEGATIVE, NEUTRAL, BREAKING, PRICE, TECHNICAL, MARKET, WATCHLIST
    title = Column(String, nullable=False)
    message = Column(Text, nullable=False)
    article_id = Column(String, nullable=True)
    stock_symbol = Column(String, nullable=True)
    is_read = Column(Boolean, default=False)
    created_at = Column(DateTime, default=datetime.utcnow, index=True)
