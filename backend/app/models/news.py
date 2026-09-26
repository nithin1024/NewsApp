from sqlalchemy import Column, String, Text, Float, DateTime, ForeignKey, Integer, Boolean
from sqlalchemy.orm import relationship
from datetime import datetime
from app.db.database import Base

class NewsArticle(Base):
    __tablename__ = "news_articles"

    id = Column(String, primary_key=True)  # Hash or provider id
    provider_id = Column(String, index=True, nullable=True)
    title = Column(Text, nullable=False)
    description = Column(Text, nullable=True)
    content = Column(Text, nullable=True)
    source_name = Column(String, nullable=False, default="NewsSource")
    source_url = Column(String, nullable=False)
    image_url = Column(String, nullable=True)
    category = Column(String, index=True, default="General")
    published_at = Column(DateTime, default=datetime.utcnow, index=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    is_breaking = Column(Boolean, default=False)

    # Relationships
    translations = relationship("NewsTranslation", back_populates="article", cascade="all, delete-orphan")
    sentiment = relationship("NewsSentiment", back_populates="article", uselist=False, cascade="all, delete-orphan")

class NewsTranslation(Base):
    __tablename__ = "news_translations"

    id = Column(Integer, primary_key=True, autoincrement=True)
    article_id = Column(String, ForeignKey("news_articles.id"), nullable=False, index=True)
    language = Column(String, default="tel_Telu", nullable=False)
    translated_title = Column(Text, nullable=False)
    translated_description = Column(Text, nullable=True)
    translated_summary = Column(Text, nullable=True)
    translation_status = Column(String, default="SUCCESS")  # SUCCESS, PENDING, FAILED
    created_at = Column(DateTime, default=datetime.utcnow)

    article = relationship("NewsArticle", back_populates="translations")

class NewsSentiment(Base):
    __tablename__ = "news_sentiments"

    article_id = Column(String, ForeignKey("news_articles.id"), primary_key=True)
    sentiment = Column(String, nullable=False, default="NEUTRAL")  # POSITIVE, NEGATIVE, NEUTRAL
    score = Column(Float, default=0.0)  # -1.0 to 1.0
    market_relevance = Column(String, default="MEDIUM")  # HIGH, MEDIUM, LOW
    related_company = Column(String, nullable=True)
    related_symbol = Column(String, nullable=True)
    related_sector = Column(String, nullable=True)
    analyzed_at = Column(DateTime, default=datetime.utcnow)

    article = relationship("NewsArticle", back_populates="sentiment")
