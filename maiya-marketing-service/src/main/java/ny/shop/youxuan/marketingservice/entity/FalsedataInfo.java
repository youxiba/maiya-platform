package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("falsedata_info")
public class FalsedataInfo {
    private Integer id;
    private String activityId;
    private String usersName;
    private String prizesName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String v) {
        this.activityId = v;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String v) {
        this.usersName = v;
    }

    public String getPrizesName() {
        return prizesName;
    }

    public void setPrizesName(String v) {
        this.prizesName = v;
    }
}