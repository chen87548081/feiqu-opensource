package com.feiqu.system.pojo.response.baidu;

/**
 * @author cwd
 * @create 2017-10-13:56
 **/
public class BaiduMapContent {
    private MapAddressDetail address_detail;
    private String address;
    private MapPoint point;

    public MapAddressDetail getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(MapAddressDetail address_detail) {
        this.address_detail = address_detail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MapPoint getPoint() {
        return point;
    }

    public void setPoint(MapPoint point) {
        this.point = point;
    }
}
