package ny.shop.youxuan.common.enums;

public enum DtbtType {
    MAIYA("MAIYA"), MCH_GF("MCH_GF"), MCH_JT("MCH_JT"), SELF("SELF");

    private final String value;

    DtbtType(String v) {
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