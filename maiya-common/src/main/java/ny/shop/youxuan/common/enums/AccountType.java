package ny.shop.youxuan.common.enums;

public enum AccountType {
    XF("XF"), CZ("CZ"), TX("TX"), ORDER_MCH_SY("ORDER_MCH_SY"), ORDER_MY_BT("ORDER_MY_BT"),
    ORDER_MY_DTSY("ORDER_MY_DTSY"), ORDER_MY_FWSY("ORDER_MY_FWSY"), ORDER_MY_FXSY("ORDER_MY_FXSY"),
    ORDER_XFFL("ORDER_XFFL"), ORDER_ZTFH("ORDER_ZTFH"), ORDER_YWFH("ORDER_YWFH"), ORDER_ZJFH("ORDER_ZJFH"),
    RE("RE"), MCH_TX("MCH_TX"), DTBT_ZC("DTBT_ZC"), DTBT_SY("DTBT_SY"), RE_TX("RE_TX");

    private final String value;

    AccountType(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}