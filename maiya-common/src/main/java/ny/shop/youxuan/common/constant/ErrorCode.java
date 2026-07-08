package ny.shop.youxuan.common.constant;

public interface ErrorCode {
    int SUCCESS = 200;
    int PARAM_ERROR = 1000;
    int USER_NOT_FOUND = 2000;
    int USER_EXISTS = 2001;
    int USER_NOT_REG = 2002;
    int USER_BLACKLIST = 2003;
    int PHONE_CODE_ERROR = 2004;
    int INVITE_CODE_ERROR = 2005;
    int TOKEN_INVALID = 2006;
    int ORDER_NOT_FOUND = 3000;
    int ORDER_STATUS_INVALID = 3001;
    int PAY_TYPE_ERROR = 4000;
    int PAY_FAILED = 4001;
    int GOODS_NOT_FOUND = 5000;
    int STOCK_NOT_ENOUGH = 5001;
    int FORBIDDEN = 6000;
}