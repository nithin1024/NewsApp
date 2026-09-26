from fastapi import APIRouter, Query, HTTPException
from typing import List
from app.services.stock_service import StockService
from app.services.upstox_service import UpstoxService
from app.schemas.stock import StockQuoteSchema, StockHistoryResponse, CandlePoint, TechnicalIndicatorsResponse

router = APIRouter(prefix="/stocks", tags=["Stocks"])

@router.get("/search", response_model=List[StockQuoteSchema])
async def search_stocks(q: str = Query(..., min_length=1)):
    matches = StockService.search_stocks(q)
    symbols = [m["symbol"] for m in matches]
    if not symbols:
        return []
    quotes = await UpstoxService.fetch_market_quotes(symbols)
    res = []
    for q_data in quotes:
        res.append(StockQuoteSchema(
            symbol=q_data["symbol"],
            name=q_data["name"],
            exchange=q_data["exchange"],
            instrument_key=q_data.get("instrument_key"),
            current_price=q_data["current_price"],
            change=q_data["change"],
            percent_change=q_data["percent_change"],
            open_price=q_data["open_price"],
            high_price=q_data["high_price"],
            low_price=q_data["low_price"],
            prev_close=q_data["prev_close"],
            volume=q_data["volume"],
            fifty_two_week_high=q_data["fifty_two_week_high"],
            fifty_two_week_low=q_data["fifty_two_week_low"],
            sector=q_data.get("sector"),
            last_updated=q_data["last_updated"]
        ))
    return res

@router.get("/{symbol}", response_model=StockQuoteSchema)
async def get_stock_quote(symbol: str):
    q_data = await StockService.get_stock_details(symbol.upper())
    return StockQuoteSchema(
        symbol=q_data["symbol"],
        name=q_data["name"],
        exchange=q_data["exchange"],
        instrument_key=q_data.get("instrument_key"),
        current_price=q_data["current_price"],
        change=q_data["change"],
        percent_change=q_data["percent_change"],
        open_price=q_data["open_price"],
        high_price=q_data["high_price"],
        low_price=q_data["low_price"],
        prev_close=q_data["prev_close"],
        volume=q_data["volume"],
        fifty_two_week_high=q_data["fifty_two_week_high"],
        fifty_two_week_low=q_data["fifty_two_week_low"],
        sector=q_data.get("sector"),
        last_updated=q_data["last_updated"]
    )

@router.get("/{symbol}/history", response_model=StockHistoryResponse)
async def get_stock_history(symbol: str, period: str = Query("1M")):
    raw_candles = await UpstoxService.fetch_historical_candles(symbol.upper(), period=period)
    candle_points = [
        CandlePoint(
            timestamp=c["timestamp"],
            open=c["open"],
            high=c["high"],
            low=c["low"],
            close=c["close"],
            volume=c["volume"]
        ) for c in raw_candles
    ]
    return StockHistoryResponse(
        symbol=symbol.upper(),
        period=period,
        candles=candle_points
    )

@router.get("/{symbol}/indicators", response_model=TechnicalIndicatorsResponse)
async def get_technical_indicators(symbol: str):
    tech_data = await StockService.get_technical_analysis(symbol.upper())
    return TechnicalIndicatorsResponse(**tech_data)
