# 🌐 NewsTelugu - Telugu-First Financial News & Stock Market App

**NewsTelugu** is a professional Android application and backend service built for real-time financial news, automatic English-to-Telugu translation (powered by AI4Bharat IndicTrans2), market sentiment analysis (powered by OpenAI), real-time stock quotes, technical indicator analysis (powered by Upstox), and push notifications (powered by Firebase Cloud Messaging).

---

## 🏗️ Architecture Overview

```
Android App (Kotlin, Jetpack Compose, Material 3, Room, Retrofit, FCM)
        │
        ▼ (HTTP REST API)
NewsTelugu FastAPI Backend (Python, FastAPI, SQLAlchemy, Pandas, AsyncPG)
   ├── NewsData.io (Real News Feed)
   ├── Upstox API V3 (Indian Market Quotes, Candles & Instruments)
   ├── AI4Bharat IndicTrans2 (English -> Telugu Automatic Translation)
   ├── OpenAI API (Financial Sentiment & Sector Analysis)
   └── Firebase Admin SDK (Categorized Push Notifications)
```

### 🔒 Security Architecture
- **No API Keys in APK**: All secret credentials (`UPSTOX_ACCESS_TOKEN`, `NEWSDATA_API_KEY`, `OPENAI_API_KEY`, `FIREBASE_CREDENTIALS`) are securely kept on the backend in environment variables / `.env`.
- The Android application only references `BACKEND_BASE_URL` (configurable in `BuildConfig.DEFAULT_BASE_URL` or app settings).

---

## 🚀 Backend Setup & Run Instructions

### 1. Requirements
- Python 3.10+
- PostgreSQL (or local SQLite fallback)

### 2. Environment Configuration
Navigate to the `backend/` directory and create `.env` from `.env.example`:

```bash
cd backend
cp .env.example .env
```

Edit `.env` to supply your credentials:
```env
UPSTOX_ACCESS_TOKEN=your_upstox_access_token
NEWSDATA_API_KEY=your_newsdata_api_key
OPENAI_API_KEY=your_openai_api_key
DATABASE_URL=sqlite+aiosqlite:///./newstelugu.db
FIREBASE_CREDENTIALS_PATH=serviceAccountKey.json
INDICTRANS_MODEL_NAME=ai4bharat/indictrans2-en-indic-dist-200m
```

### 3. Install Dependencies & Run Server
```bash
cd backend
python -m venv venv
# On Windows:
venv\Scripts\activate
# On Linux/macOS:
source venv/bin/activate

pip install -r requirements.txt
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 4. Health Check
Open your browser or run:
```bash
curl http://localhost:8000/health
```
Response:
```json
{
  "status": "ok"
}
```

System status endpoint: `http://localhost:8000/api/v1/system/status`

---

## 📱 Android App Setup & Build

### 1. Requirements
- Android Studio Ladybug / Jellyfish / 2024.x+
- Android SDK 35
- JDK 17

### 2. Base URL Configuration
- **Android Emulator**: Uses `http://10.0.2.2:8000/` (pre-configured in `app/build.gradle.kts`).
- **Physical Device**: Update base URL to your PC's LAN IP (e.g. `http://192.168.1.100:8000/`).

### 3. Build & Deploy
Run in terminal or IDE:
```bash
./gradlew app:assembleDebug
```

---

## 📱 Application Screens (18 Screens)

1. **Home Screen**: Telugu tagline, Indian market ticker cards (NIFTY 50, SENSEX, BANK NIFTY, INDIA VIX), breaking news banner, latest translated news feeds.
2. **Markets Screen**: Indian market indices overview and quick search navigation.
3. **Global Markets Screen**: S&P 500, NASDAQ, Dow Jones, FTSE 100, DAX, NIKKEI, HANG SENG, GIFT NIFTY.
4. **News Categories Screen**: Grid of all news categories (Top Stories, India, Business, Banking, Stock Market, Technology, Telangana, Andhra Pradesh, etc.).
5. **News List Screen**: Categorized news stream in Telugu.
6. **Article Details Screen**: Full Telugu translated article, original English headline, sentiment badge, market relevance, source attribution, share, and "Read Original Article" link.
7. **Stock Search Screen**: Real-time stock search for NSE/BSE and global instruments.
8. **Stock Details Screen**: Price quotes, 1D/1W/1M/3M/6M/1Y/5Y charts, key statistics (52W High/Low, Volume), and technical indicator analysis.
9. **Technical Indicator Analysis**: SMA (20, 50, 200), EMA (20, 50), RSI (14), MACD, Bollinger Bands, ATR, OBV with neutral informational summaries.
10. **Watchlist Screen**: Local & backend synced watchlist with stock price alerts.
11. **Alerts Management Screen**: Price alerts (Above / Below) and Technical indicator alerts (RSI, Moving Averages).
12. **Notification History Screen**: History of categorized push alerts (Positive, Negative, Neutral, Breaking, Market, Technical, Price).
13. **Notification Settings Screen**: Independent toggles for notification categories.
14. **Global Search Screen**: Instant search for stocks, companies, news, and categories.
15. **Language Settings Screen**: Telugu / English selection.
16. **Theme Settings Screen**: Light / Dark / System Default themes.
17. **Personalization Screen**: Favorite categories, companies, and sectors.
18. **About & Financial Disclaimer**: Informational notice emphasizing market data is provided for informational purposes only and does not constitute investment advice.

---

## ⚙️ Key Technical Features
- **Automatic Translation**: English news headlines and descriptions automatically converted into Telugu using AI4Bharat IndicTrans2 and financial translation dictionary rules.
- **Sentiment Classification**: Context-aware sentiment analysis (-1.0 to +1.0) with POSITIVE (🟢), NEGATIVE (🔴), NEUTRAL (⚪) badges.
- **Offline Caching**: Room local database caches articles, watchlist items, user preferences, and notification history for offline reading.
- **Background Sync**: Android WorkManager syncs latest news every 30 minutes in the background.

---

## ⚖️ Financial Disclaimer
Market information, news analysis, and technical indicators provided in NewsTelugu are for informational purposes only and do not constitute investment or financial advice.
