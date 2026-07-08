package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("flash_sale_mch")
public class FlashSaleMch {
    private Integer id;
    private String infoId;
    private String fsId;
    private String mid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String v) {
        this.infoId = v;
    }

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String v) {
        this.fsId = v;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }
}