package ny.shop.youxuan.common.enums;

public enum PayStatus {
    ORDER("ORDER"), PAID("PAID"), FAIL("FAIL");

    private final String value;

    PayStatus(String v) {
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