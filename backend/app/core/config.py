import os
from pydantic_settings import BaseSettings, SettingsConfigDict
from typing import Optional

class Settings(BaseSettings):
    APP_NAME: str = "NewsTelugu Backend"
    API_V1_STR: str = "/api/v1"

    # API Credentials (Loaded from environment / .env file)
    UPSTOX_ACCESS_TOKEN: Optional[str] = None
    NEWSDATA_API_KEY: Optional[str] = None
    OPENAI_API_KEY: Optional[str] = None

    # Database
    DATABASE_URL: str = "sqlite+aiosqlite:///./newstelugu.db"

    # Security & Firebase
    JWT_SECRET: str = "default_jwt_secret_change_me"
    FIREBASE_CREDENTIALS_PATH: str = "serviceAccountKey.json"

    # IndicTrans2 Translation
    INDICTRANS_MODEL_NAME: str = "ai4bharat/indictrans2-en-indic-dist-200m"
    INDICTRANS_MODEL_PATH: Optional[str] = None

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore"
    )

settings = Settings()
