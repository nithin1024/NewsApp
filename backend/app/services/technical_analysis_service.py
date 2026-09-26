import numpy as np
import pandas as pd
from typing import List, Dict, Any

class TechnicalAnalysisService:
    @classmethod
    def calculate_indicators(cls, symbol: str, candles: List[Dict[str, Any]]) -> Dict[str, Any]:
        if not candles or len(candles) < 14:
            # Provide safe default indicator structure if candles are sparse
            return cls.get_default_indicators(symbol)

        df = pd.DataFrame(candles)
        closes = df['close'].astype(float)
        highs = df['high'].astype(float)
        lows = df['low'].astype(float)
        volumes = df['volume'].astype(float)

        latest_close = closes.iloc[-1]

        # SMA
        sma_20 = float(closes.rolling(window=20, min_periods=1).mean().iloc[-1])
        sma_50 = float(closes.rolling(window=50, min_periods=1).mean().iloc[-1])
        sma_200 = float(closes.rolling(window=200, min_periods=1).mean().iloc[-1])

        # EMA
        ema_20 = float(closes.ewm(span=20, adjust=False).mean().iloc[-1])
        ema_50 = float(closes.ewm(span=50, adjust=False).mean().iloc[-1])

        # RSI 14
        delta = closes.diff()
        gain = (delta.where(delta > 0, 0)).rolling(window=14, min_periods=1).mean()
        loss = (-delta.where(delta < 0, 0)).rolling(window=14, min_periods=1).mean()
        rs = gain / (loss.replace(0, 0.00001))
        rsi_14 = float(100 - (100 / (1 + rs)).iloc[-1])

        # MACD (12, 26)
        ema12 = closes.ewm(span=12, adjust=False).mean()
        ema26 = closes.ewm(span=26, adjust=False).mean()
        macd_line = float((ema12 - ema26).iloc[-1])

        # Bollinger Bands (20, 2)
        bb_middle = sma_20
        bb_std = float(closes.rolling(window=20, min_periods=1).std().iloc[-1])
        bb_upper = bb_middle + (2 * bb_std)
        bb_lower = bb_middle - (2 * bb_std)

        # ATR 14
        tr1 = highs - lows
        tr2 = abs(highs - closes.shift(1))
        tr3 = abs(lows - closes.shift(1))
        tr = pd.concat([tr1, tr2, tr3], axis=1).max(axis=1)
        atr_14 = float(tr.rolling(window=14, min_periods=1).mean().iloc[-1])

        # OBV
        obv_series = (np.sign(closes.diff()) * volumes).fillna(0).cumsum()
        obv_val = float(obv_series.iloc[-1])

        # Status evaluation helper
        rsi_status = "Neutral"
        if rsi_14 >= 70:
            rsi_status = "Overbought"
        elif rsi_14 <= 30:
            rsi_status = "Oversold"

        sma20_status = "Trading above SMA20" if latest_close >= sma_20 else "Trading below SMA20"

        return {
            "symbol": symbol,
            "sma_20": {
                "name": "SMA (20)",
                "value": round(sma_20, 2),
                "status": sma20_status,
                "description": f"20-period Simple Moving Average is {round(sma_20, 2)}"
            },
            "sma_50": {
                "name": "SMA (50)",
                "value": round(sma_50, 2),
                "status": "Trading above SMA50" if latest_close >= sma_50 else "Trading below SMA50",
                "description": f"50-period Simple Moving Average is {round(sma_50, 2)}"
            },
            "sma_200": {
                "name": "SMA (200)",
                "value": round(sma_200, 2),
                "status": "Trading above SMA200" if latest_close >= sma_200 else "Trading below SMA200",
                "description": f"Long-term 200-period Simple Moving Average is {round(sma_200, 2)}"
            },
            "ema_20": {
                "name": "EMA (20)",
                "value": round(ema_20, 2),
                "status": "Short-term Momentum",
                "description": f"20-period Exponential Moving Average is {round(ema_20, 2)}"
            },
            "ema_50": {
                "name": "EMA (50)",
                "value": round(ema_50, 2),
                "status": "Medium-term Trend",
                "description": f"50-period Exponential Moving Average is {round(ema_50, 2)}"
            },
            "rsi_14": {
                "name": "RSI (14)",
                "value": round(rsi_14, 2),
                "status": rsi_status,
                "description": f"14-period Relative Strength Index is {round(rsi_14, 2)}"
            },
            "macd": {
                "name": "MACD",
                "value": round(macd_line, 2),
                "status": "Positive Momentum" if macd_line > 0 else "Negative Momentum",
                "description": f"Moving Average Convergence Divergence line at {round(macd_line, 2)}"
            },
            "bollinger_bands": {
                "name": "Bollinger Bands",
                "value": round(bb_middle, 2),
                "status": "Middle Band",
                "description": f"Upper: {round(bb_upper, 2)} | Middle: {round(bb_middle, 2)} | Lower: {round(bb_lower, 2)}"
            },
            "atr": {
                "name": "ATR (14)",
                "value": round(atr_14, 2),
                "status": "Volatility",
                "description": f"Average True Range indicator shows average price range of {round(atr_14, 2)}"
            },
            "obv": {
                "name": "OBV",
                "value": round(obv_val, 2),
                "status": "Volume Trend",
                "description": f"On-Balance Volume cumulative trend indicator value is {round(obv_val, 2)}"
            },
            "summary": f"Technical indicators for {symbol} indicate current RSI at {round(rsi_14, 2)} ({rsi_status}) and price relative to 20-day SMA at {round(sma_20, 2)}. Provided for informational market analysis only."
        }

    @classmethod
    def get_default_indicators(cls, symbol: str) -> Dict[str, Any]:
        return {
            "symbol": symbol,
            "sma_20": {"name": "SMA (20)", "value": 0.0, "status": "Neutral", "description": "20-period SMA"},
            "sma_50": {"name": "SMA (50)", "value": 0.0, "status": "Neutral", "description": "50-period SMA"},
            "sma_200": {"name": "SMA (200)", "value": 0.0, "status": "Neutral", "description": "200-period SMA"},
            "ema_20": {"name": "EMA (20)", "value": 0.0, "status": "Neutral", "description": "20-period EMA"},
            "ema_50": {"name": "EMA (50)", "value": 0.0, "status": "Neutral", "description": "50-period EMA"},
            "rsi_14": {"name": "RSI (14)", "value": 50.0, "status": "Neutral", "description": "14-period RSI"},
            "macd": {"name": "MACD", "value": 0.0, "status": "Neutral", "description": "MACD indicator"},
            "bollinger_bands": {"name": "Bollinger Bands", "value": 0.0, "status": "Neutral", "description": "Bollinger Bands"},
            "atr": {"name": "ATR (14)", "value": 0.0, "status": "Neutral", "description": "Average True Range"},
            "obv": {"name": "OBV", "value": 0.0, "status": "Neutral", "description": "On-Balance Volume"},
            "summary": f"Technical analysis data for {symbol} is currently calculating. Provided for informational purposes only."
        }
