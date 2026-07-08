package ny.shop.youxuan.common.enums;

public enum CreatorType {
    USER("USER"), MCH("MCH");

    private final String value;

    CreatorType(String v) {
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