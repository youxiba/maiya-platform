package ny.shop.youxuan.common.enums;

public enum BalType {
    IN("IN"), OUT("OUT");

    private final String value;

    BalType(String v) {
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