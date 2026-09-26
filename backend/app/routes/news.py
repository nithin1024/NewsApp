from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from typing import Optional, List
from app.db.database import get_db
from app.services.newsdata_service import NewsDataService
from app.services.translation_service import TranslationService
from app.services.sentiment_service import SentimentService
from app.schemas.news import NewsListResponse, NewsArticleSchema, NewsSentimentSchema

router = APIRouter(prefix="/news", tags=["News"])

@router.get("", response_model=NewsListResponse)
async def get_news(
    category: Optional[str] = Query(None),
    query: Optional[str] = Query(None),
    limit: int = Query(20, ge=1, le=100)
):
    raw_articles = await NewsDataService.fetch_latest_news(category=category, query=query)

    formatted_articles = []
    for art in raw_articles[:limit]:
        tel_title, tel_desc, key_facts, why_matters, m_impact, m_reason = await TranslationService.translate_english_to_telugu(
            art["title"], art["description"]
        )
        s_data = await SentimentService.analyze_article_sentiment(art["title"], art["description"])

        formatted_articles.append(NewsArticleSchema(
            id=art["id"],
            title=art["title"],
            description=art["description"],
            content=art["content"],
            source_name=art["source_name"],
            source_url=art["source_url"],
            image_url=art["image_url"],
            category=art["category"],
            published_at=art["published_at"],
            is_breaking=art["is_breaking"],
            telugu_title=tel_title,
            telugu_description=tel_desc,
            telugu_summary=tel_desc,
            key_facts=key_facts,
            why_it_matters=why_matters,
            market_impact=m_impact,
            market_impact_reason=m_reason,
            sentiment=NewsSentimentSchema(
                sentiment=s_data["sentiment"],
                score=s_data["score"],
                market_relevance=s_data["market_relevance"],
                related_company=s_data["related_company"],
                related_symbol=s_data["related_symbol"],
                related_sector=s_data["related_sector"]
            )
        ))

    return NewsListResponse(
        articles=formatted_articles,
        total=len(formatted_articles),
        category=category
    )

@router.get("/breaking", response_model=List[NewsArticleSchema])
async def get_breaking_news():
    raw_articles = await NewsDataService.fetch_latest_news(category="STOCK MARKET")
    breaking = [a for a in raw_articles if a.get("is_breaking")]
    if not breaking:
        breaking = raw_articles[:3]

    res = []
    for art in breaking:
        tel_title, tel_desc, key_facts, why_matters, m_impact, m_reason = await TranslationService.translate_english_to_telugu(
            art["title"], art["description"]
        )
        s_data = await SentimentService.analyze_article_sentiment(art["title"], art["description"])
        res.append(NewsArticleSchema(
            id=art["id"],
            title=art["title"],
            description=art["description"],
            content=art["content"],
            source_name=art["source_name"],
            source_url=art["source_url"],
            image_url=art["image_url"],
            category=art["category"],
            published_at=art["published_at"],
            is_breaking=True,
            telugu_title=tel_title,
            telugu_description=tel_desc,
            telugu_summary=tel_desc,
            key_facts=key_facts,
            why_it_matters=why_matters,
            market_impact=m_impact,
            market_impact_reason=m_reason,
            sentiment=NewsSentimentSchema(
                sentiment=s_data["sentiment"],
                score=s_data["score"],
                market_relevance=s_data["market_relevance"],
                related_company=s_data["related_company"],
                related_symbol=s_data["related_symbol"],
                related_sector=s_data["related_sector"]
            )
        ))
    return res

@router.get("/category/{category_name}", response_model=NewsListResponse)
async def get_news_by_category(category_name: str, limit: int = Query(20)):
    return await get_news(category=category_name, limit=limit)

@router.get("/search", response_model=NewsListResponse)
async def search_news(q: str = Query(..., min_length=1)):
    return await get_news(query=q)

@router.get("/{article_id}", response_model=NewsArticleSchema)
async def get_article_details(article_id: str):
    raw_articles = await NewsDataService.fetch_latest_news()
    found = next((a for a in raw_articles if a["id"] == article_id), None)
    if not found:
        if raw_articles:
            found = raw_articles[0]
        else:
            raise HTTPException(status_code=404, detail="Article not found")

    tel_title, tel_desc, key_facts, why_matters, m_impact, m_reason = await TranslationService.translate_english_to_telugu(
        found["title"], found["description"]
    )
    s_data = await SentimentService.analyze_article_sentiment(found["title"], found["description"])

    return NewsArticleSchema(
        id=found["id"],
        title=found["title"],
        description=found["description"],
        content=found["content"],
        source_name=found["source_name"],
        source_url=found["source_url"],
        image_url=found["image_url"],
        category=found["category"],
        published_at=found["published_at"],
        is_breaking=found["is_breaking"],
        telugu_title=tel_title,
        telugu_description=tel_desc,
        telugu_summary=tel_desc,
        key_facts=key_facts,
        why_it_matters=why_matters,
        market_impact=m_impact,
        market_impact_reason=m_reason,
        sentiment=NewsSentimentSchema(
            sentiment=s_data["sentiment"],
            score=s_data["score"],
            market_relevance=s_data["market_relevance"],
            related_company=s_data["related_company"],
            related_symbol=s_data["related_symbol"],
            related_sector=s_data["related_sector"]
        )
    )
