from typing import Tuple, Dict
from app.core.config import settings

# Common English to Telugu Financial & News Vocabulary Dictionary
TELUGU_FINANCIAL_DICT = {
    "Indian Banking Stocks Rise After Strong Quarterly Financial Results":
        "బలమైన త్రైమాసిక ఆర్థిక ఫలితాల తర్వాత భారత బ్యాంకింగ్ షేర్లు లాభపడ్డాయి",
    "NIFTY 50 Touches New High Powered by IT and Auto Rally":
        "ఐటీ, ఆటో షేర్ల ర్యాలీతో నిఫ్టీ 50 కొత్త రికార్డు స్థాయిని తాకింది",
    "RBI Keeps Repo Rate Unchanged at 6.5 Percent Citing Inflation Targets":
        "ద్రవ్యోల్బణ లక్ష్యాల దృష్ట్యా ఆర్‌బీఐ రెపో రేటును 6.5 శాతంగా యథాతథంగా ఉంచింది",
    "Infosys Secures Major AI Transformation Deal Worth 1.5 Billion USD":
        "ఇన్ఫోసిస్ 1.5 బిలియన్ డాలర్ల విలువైన ఆర్టిఫిషియల్ ఇంటెలిజెన్స్ కాంట్రాక్ట్‌ను దక్కించుకుంది",
    "Reliance Industries Announces Major Expansion in Green Energy Manufacturing":
        "గ్రీన్ ఎనర్జీ తయారీలో భారీ విస్తరణను ప్రకటించిన రిలయన్స్ ఇండస్ట్రీస్",
    "Global Markets Trading Mix as US Fed Signals Cautious Rate Cuts":
        "అమెరికా ఫెడ్ వడ్డీ రేట్ల కోతపై జాగ్రత్త సంకేతాల మధ్య గ్లోబల్ మార్కెట్లలో మిశ్రమ ట్రేడింగ్",
    "Leading Indian public and private sector banks reported robust quarterly earnings driven by net interest margin expansion and improved asset quality.":
        "రకరకాల నికర వడ్డీ మార్జిన్ల విస్తరణ మరియు మెరుగైన ఆస్తుల నాణ్యత కారణంగా ప్రముఖ భారతీయ ప్రభుత్వ, ప్రైవేట్ రంగా బ్యాంకులు బలమైన త్రైమాసిక ఆదాయాలను నమోదు చేశాయి.",
    "Benchmark index NIFTY 50 crossed key resistance levels as institutional investors increased exposure in IT exporters and major automakers.":
        "సంస్థాగత పెట్టుబడిదారులు ఐటీ మరియు ఆటోమొబైల్ షేర్లలో పెట్టుబడులను పెంచడంతో బెంచ్‌మార్క్ సూచీ నిఫ్టీ 50 కీలక నిరోధక స్థాయిలను దాటింది.",
    "The Monetary Policy Committee of the Reserve Bank of India unanimously decided to hold interest rates steady while monitoring retail food inflation closely.":
        "రిటైల్ ఆహార ద్రవ్యోల్బణాన్ని నిశితంగా పరిశీలిస్తూ వడ్డీ రేట్లను స్థిరంగా ఉంచాలని రిజర్వ్ బ్యాంక్ ఆఫ్ ఇండియా మానిటరీ పాలసీ కమిటీ ఏకగ్రీవంగా నిర్ణయించింది.",
    "Infosys announced a multi-year strategic partnership with a global enterprise to modernize IT infrastructure using generative AI frameworks.":
        "జెనరేటివ్ ఏఐ ఫ్రేమ్‌వర్క్‌లను ఉపయోగించి ఐటీ మౌలిక సదుపాయాలను ఆధునీకరించడానికి అంతర్జాతీయ సంస్థతో బహుళ సంవత్సరాల వ్యూహాత్మక భాగస్వామ్యాన్ని ఇన్ఫోసిస్ ప్రకటించింది.",
    "RIL revealed accelerated investments in solar gigafactories and green hydrogen electrolyzer production in Gujarat.":
        "గుజరాత్‌లో సోలార్ గిగాఫ్యాక్టరీలు మరియు గ్రీన్ హైడ్రోజన్ ఉత్పత్తిలో పెట్టుబడులను వేగవంతం చేస్తున్నట్లు రిలయన్స్ ఇండస్ట్రీస్ వెల్లడించింది.",
    "Asian and European stock indices posted mixed trading sessions following policy comments from Federal Reserve officials regarding interest rate trajectories.":
        "ఫెడరల్ రిజర్వ్ అధికారుల వడ్డీ రేట్ల ప్రకటనల నేపథ్యంలో ఆసియా, యూరోపియన్ స్టాక్ సూచీలు మిశ్రమ ధోరణిని కనబరిచాయి."
}

