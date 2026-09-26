import json
from typing import Dict, Any, Optional
from openai import AsyncOpenAI
from app.core.config import settings

class SentimentService:
    @classmethod
    async def analyze_article_sentiment(cls, title: str, description: str = "") -> Dict[str, Any]:
        api_key = settings.OPENAI_API_KEY

        if api_key:
            try:
                client = AsyncOpenAI(api_key=api_key)
                prompt = f"""
Analyze the following news headline and content for Indian stock market & financial sentiment:
Title: {title}
Description: {description}

Return JSON with:
1. sentiment: "POSITIVE", "NEGATIVE", or "NEUTRAL"
2. score: float between -1.0 (most negative) and +1.0 (most positive)
3. market_relevance: "HIGH", "MEDIUM", or "LOW"
4. related_company: name of company if mentioned, else null
5. related_symbol: NSE symbol if applicable (e.g. RELIANCE, INFY, HDFCBANK), else null
6. related_sector: sector name (e.g. Banking, IT, Energy, Auto), else null
"""
                response = await client.chat.completions.create(
                    model="gpt-3.5-turbo",
                    messages=[
                        {"role": "system", "content": "You are a financial news analyst. Respond strictly in valid JSON format."},
                        {"role": "user", "content": prompt}
                    ],
                    temperature=0.1,
                    response_format={"type": "json_object"}
                )

                res_content = response.choices[0].message.content
                data = json.loads(res_content)
                return {
                    "sentiment": data.get("sentiment", "NEUTRAL").upper(),
                    "score": float(data.get("score", 0.0)),
                    "market_relevance": data.get("market_relevance", "MEDIUM").upper(),
                    "related_company": data.get("related_company"),
                    "related_symbol": data.get("related_symbol"),
                    "related_sector": data.get("related_sector")
                }
            except Exception as e:
                print(f"Error calling OpenAI sentiment API: {e}")

        # Deterministic context-aware sentiment rules fallback
        return cls.rule_based_sentiment(title, description)

    @classmethod
    def rule_based_sentiment(cls, title: str, description: str) -> Dict[str, Any]:
        text = f"{title} {description}".lower()

        pos_words = ["rise", "rises", "rose", "rally", "gain", "gains", "jump", "record high", "profit", "surge", "strong", "higher", "positive", "growth"]
        neg_words = ["fall", "falls", "fell", "drop", "drops", "plunge", "loss", "decline", "lower", "negative", "weak", "down", "crash"]

        pos_score = sum(1 for w in pos_words if w in text)
        neg_score = sum(1 for w in neg_words if w in text)

        if pos_score > neg_score:
            sent = "POSITIVE"
            score = min(0.3 + (pos_score * 0.2), 0.95)
        elif neg_score > pos_score:
            sent = "NEGATIVE"
            score = max(-0.3 - (neg_score * 0.2), -0.95)
        else:
            sent = "NEUTRAL"
            score = 0.0

        # Company / Symbol / Sector detection
        comp, sym, sector = None, None, None
        if "infosys" in text or "infy" in text:
            comp, sym, sector = "Infosys Limited", "INFY", "IT"
        elif "reliance" in text or "ril" in text:
            comp, sym, sector = "Reliance Industries", "RELIANCE", "Energy"
        elif "hdfc" in text:
            comp, sym, sector = "HDFC Bank", "HDFCBANK", "Banking"
        elif "icici" in text:
            comp, sym, sector = "ICICI Bank", "ICICIBANK", "Banking"
        elif "bank" in text or "rbi" in text:
            sector = "Banking"
        elif "nifty" in text or "sensex" in text or "stock" in text:
            sector = "Stock Market"

        relevance = "HIGH" if (sym or "nifty" in text or "rbi" in text) else "MEDIUM"

        return {
            "sentiment": sent,
            "score": round(score, 2),
            "market_relevance": relevance,
            "related_company": comp,
            "related_symbol": sym,
            "related_sector": sector
        }
