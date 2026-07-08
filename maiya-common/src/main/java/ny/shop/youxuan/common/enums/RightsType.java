package ny.shop.youxuan.common.enums;

public enum RightsType {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN"), ROLE_SUPERADMIN("ROLE_SUPERADMIN");

    private final String value;

    RightsType(String v) {
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