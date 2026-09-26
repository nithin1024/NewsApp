from sqlalchemy import Column, String, Float, DateTime, ForeignKey, Integer, Boolean
from datetime import datetime
from app.db.database import Base

class Watchlist(Base):
    __tablename__ = "watchlists"

    id = Column(Integer, primary_key=True, autoincrement=True)
    device_token = Column(String, ForeignKey("device_tokens.token"), nullable=False, index=True)
    stock_symbol = Column(String, ForeignKey("stocks.symbol"), nullable=False)
    added_at = Column(DateTime, default=datetime.utcnow)

class PriceAlert(Base):
    __tablename__ = "price_alerts"

    id = Column(Integer, primary_key=True, autoincrement=True)
    device_token = Column(String, ForeignKey("device_tokens.token"), nullable=False, index=True)
    stock_symbol = Column(String, nullable=False, index=True)
    condition = Column(String, nullable=False)  # ABOVE or BELOW
    target_price = Column(Float, nullable=False)
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    last_triggered_at = Column(DateTime, nullable=True)

class TechnicalAlert(Base):
    __tablename__ = "technical_alerts"

    id = Column(Integer, primary_key=True, autoincrement=True)
    device_token = Column(String, ForeignKey("device_tokens.token"), nullable=False, index=True)
    stock_symbol = Column(String, nullable=False, index=True)
    indicator = Column(String, nullable=False)  # RSI, SMA20, SMA50, SMA200, EMA20, MACD, VOLUME_SPIKE
    condition = Column(String, nullable=False)  # ABOVE, BELOW, CROSS_ABOVE, CROSS_BELOW
    value = Column(Float, nullable=False)
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    last_triggered_at = Column(DateTime, nullable=True)
