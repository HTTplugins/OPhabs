package fruitSystem;

public class fruitIdentification {

    public final static int fruitsNumber = 5;

    public final static String
            fruitItemNameYami = "yami yami no mi",
            fruitItemNameMera = "mera mera no mi",
            fruitItemNameGura = "gura gura no mi",
            fruitItemNameMoku = "moku moku no mi",
            fruitItemNameNekoReoparudo = "neko neko reoparudo no mi",
            fruitItemNameMagu = "magu magu no mi",
            fruitItemNameGoro = "goro goro no mi",
            fruitItemNameIshi = "ishi ishi no mi";
    public final static String
            fruitCommandNameYami = "yami_yami",
            fruitCommandNameMera = "mera_mera",
            fruitCommandNameGura = "gura_gura",
            fruitCommandNameMoku = "moku_moku",
            fruitCommandNameNekoReoparudo = "neko_neko_reoparudo",
            fruitCommandNameMagu = "magu_magu",
            fruitCommandNameGoro = "goro_goro",
            fruitCommandNameIshi = "ishi_ishi";

    public String getItemName(String fruitCommandName){
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
            default:
                return null;
        }
    }
}
