from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class StockQuoteSchema(BaseModel):
    symbol: str
    name: str
    exchange: str = "NSE"
    instrument_key: Optional[str] = None
    current_price: float
    change: float
    percent_change: float
    open_price: float
    high_price: float
    low_price: float
    prev_close: float
    volume: int
    fifty_two_week_high: float
    fifty_two_week_low: float
    sector: Optional[str] = None
    last_updated: datetime

    class Config:
        from_attributes = True

class CandlePoint(BaseModel):
    timestamp: str
    open: float
    high: float
    low: float
    close: float
    volume: int

class StockHistoryResponse(BaseModel):
    symbol: str
    period: str  # 1D, 1W, 1M, 3M, 6M, 1Y, 5Y
    candles: List[CandlePoint]

class IndicatorDetail(BaseModel):
    name: str
    value: float
    status: str  # Bullish, Bearish, Neutral, Overbought, Oversold
    description: str

class TechnicalIndicatorsResponse(BaseModel):
    symbol: str
    sma_20: IndicatorDetail
    sma_50: IndicatorDetail
    sma_200: IndicatorDetail
    ema_20: IndicatorDetail
    ema_50: IndicatorDetail
    rsi_14: IndicatorDetail
    macd: IndicatorDetail
    bollinger_bands: IndicatorDetail
    atr: IndicatorDetail
    obv: IndicatorDetail
    summary: str  # Neutral informational summary
