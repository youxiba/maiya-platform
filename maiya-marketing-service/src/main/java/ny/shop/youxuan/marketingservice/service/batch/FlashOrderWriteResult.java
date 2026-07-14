package ny.shop.youxuan.marketingservice.service.batch;

/**
 * 批量订单写入结果
 */
public class FlashOrderWriteResult {

    /** 成功写入数 */
    private int successCount;

    /** 重复（唯一键冲突）数 */
    private int duplicateCount;

    /** 失败数 */
    private int failCount;

    public FlashOrderWriteResult() {
        this.successCount = 0;
        this.duplicateCount = 0;
        this.failCount = 0;
    }

    public void addSuccess() { this.successCount++; }
    public void addDuplicate() { this.duplicateCount++; }
    public void addFail() { this.failCount++; }

    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getDuplicateCount() { return duplicateCount; }
    public void setDuplicateCount(int duplicateCount) { this.duplicateCount = duplicateCount; }
    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }
}
