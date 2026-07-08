package ny.shop.youxuan.common.enums;

public enum PayType {
    MY_PAY("MY_PAY"), WX_PAY("WX_PAY"), WX_XCX_PAY("WX_XCX_PAY"),
    ALI_PAY("ALI_PAY"), COUPON_PAY("COUPON_PAY"), POINTS_PAY("POINTS_PAY");

    private final String value;

    PayType(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String t) {
        for (PayType p : values()) {
            if (p.name().equals(t))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}