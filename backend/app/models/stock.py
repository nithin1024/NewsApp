from sqlalchemy import Column, String, Float, DateTime, BigInteger, Boolean
from datetime import datetime
from app.db.database import Base

class Stock(Base):
    __tablename__ = "stocks"

    symbol = Column(String, primary_key=True, index=True)
    name = Column(String, nullable=False)
    exchange = Column(String, default="NSE")
    instrument_key = Column(String, nullable=True, index=True)
    current_price = Column(Float, nullable=False, default=0.0)
    change = Column(Float, default=0.0)
    percent_change = Column(Float, default=0.0)
    open_price = Column(Float, default=0.0)
    high_price = Column(Float, default=0.0)
    low_price = Column(Float, default=0.0)
    prev_close = Column(Float, default=0.0)
    volume = Column(BigInteger, default=0)
    fifty_two_week_high = Column(Float, default=0.0)
    fifty_two_week_low = Column(Float, default=0.0)
    sector = Column(String, nullable=True)
    last_updated = Column(DateTime, default=datetime.utcnow)

class MarketIndex(Base):
    __tablename__ = "market_indices"

    symbol = Column(String, primary_key=True)  # NIFTY 50, SENSEX, BANK NIFTY, INDIA VIX, S&P 500, etc.
    name = Column(String, nullable=False)
    region = Column(String, default="INDIA")  # INDIA or GLOBAL
    current_value = Column(Float, nullable=False, default=0.0)
    change = Column(Float, default=0.0)
    percent_change = Column(Float, default=0.0)
    market_status = Column(String, default="CLOSED")  # OPEN, CLOSED, PRE-OPEN
    is_available = Column(Boolean, default=True)
    last_updated = Column(DateTime, default=datetime.utcnow)