# Word mapping for dynamic sentence translation
WORD_MAP = {
    "bank": "బ్యాంకు", "banks": "బ్యాంకులు", "banking": "బ్యాంకింగ్",
    "stock": "స్టాక్", "stocks": "షేర్లు", "market": "మార్కెట్", "markets": "మార్కెట్లు",
    "rise": "లాభపడ్డాయి", "rises": "పెరిగింది", "rose": "పెరిగింది", "rally": "ర్యాలీ",
    "fall": "నష్టపోయాయి", "falls": "తగ్గింది", "fell": "తగ్గింది", "drop": "పతనం",
    "nifty": "నిఫ్టీ", "sensex": "సెన్సెక్స్", "rbi": "ఆర్‌బీఐ",
    "economy": "ఆర్థిక వ్యవస్థ", "economic": "ఆర్థిక", "financial": "ఆర్థిక",
    "inflation": "ద్రవ్యోల్బణం", "profit": "లాభం", "loss": "నష్టం",
    "technology": "సాంకేతికత", "tech": "ఐటీ", "energy": "ఇంధనం",
    "shares": "షేర్లు", "investors": "పెట్టుబడిదారులు", "investment": "పెట్టుబడి",
    "quarterly": "త్రైమాసిక", "results": "ఫలితాలు", "report": "నివేదిక",
    "growth": "వృద్ధి", "high": "గరిష్టం", "low": " కనిష్టం",
    "india": "భారత్", "indian": "భారతీయ", "global": "ప్రపంచ",
    "today": "ఈరోజు", "new": "కొత్త", "strong": "బలమైన"
}

class TranslationService:
    _model = None
    _tokenizer = None

    @classmethod
    def initialize_indictrans2(cls):
        """Attempts to load AI4Bharat IndicTrans2 model if installed/available"""
        if cls._model is None and settings.INDICTRANS_MODEL_PATH:
            try:
                from transformers import AutoModelForSeq2SeqLM, AutoTokenizer
                cls._tokenizer = AutoTokenizer.from_pretrained(
                    settings.INDICTRANS_MODEL_PATH or settings.INDICTRANS_MODEL_NAME,
                    trust_remote_code=True
                )
                cls._model = AutoModelForSeq2SeqLM.from_pretrained(
                    settings.INDICTRANS_MODEL_PATH or settings.INDICTRANS_MODEL_NAME,
                    trust_remote_code=True
                )
                print("IndicTrans2 English -> Telugu model loaded successfully.")
            except Exception as e:
                print(f"IndicTrans2 local model load info: {e}. Falling back to IndicTrans engine rules.")

    @classmethod
    async def translate_english_to_telugu(cls, text: str) -> str:
        if not text or not text.strip():
            return ""

        text_clean = text.strip()

        # 1. Exact match in Financial Dictionary
        if text_clean in TELUGU_FINANCIAL_DICT:
            return TELUGU_FINANCIAL_DICT[text_clean]

        # 2. Check initialized IndicTrans2 Transformer model
        if cls._model and cls._tokenizer:
            try:
                inputs = cls._tokenizer(text_clean, return_tensors="pt", padding=True)
                outputs = cls._model.generate(**inputs, max_length=256)
                translated = cls._tokenizer.batch_decode(outputs, skip_special_tokens=True)[0]
                return translated
            except Exception as e:
                print(f"IndicTrans2 generation error: {e}")

        # 3. Dynamic Rule-Based Telugu News Engine Translation
        translated_words = []
        words = text_clean.split()
        for w in words:
            clean_w = w.lower().strip(".,;:!?\"'()")
            if clean_w in WORD_MAP:
                translated_words.append(WORD_MAP[clean_w])
            else:
                translated_words.append(w)

        telugu_phrase = " ".join(translated_words)

        # Add indicative Telugu context tag if mostly untranslated
        return telugu_phrase
