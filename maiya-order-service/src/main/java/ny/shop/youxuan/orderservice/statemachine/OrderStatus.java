package ny.shop.youxuan.orderservice.statemachine;

public enum OrderStatus {
    ORDER(0), WAIT_GROUP(1), PAID(2), PAID_IN_SERV(3), DELAY(4), WAIT_REVIEWS(5), FINISH(6), SUCCESS(7),
    REFUND(8), REFUND_ING(9), REFUND_FINISH(10), REFUND_FAIL(11), CLOSE(12), USELESS(13), REFUND_SUCCESS(14),
    MCH_REFUND(15);

    private final int code;

    OrderStatus(int c) {
        this.code = c;
    }

    public int getCode() {
        return code;
    }
}