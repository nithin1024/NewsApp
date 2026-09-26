from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from datetime import datetime
from app.db.database import get_db
from app.schemas.notification import DeviceTokenRegister, NotificationListResponse, NotificationHistorySchema

router = APIRouter(prefix="/notifications", tags=["Notifications"])

DEVICE_TOKENS = set()

SAMPLE_NOTIFICATIONS = [
    {
        "id": 1,
        "type": "POSITIVE",
        "title": "Infosys Reports Strong AI Revenue Growth",
        "message": "ఇన్ఫోసిస్ ఏఐ విభాగం ఆదాయంలో గణనీయమైన వృద్ధిని నమోదు చేసింది.",
        "article_id": "art_1",
        "stock_symbol": "INFY",
        "is_read": False,
        "created_at": datetime.utcnow()
    },
    {
        "id": 2,
        "type": "BREAKING",
        "title": "NIFTY 50 Touches Record High",
        "message": "నిఫ్టీ 50 బెంచ్ మార్క్ ఇండెక్స్ ఆల్ టైమ్ గరిష్ట స్థాయిని తాకింది.",
        "article_id": "art_2",
        "stock_symbol": "NIFTY 50",
        "is_read": True,
        "created_at": datetime.utcnow()
    },
    {
        "id": 3,
        "type": "TECHNICAL",
        "title": "RELIANCE RSI Crosses 60",
        "message": "రిలయన్స్ ఇండస్ట్రీస్ RSI సూచిక 60ని దాటి అనుకూల ధోరణిని సూచిస్తుంది.",
        "article_id": None,
        "stock_symbol": "RELIANCE",
        "is_read": False,
        "created_at": datetime.utcnow()
    }
]

@router.post("/device-token")
async def register_device_token(req: DeviceTokenRegister):
    DEVICE_TOKENS.add(req.token)
    return {"status": "registered", "token": req.token}

@router.get("", response_model=NotificationListResponse)
async def get_notifications():
    items = [NotificationHistorySchema(**n) for n in SAMPLE_NOTIFICATIONS]
    return NotificationListResponse(notifications=items)
