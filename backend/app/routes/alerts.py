from fastapi import APIRouter
from typing import List
from datetime import datetime
from app.schemas.alert import PriceAlertCreate, TechnicalAlertCreate, PriceAlertSchema, TechnicalAlertSchema, AlertsResponse

router = APIRouter(prefix="/alerts", tags=["Alerts"])

PRICE_ALERTS_STORE = []
TECH_ALERTS_STORE = []

@router.get("", response_model=AlertsResponse)
async def get_alerts():
    return AlertsResponse(
        price_alerts=[PriceAlertSchema(**p) for p in PRICE_ALERTS_STORE],
        technical_alerts=[TechnicalAlertSchema(**t) for t in TECH_ALERTS_STORE]
    )

@router.post("/price", response_model=PriceAlertSchema)
async def create_price_alert(req: PriceAlertCreate):
    alert_obj = {
        "id": len(PRICE_ALERTS_STORE) + 1,
        "stock_symbol": req.stock_symbol.upper(),
        "condition": req.condition,
        "target_price": req.target_price,
        "is_active": True,
        "created_at": datetime.utcnow()
    }
    PRICE_ALERTS_STORE.append(alert_obj)
    return PriceAlertSchema(**alert_obj)

@router.post("/technical", response_model=TechnicalAlertSchema)
async def create_technical_alert(req: TechnicalAlertCreate):
    alert_obj = {
        "id": len(TECH_ALERTS_STORE) + 1,
        "stock_symbol": req.stock_symbol.upper(),
        "indicator": req.indicator,
        "condition": req.condition,
        "value": req.value,
        "is_active": True,
        "created_at": datetime.utcnow()
    }
    TECH_ALERTS_STORE.append(alert_obj)
    return TechnicalAlertSchema(**alert_obj)

@router.delete("/{alert_id}")
async def delete_alert(alert_id: int):
    global PRICE_ALERTS_STORE, TECH_ALERTS_STORE
    PRICE_ALERTS_STORE = [p for p in PRICE_ALERTS_STORE if p["id"] != alert_id]
    TECH_ALERTS_STORE = [t for t in TECH_ALERTS_STORE if t["id"] != alert_id]
    return {"status": "deleted", "id": alert_id}
