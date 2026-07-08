package ny.shop.youxuan.common.enums;

public enum OrderSource {
    APP("APP"), XCX("XCX"), CONSOLE("CONSOLE");

    private final String value;

    OrderSource(String v) {
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