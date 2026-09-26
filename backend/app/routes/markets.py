from fastapi import APIRouter
from typing import List
from datetime import datetime
from app.services.stock_service import StockService
from app.schemas.market import MarketIndexSchema, MarketsOverviewResponse

router = APIRouter(prefix="/markets", tags=["Markets"])

@router.get("/india", response_model=List[MarketIndexSchema])
async def get_indian_markets():
    markets = await StockService.get_indian_markets()
    res = []
    for m in markets:
        res.append(MarketIndexSchema(
            symbol=m["symbol"],
            name=m["name"],
            region="INDIA",
            current_value=m["current_price"],
            change=m["change"],
            percent_change=m["percent_change"],
            market_status="OPEN",
            is_available=True,
            last_updated=m["last_updated"]
        ))
    return res

@router.get("/global", response_model=List[MarketIndexSchema])
async def get_global_markets():
    globals_data = StockService.get_global_markets()
    res = []
    for g in globals_data:
        res.append(MarketIndexSchema(
            symbol=g["symbol"],
            name=g["name"],
            region="GLOBAL",
            current_value=g["current_value"],
            change=g["change"],
            percent_change=g["percent_change"],
            market_status=g["market_status"],
            is_available=g["is_available"],
            last_updated=datetime.utcnow()
        ))
    return res

@router.get("/overview", response_model=MarketsOverviewResponse)
async def get_markets_overview():
    indian = await get_indian_markets()
    glob = await get_global_markets()
    return MarketsOverviewResponse(
        indian_markets=indian,
        global_markets=glob,
        last_updated=datetime.utcnow()
    )
