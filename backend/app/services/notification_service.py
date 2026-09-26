import os
from datetime import datetime
from typing import Optional, Dict, Any
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.notification import NotificationHistory
from app.core.config import settings

# Attempt Firebase Admin SDK initialization
FIREBASE_INITIALIZED = False
try:
    import firebase_admin
    from firebase_admin import credentials, messaging

    if os.path.exists(settings.FIREBASE_CREDENTIALS_PATH):
        cred = credentials.Certificate(settings.FIREBASE_CREDENTIALS_PATH)
        firebase_admin.initialize_app(cred)
        FIREBASE_INITIALIZED = True
        print("Firebase Admin SDK initialized successfully.")
except Exception as e:
    print(f"Firebase Admin SDK initialization skipped: {e}")

class NotificationService:
    @classmethod
    async def send_categorized_notification(
        cls,
        db: AsyncSession,
        device_token: Optional[str],
        notification_type: str,  # POSITIVE, NEGATIVE, NEUTRAL, BREAKING, PRICE, TECHNICAL, MARKET, WATCHLIST
        title: str,
        message: str,
        article_id: Optional[str] = None,
        stock_symbol: Optional[str] = None
    ) -> bool:
        # 1. Deduplication check against notification history
        stmt = select(NotificationHistory).where(
            NotificationHistory.title == title,
            NotificationHistory.type == notification_type
        )
        if article_id:
            stmt = stmt.where(NotificationHistory.article_id == article_id)

        res = await db.execute(stmt)
        existing = res.scalars().first()
        if existing:
            # Duplicate notification found, skip re-sending
            print(f"Skipping duplicate notification: {title}")
            return False

        # 2. Record in NotificationHistory database table
        notif_record = NotificationHistory(
            device_token=device_token,
            type=notification_type,
            title=title,
            message=message,
            article_id=article_id,
            stock_symbol=stock_symbol,
            created_at=datetime.utcnow()
        )
        db.add(notif_record)
        await db.commit()

        # 3. Format Notification Title with Emoji Category
        type_prefix = {
            "POSITIVE": "🟢 Positive News",
            "NEGATIVE": "🔴 Negative News",
            "NEUTRAL": "⚪ Neutral News",
            "BREAKING": "🚨 Breaking News",
            "PRICE": "📈 Price Alert",
            "TECHNICAL": "📊 Technical Alert",
            "MARKET": "🏦 Market Alert",
            "WATCHLIST": "⭐ Watchlist News"
        }
        formatted_title = f"{type_prefix.get(notification_type, '📰 News Alert')}: {title}"

        # 4. Dispatch via FCM if Firebase is active and device_token is valid
        if FIREBASE_INITIALIZED and device_token:
            try:
                fcm_msg = messaging.Message(
                    notification=messaging.Notification(
                        title=formatted_title,
                        body=message
                    ),
                    data={
                        "type": notification_type,
                        "article_id": article_id or "",
                        "stock_symbol": stock_symbol or ""
                    },
                    token=device_token
                )
                messaging.send(fcm_msg)
                print(f"FCM Notification sent to {device_token[:10]}...")
            except Exception as e:
                print(f"FCM Notification dispatch error: {e}")

        return True
