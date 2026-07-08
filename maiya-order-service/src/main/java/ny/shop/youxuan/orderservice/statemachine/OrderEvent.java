package ny.shop.youxuan.orderservice.statemachine;

public enum OrderEvent {
    PAY, TIMEOUT, CANCEL, GROUP_SUCCESS, GROUP_FAIL, DELIVER, CONFIRM, REVIEW, SETTLE, APPLY_REFUND, REFUND;
}