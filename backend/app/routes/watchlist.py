from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from typing import List
from app.db.database import get_db
from app.services.stock_service import StockService
from app.schemas.stock import StockQuoteSchema
from app.schemas.watchlist import WatchlistAddRequest, WatchlistResponse

router = APIRouter(prefix="/watchlist", tags=["Watchlist"])

# In-memory session watchlist fallback
SESSION_WATCHLIST = ["RELIANCE", "INFY", "TCS", "HDFCBANK"]

@router.get("", response_model=WatchlistResponse)
async def get_watchlist():
    quotes = []
    for sym in SESSION_WATCHLIST:
        q = await StockService.get_stock_details(sym)
        quotes.append(StockQuoteSchema(**q))
    return WatchlistResponse(items=quotes)

@router.post("", response_model=WatchlistResponse)
async def add_to_watchlist(req: WatchlistAddRequest):
    sym = req.stock_symbol.upper()
    if sym not in SESSION_WATCHLIST:
        SESSION_WATCHLIST.append(sym)
    return await get_watchlist()

@router.delete("/{symbol}", response_model=WatchlistResponse)
async def remove_from_watchlist(symbol: str):
    sym = symbol.upper()
    if sym in SESSION_WATCHLIST:
        SESSION_WATCHLIST.remove(sym)
    return await get_watchlist()
