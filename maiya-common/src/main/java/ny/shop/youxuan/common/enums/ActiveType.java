package ny.shop.youxuan.common.enums;

public enum ActiveType {
    NON("NON"), GOODS_DIS("GOODS_DIS"), GOODS_FS("GOODS_FS"), GOODS_GROUP("GOODS_GROUP"), GOODS_LIST("GOODS_LIST");

    private final String value;

    ActiveType(String v) {
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