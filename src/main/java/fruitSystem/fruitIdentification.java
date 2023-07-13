package fruitSystem;

/**
 * @brief Fruit identification class.
 */
public class fruitIdentification {

    public final static int fruitsNumber = 12;

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
            fruitItemNameInuUrufu = "inu inu urufu no mi",
            fruitItemNameRyuAllosaurs = "ryu ryu allosaurs no mi",
            fruitItemNameOpe = "ope ope no mi",
            fruitItemNameZushi = "zushi zushi no mi",
            fruitItemNameSuke = "Suke suke no mi",
            fruitItemNameHie = "Hie hie no mi",
            fruitItemNameBane = "Bane bane no mi";


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
            fruitCommandNameInuUrufu = "inu_inu_urufu",
            fruitCommandNameRyuAllosaurs = "ryu_ryu_allosaurs",
            fruitCommandNameOpe = "ope_ope",
            fruitCommandNameZushi = "zushi_zushi",
            fruitCommandNameSuke = "suke_suke",
            fruitCommandNameHie = "hie_hie",
            fruitCommandNameBane = "bane_bane";

    /**
     * @brief Obtains the fruit name from the command name.
     * @param fruitCommandName Command name.
     * @return Fruit name.
     */
    public static String getItemName(String fruitCommandName){
        String fruitItemName;
        switch (fruitCommandName){
            case fruitCommandNameYami:
                fruitItemName = fruitItemNameYami;
                break;
            case fruitCommandNameMera:
                fruitItemName = fruitItemNameMera;
                break;
            case fruitCommandNameGura:
                fruitItemName = fruitItemNameGura;
                break;
            case fruitCommandNameMoku:
                fruitItemName = fruitItemNameMoku;
                break;
            case fruitCommandNameNekoReoparudo:
                fruitItemName = fruitItemNameNekoReoparudo;
                break;
            case fruitCommandNameMagu:
                fruitItemName = fruitItemNameMagu;
                break;
            case fruitCommandNameGoro:
                fruitItemName = fruitItemNameGoro;
                break;
            case fruitCommandNameIshi:
                fruitItemName = fruitItemNameIshi;
                break;
            case fruitCommandNameGoru:
                fruitItemName = fruitItemNameGoru;
                break;
            case fruitCommandNameInuOkuchi:
                fruitItemName = fruitItemNameInuOkuchi;
                break;
            case fruitCommandNameInuUrufu:
                fruitItemName = fruitItemNameInuUrufu;
                break;
            case fruitCommandNameRyuAllosaurs:
                fruitItemName = fruitItemNameRyuAllosaurs;
                break;
            case fruitCommandNameOpe:
                fruitItemName = fruitItemNameOpe;
                break;
            case fruitCommandNameZushi:
                fruitItemName = fruitItemNameZushi;
                break;
            case fruitCommandNameSuke:
                fruitItemName = fruitItemNameSuke;
                break;
            case fruitCommandNameHie:
                fruitItemName = fruitItemNameHie;
                break;
            case fruitCommandNameBane:
                fruitItemName = fruitItemNameBane;
                break;
            default:
                fruitItemName = null;
                break;
        }
        return fruitItemName;
    }

    /**
     * @brief Obtains the command name from the item name.
     * @param fruitItemName fruit name.
     * @return Command name.
     */
    public static String getCommandName(String fruitItemName){
        String fruitCommandName;
        switch (fruitItemName){
            case fruitItemNameYami:
                fruitCommandName = fruitCommandNameYami;
                break;
            case fruitItemNameMera:
                fruitCommandName = fruitCommandNameMera;
                break;
            case fruitItemNameGura:
                fruitCommandName = fruitCommandNameGura;
                break;
            case fruitItemNameMoku:
                fruitCommandName = fruitCommandNameMoku;
                break;
            case fruitItemNameNekoReoparudo:
                fruitCommandName = fruitCommandNameNekoReoparudo;
                break;
            case fruitItemNameMagu:
                fruitCommandName = fruitCommandNameMagu;
                break;
            case fruitItemNameGoro:
                fruitCommandName = fruitCommandNameGoro;
                break;
            case fruitItemNameIshi:
                fruitCommandName = fruitCommandNameIshi;
                break;
            case fruitItemNameGoru:
                fruitCommandName = fruitCommandNameGoru;
                break;
            case fruitItemNameInuOkuchi:
                fruitCommandName = fruitCommandNameInuOkuchi;
                break;
            case fruitItemNameInuUrufu:
                fruitCommandName = fruitCommandNameInuUrufu;
                break;
            case fruitItemNameRyuAllosaurs:
                fruitCommandName = fruitCommandNameRyuAllosaurs;
                break;
            case fruitItemNameOpe:
                fruitCommandName = fruitCommandNameOpe;
                break;
            case fruitItemNameZushi:
                fruitCommandName = fruitCommandNameZushi;
                break;
            case fruitItemNameSuke:
                fruitCommandName = fruitCommandNameSuke;
                break;
            case fruitItemNameHie:
                fruitCommandName = fruitCommandNameHie;
                break;
            case fruitItemNameBane:
                fruitCommandName = fruitCommandNameBane;
                break;
            default:
                fruitCommandName = null;
                break;
        }
        return fruitCommandName;
    }

    /**
     * @brief Obtains if the string is an identificated fruit.
     * @param fruitItemName fruit name.
     * @return if exist.
     */
    public static boolean isFruit(String fruitItemName){
        return getCommandName(fruitItemName) != null;
    }
}
