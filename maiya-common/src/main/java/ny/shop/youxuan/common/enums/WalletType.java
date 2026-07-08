package ny.shop.youxuan.common.enums;

public enum WalletType {
    BANK("BANK"), QC_BANK("QC_BANK"), WX_WALLET("WX_WALLET"), ALI_WALLET("ALI_WALLET"), QC_WALLET("QC_WALLET");

    private final String value;

    WalletType(String v) {
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