package fruitSystem;

public class fruitIdentification {

    public final static int fruitsNumber = 11;

    public final static String
            fruitItemNameYami = "yami yami no mi",
            fruitItemNameMera = "mera mera no mi",
            fruitItemNameGura = "gura gura no mi",
            fruitItemNameMoku = "moku moku no mi",
            fruitItemNameNekoReoparudo = "neko neko reoparudo no mi",
            fruitItemNameMagu = "magu magu no mi",
            fruitItemNameGoro = "goro goro no mi",
            fruitItemNameIshi = "ishi ishi no mi",
            fruitItemNameGoru = "goru goru no mi",
            fruitItemNameInuOkuchi = "inu inu okuchi no makami",
            fruitItemNameRyuAllosaurs = "ryu ryu allosaurs no mi";


    public final static String
            fruitCommandNameYami = "yami_yami",
            fruitCommandNameMera = "mera_mera",
            fruitCommandNameGura = "gura_gura",
            fruitCommandNameMoku = "moku_moku",
            fruitCommandNameNekoReoparudo = "neko_neko_reoparudo",
            fruitCommandNameMagu = "magu_magu",
            fruitCommandNameGoro = "goro_goro",
            fruitCommandNameIshi = "ishi_ishi",
            fruitCommandNameGoru = "goru_goru",
            fruitCommandNameInuOkuchi = "inu_inu_okuchi",
            fruitCommandNameRyuAllosaurs = "ryu_ryu_allosaurs";

    public static String getItemName(String fruitCommandName){
        switch (fruitCommandName){
            case fruitCommandNameYami:
                return fruitItemNameYami;
            case fruitCommandNameMera:
                return fruitItemNameMera;
            case fruitCommandNameGura:
                return fruitItemNameGura;
            case fruitCommandNameMoku:
                return fruitItemNameMoku;
            case fruitCommandNameNekoReoparudo:
                return fruitItemNameNekoReoparudo;
            case fruitCommandNameMagu:
                return fruitItemNameMagu;
            case fruitCommandNameGoro:
                return fruitItemNameGoro;
            case fruitCommandNameIshi:
                return fruitItemNameIshi;
            case fruitCommandNameGoru:
                return fruitItemNameGoru;
            case fruitCommandNameInuOkuchi:
                return fruitItemNameInuOkuchi;
            case fruitCommandNameRyuAllosaurs:
                return fruitItemNameRyuAllosaurs;
            default:
                return null;
        }
    }

    public String getCommandName(String fruitItemName){
        switch (fruitItemName){
            case fruitItemNameYami:
                return fruitCommandNameYami;
            case fruitItemNameMera:
                return fruitCommandNameMera;
            case fruitItemNameGura:
                return fruitCommandNameGura;
            case fruitItemNameMoku:
                return fruitCommandNameMoku;
            case fruitItemNameNekoReoparudo:
                return fruitCommandNameNekoReoparudo;
            case fruitItemNameMagu:
                return fruitCommandNameMagu;
            case fruitItemNameGoro:
                return fruitCommandNameGoro;
            case fruitItemNameIshi:
                return fruitCommandNameIshi;
            case fruitItemNameGoru:
                return fruitCommandNameGoru;
            case fruitItemNameInuOkuchi:
                return fruitCommandNameInuOkuchi;
            case fruitItemNameRyuAllosaurs:
                return fruitCommandNameRyuAllosaurs;
            default:
                return null;
        }
    }

    public static boolean isFruit(String fruitItemName){
        switch (fruitItemName){
            case fruitItemNameYami:
                return true;
            case fruitItemNameMera:
                return true;
            case fruitItemNameGura:
                return true;
            case fruitItemNameMoku:
                return true;
            case fruitItemNameNekoReoparudo:
                return true;
            case fruitItemNameMagu:
                return true;
            case fruitItemNameGoro:
                return true;
            case fruitItemNameIshi:
                return true;
            case fruitItemNameGoru:
                return true;
            case fruitItemNameInuOkuchi:
                return true;
            case fruitItemNameRyuAllosaurs:
                return true;
            default:
                return false;
        }
    }
}
