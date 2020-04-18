package com.cn.demo.resp;

public class LocationResp {

    private String city;

    private String provice;

    private String isp;

    private String proviceAndCity;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getProviceAndCity() {
        return proviceAndCity = this.provice+"/"+this.provice;
    }
}
