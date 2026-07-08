package ny.shop.youxuan.common.exception;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int code;

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BizException(String msg) {
        super(msg);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }
}