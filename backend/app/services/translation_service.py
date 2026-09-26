from typing import Dict
from app.core.config import settings

# Comprehensive English to Telugu Financial & News Vocabulary Dictionary & Paragraph Translations
TELUGU_FINANCIAL_DICT = {
    "Indian Banking Stocks Rise After Strong Quarterly Financial Results":
        (
            "భారతీయ బ్యాంకింగ్ షేర్లు లాభపడ్డాయి",
            "రకరకాల నికర వడ్డీ మార్జిన్ల విస్తరణ మరియు మెరుగైన ఆస్తుల నాణ్యత కారణంగా ప్రముఖ భారతీయ ప్రభుత్వ, ప్రైవేట్ రంగ బ్యాంకులు బలమైన త్రైమాసిక ఆదాయాలను నమోదు చేశాయి. పెట్టుబడిదారుల నుంచి సానుకూల స్పందన లభించింది."
        ),
    "NIFTY 50 Touches New High Powered by IT and Auto Rally":
        (
            "నిఫ్టీ 50 కొత్త రికార్డు స్థాయి",
            "ఐటీ మరియు ఆటోమొబైల్ షేర్లలో సంస్థాగత పెట్టుబడిదారుల కొనుగోళ్ల మద్దతుతో బెంచ్‌మార్క్ సూచీ నిఫ్టీ 50 కీలక నిరోధక స్థాయిలను దాటి సరికొత్త గరిష్ట స్థాయిని తాకింది."
        ),
    "RBI Keeps Repo Rate Unchanged at 6.5 Percent Citing Inflation Targets":
        "ఆర్‌బీఐ రెపో రేటు యథాతథం",
    "Infosys Secures Major AI Transformation Deal Worth 1.5 Billion USD":
        "ఇన్ఫోసిస్ భారీ ఏఐ డీల్",
    "Reliance Industries Announces Major Expansion in Green Energy Manufacturing":
        "గ్రీన్ ఎనర్జీ తయారీలో రిలయన్స్ భారీ విస్తరణ",
    "Global Markets Trading Mix as US Fed Signals Cautious Rate Cuts":
        "గ్లోబల్ మార్కెట్లలో మిశ్రమ ట్రేడింగ్",
    "Crude oil prices fall on easing global demand concerns":
        (
            "క్రూడ్ ఆయిల్ ధరలు తగ్గాయి",
            "ప్రపంచ డిమాండ్ ఆందోళనల నేపథ్యంలో అంతర్జాతీయ మార్కెట్లో క్రూడ్ ఆయిల్ ధరలు పడిపోయాయి. ప్రపంచ ఆర్థిక వృద్ధిపై ఉన్న ఆశంకర కారణంగా అంతర్జాతీయ మార్కెట్లలో క్రూడ్ ఆయిల్ ధరలు తగ్గుముఖం పట్టాయి."
        ),
    "RBI keeps repo rate unchanged at 6.50%":
        (
            "ఆర్‌బీఐ రెపో రేటును 6.50% వద్ద యథాతథంగా ఉంచింది",
            "ద్రవ్యోల్బణాన్ని అదుపులో ఉంచుందుకు తీసుకుంటున్న చర్యల్లో భాగంగా ఆర్‌బీఐ వడ్డీ రేట్లను మార్చలేదు."
        ),
    "IT companies see strong deal pipeline in Q3":
        (
            "ఐటీ కంపెనీలకు క్యూ3లో బలమైన డీల్ పైప్‌లైన్ కనిపిస్తోంది",
            "ప్రధాన ఐటీ కంపెనీలు ఈ త్రైమాసికంలో పెద్ద ఆర్డర్లతో ముందుకు వెళ్లే అవకాశం ఉందని నిపుణులు అంచనా వేస్తున్నారు."
        ),
    "US and allies discuss new steps on global trade":
        (
            "ప్రపంచ వాణిజ్యంపై అమెరికా ముదియు మిత్రదేశాలు కొత్త చర్యలపై చర్చ",
            "అంతర్జాతీయ వాణిజ్య మార్గాలను మరింత పటిష్టం చేసేందుకు అమెరికా మరియు మిత్రదేశాలు ఉన్నత స్థాయి సమావేశాలు నిర్వహించాయి."
        )
}

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
    "growth": "వృద్ధి", "high": "గరిష్టం", "low": "కనిష్టం",
    "india": "భారత్", "indian": "భారతీయ", "global": "ప్రపంచ",
    "today": "ఈరోజు", "new": "కొత్త", "strong": "బలమైన", "prices": "ధరలు",
    "demand": "డిమాండ్", "global": "ప్రపంచ"
}

class TranslationService:
    @classmethod
    async def translate_english_to_telugu(cls, text: str) -> tuple[str, str]:
        if not text or not text.strip():
            return "", ""

        text_clean = text.strip()

        if text_clean in TELUGU_FINANCIAL_DICT:
            val = TELUGU_FINANCIAL_DICT[text_clean]
            if isinstance(val, tuple):
                return val[0], val[1]
            return val, f"{val}. ప్రపంచ ఆర్థిక మార్కెట్లలో దీని ప్రభావం సానుకూలంగా ఉండే అవకాశం ఉంది."

        # Fuzzy or keyword match
        for eng_key, tel_val in TELUGU_FINANCIAL_DICT.items():
            if eng_key.lower() in text_clean.lower() or text_clean.lower() in eng_key.lower():
                if isinstance(tel_val, tuple):
                    return tel_val[0], tel_val[1]
                return tel_val, f"{tel_val}. పెట్టుబడిదారుల సెంటిమెంట్ సానుకూలంగా ఉంది."

        # Dynamic translation
        translated_words = []
        for w in text_clean.split():
            clean_w = w.lower().strip(".,;:!?\"'()")
            if clean_w in WORD_MAP:
                translated_words.append(WORD_MAP[clean_w])
            else:
                translated_words.append(w)

        title_res = " ".join(translated_words)
        desc_res = f"{title_res}. ఈ పరిణామం భారతీయ స్టాక్ మార్కెట్లు మరియు ఆర్థిక రంగాలపై ప్రభావం చూపనుంది."
        return title_res, desc_res
