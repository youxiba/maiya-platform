package ny.shop.youxuan.marketingservice.service;

import ny.shop.youxuan.marketingservice.entity.PrizeInfo;

/** 抽奖结果 */
public class PrizeResult {
    private boolean win;
    private String prizeName;
    private String prizeImage;
    private int prizeType; // 0=谢谢惠顾 1=优惠券 2=实物

    public PrizeResult() {}
    public PrizeResult(PrizeInfo prize) {
        this.prizeName = prize.getPrizeName();
        this.prizeImage = prize.getPrizeImage();
        this.prizeType = prize.getPrizeType();
        this.win = prize.getPrizeType() != 0;
    }

    public boolean isWin() { return win; } public void setWin(boolean v) { this.win=v; }
    public String getPrizeName() { return prizeName; } public void setPrizeName(String v) { this.prizeName=v; }
    public String getPrizeImage() { return prizeImage; } public void setPrizeImage(String v) { this.prizeImage=v; }
    public int getPrizeType() { return prizeType; } public void setPrizeType(int v) { this.prizeType=v; }
}
