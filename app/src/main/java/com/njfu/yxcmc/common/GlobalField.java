package com.njfu.yxcmc.common;

import org.json.JSONObject;

import java.util.Map;

public class GlobalField {

    private String staffId;
    private String staffName;
    private String staffPhone;
    private String staffEmail;
    private String staffHeadIcon;
    private String deviceId;
    private String lastLoginTime; // 最后登录时间

    private Map<String, String> commonMapParams;
    private JSONObject commonJsonParams;

    public JSONObject getCommonJsonParams() {
        return commonJsonParams;
    }

    public void setCommonJsonParams(JSONObject commonJsonParams) {
        this.commonJsonParams = commonJsonParams;
    }

    public Map<String, String> getCommonMapParams() {
        return commonMapParams;
    }

    public void setCommonMapParams(Map<String, String> commonMapParams) {
        this.commonMapParams = commonMapParams;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffHeadIcon() {
        return staffHeadIcon;
    }

    public void setStaffHeadIcon(String staffHeadIcon) {
        this.staffHeadIcon = staffHeadIcon;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
