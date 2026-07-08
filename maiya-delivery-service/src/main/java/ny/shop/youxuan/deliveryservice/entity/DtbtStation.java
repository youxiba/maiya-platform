package ny.shop.youxuan.deliveryservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("dtbt_station")
public class DtbtStation {
    private Integer id;
    private String stationId;
    private String stationName;
    private String city;
    private String district;
    private Double lon;
    private Double lat;
    private String managerName;
    private String managerPhone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String v) {
        this.stationId = v;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String v) {
        this.stationName = v;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String v) {
        this.city = v;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String v) {
        this.district = v;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double v) {
        this.lon = v;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double v) {
        this.lat = v;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String v) {
        this.managerName = v;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String v) {
        this.managerPhone = v;
    }
}