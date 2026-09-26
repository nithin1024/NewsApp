from typing import List, Dict, Any, Optional
from app.services.upstox_service import UpstoxService
from app.services.technical_analysis_service import TechnicalAnalysisService

SEARCHABLE_STOCKS = [
    {"symbol": "RELIANCE", "name": "Reliance Industries Ltd", "sector": "Energy", "exchange": "NSE"},
    {"symbol": "TCS", "name": "Tata Consultancy Services Ltd", "sector": "IT", "exchange": "NSE"},
    {"symbol": "INFY", "name": "Infosys Limited", "sector": "IT", "exchange": "NSE"},
    {"symbol": "HDFCBANK", "name": "HDFC Bank Limited", "sector": "Banking", "exchange": "NSE"},
    {"symbol": "ICICIBANK", "name": "ICICI Bank Limited", "sector": "Banking", "exchange": "NSE"},
    {"symbol": "SBIN", "name": "State Bank of India", "sector": "Banking", "exchange": "NSE"},
    {"symbol": "BHARTIARTL", "name": "Bharti Airtel Limited", "sector": "Telecom", "exchange": "NSE"},
    {"symbol": "TATAMOTORS", "name": "Tata Motors Limited", "sector": "Auto", "exchange": "NSE"},
    {"symbol": "AAPL", "name": "Apple Inc", "sector": "Global Tech", "exchange": "NASDAQ"},
    {"symbol": "MSFT", "name": "Microsoft Corporation", "sector": "Global Tech", "exchange": "NASDAQ"},
    {"symbol": "TSLA", "name": "Tesla Inc", "sector": "Global Auto", "exchange": "NASDAQ"}
]

GLOBAL_INDICES = [
    {"symbol": "S&P 500", "name": "S&P 500 Index", "region": "GLOBAL", "is_available": True, "value": 5620.40, "change": 24.10, "pct": 0.43},
    {"symbol": "NASDAQ", "name": "NASDAQ Composite", "region": "GLOBAL", "is_available": True, "value": 17810.25, "change": 115.80, "pct": 0.65},
    {"symbol": "Dow Jones", "name": "Dow Jones Industrial Average", "region": "GLOBAL", "is_available": True, "value": 41240.50, "change": 80.20, "pct": 0.19},
    {"symbol": "FTSE 100", "name": "FTSE 100 Index", "region": "GLOBAL", "is_available": True, "value": 8280.10, "change": 12.40, "pct": 0.15},
    {"symbol": "DAX", "name": "DAX Performance Index", "region": "GLOBAL", "is_available": True, "value": 18560.30, "change": 45.60, "pct": 0.25},
    {"symbol": "NIKKEI", "name": "Nikkei 225", "region": "GLOBAL", "is_available": True, "value": 38400.00, "change": -120.00, "pct": -0.31},
    {"symbol": "HANG SENG", "name": "Hang Seng Index", "region": "GLOBAL", "is_available": False, "value": 0.0, "change": 0.0, "pct": 0.0},
    {"symbol": "GIFT NIFTY", "name": "GIFT NIFTY", "region": "GLOBAL", "is_available": True, "value": 24920.00, "change": 110.00, "pct": 0.44}
]

class StockService:
    @classmethod
    def search_stocks(cls, query: str) -> List[Dict[str, Any]]:
        q_clean = query.lower().strip()
        results = []
        for stock in SEARCHABLE_STOCKS:
            if q_clean in stock["symbol"].lower() or q_clean in stock["name"].lower() or q_clean in stock["sector"].lower():
                results.append(stock)
        return results

    @classmethod
    async def get_stock_details(cls, symbol: str) -> Dict[str, Any]:
        quotes = await UpstoxService.fetch_market_quotes([symbol])
        if quotes:
            return quotes[0]
        return UpstoxService.get_fallback_quote(symbol.upper())

    @classmethod
    async def get_technical_analysis(cls, symbol: str) -> Dict[str, Any]:
        candles = await UpstoxService.fetch_historical_candles(symbol, period="1M")
        return TechnicalAnalysisService.calculate_indicators(symbol.upper(), candles)

    @classmethod
    async def get_indian_markets(cls) -> List[Dict[str, Any]]:
        symbols = ["NIFTY 50", "BANK NIFTY", "SENSEX", "INDIA VIX"]
        return await UpstoxService.fetch_market_quotes(symbols)

    @classmethod
    def get_global_markets(cls) -> List[Dict[str, Any]]:
        results = []
        for g in GLOBAL_INDICES:
            results.append({
                "symbol": g["symbol"],
                "name": g["name"],
                "region": "GLOBAL",
                "current_value": g["value"],
                "change": g["change"],
                "percent_change": g["pct"],
                "market_status": "OPEN" if g["is_available"] else "UNAVAILABLE",
                "is_available": g["is_available"],
                "last_updated": "2026-09-26T10:00:00Z"
            })
        return results
