from sqlalchemy import Column, String, Text, DateTime, ForeignKey, Integer, Boolean
from datetime import datetime
from app.db.database import Base

class DeviceToken(Base):
    __tablename__ = "device_tokens"

    token = Column(String, primary_key=True)
    device_id = Column(String, nullable=True)
    platform = Column(String, default="android")
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow)

class UserPreference(Base):
    __tablename__ = "user_preferences"

    device_token = Column(String, ForeignKey("device_tokens.token"), primary_key=True)
    language = Column(String, default="telugu")  # telugu or english
    theme = Column(String, default="system")  # light, dark, system
    favorite_categories = Column(Text, default="Top Stories,Markets,Economy")  # comma separated
    favorite_companies = Column(Text, default="RELIANCE,INFY,TCS")
    favorite_sectors = Column(Text, default="Banking,IT")

    # Notification Toggles
    notify_positive = Column(Boolean, default=True)
    notify_negative = Column(Boolean, default=True)
    notify_neutral = Column(Boolean, default=True)
    notify_breaking = Column(Boolean, default=True)
    notify_watchlist = Column(Boolean, default=True)
    notify_market = Column(Boolean, default=True)
    notify_technical = Column(Boolean, default=True)
    notify_price = Column(Boolean, default=True)

    updated_at = Column(DateTime, default=datetime.utcnow)
