package com.qyq.springbootapi.result;

import java.util.List;

//百度地图第三方API返回数据封装
public class BaiduResult {
    private Integer status;
    private String message;
    private List<Location> results;

    public BaiduResult(Integer status, String message, List<Location> results) {
        this.status = status;
        this.message = message;
        this.results = results;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Location> getResults() {
        return results;
    }

    public void setResults(List<Location> results) {
        this.results = results;
    }
}
