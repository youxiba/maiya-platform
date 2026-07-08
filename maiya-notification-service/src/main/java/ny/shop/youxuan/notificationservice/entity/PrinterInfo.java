package ny.shop.youxuan.notificationservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("printer_info")
public class PrinterInfo {
    private Integer id;
    private String infoId;
    private String uid;
    private String mid;
    private String name;
    private String machineCode;
    private String printerId;
    private String apiKey;
    private String apiSecret;
    private Boolean enable;
    private Boolean auto;
    private Boolean mchPaperFlag;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        this.name = v;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String v) {
        this.machineCode = v;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String v) {
        this.printerId = v;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String v) {
        this.apiKey = v;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String v) {
        this.apiSecret = v;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean v) {
        this.enable = v;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean v) {
        this.auto = v;
    }

    public Boolean getMchPaperFlag() {
        return mchPaperFlag;
    }

    public void setMchPaperFlag(Boolean v) {
        this.mchPaperFlag = v;
    }
}