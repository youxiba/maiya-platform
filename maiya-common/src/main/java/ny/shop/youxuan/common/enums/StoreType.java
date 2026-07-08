package ny.shop.youxuan.common.enums;

public enum StoreType {
    MYSTORE("MYSTORE"), PERSONAL("PERSONAL");

    private final String value;

    StoreType(String v) {
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