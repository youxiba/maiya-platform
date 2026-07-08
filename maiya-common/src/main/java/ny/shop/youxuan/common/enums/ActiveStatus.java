package ny.shop.youxuan.common.enums;

public enum ActiveStatus {
    CREATE("CREATE"), ING("ING"), CANCEL("CANCEL"), END("END"), APPLY("APPLY"), RATIFY("RATIFY"), UNRATIFY("UNRATIFY"),
    USELESS("USELESS");

    private final String value;

    ActiveStatus(String v) {
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