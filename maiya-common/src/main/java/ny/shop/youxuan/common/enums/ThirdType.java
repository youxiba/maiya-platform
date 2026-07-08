package ny.shop.youxuan.common.enums;

public enum ThirdType {
    QQ("QQ"), WECHAT("WECHAT"), APPLE("APPLE");

    private final String value;

    ThirdType(String v) {
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