package com.njfu.yxcmc.common;

import android.content.Context;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务器传递的全局数据
 *
 * @author chenjm
 * @date 2014年5月28日
 */
public class GlobalField {

    private boolean isInit = false;

    // 根据用户权限的地市列表
    private JSONArray cityList;

    // 二级地市列表
    private JSONArray areaTwoLinkageArray;

    // 用户和部门列表
    private JSONArray staffInfoArray;

    // 收藏应用列表
    private List<JSONObject> favouriteList = new ArrayList<JSONObject>();

    // 用户信息
    private String staffId;
    private String staffName;
    private String staffPhone;
    private String staffEmail;
    private String staffHeadIcon; // 用户头像url
    private String staffDeptName; // 用户部门名称
    private String deviceId;
    private String lastLoginTime; // 最后登录时间

    private String userId;// 后台系统userId

    private String regionCode;// 用户归属区域代码
    private String regionName;// 用户归属区域名称

    private String aboutAppContent; // 关于App的内容

    private JSONArray pageGuideConfigJsonArray;// 引导页面配置
    private JSONArray afreshGuideMoudle;// 引导页面配置

    private String patternLock = "";//手势码

    private String rangeId;
    private String jobId;
    private String jobName;

    private String simStr;

    private String provCode; // 省编码
    private String provName;
    private String cityCode; // 市编码
    private String countyCode; // 区县编码
    private String villageCode; // 支局编码
    private String chnlId; // 渠道编码

    private boolean isLeader = false;
    private boolean isNewHome = false;
    private boolean redirectFlag = false;
    private boolean isFromNewHome = false;

    private boolean hasNotice = false;
    private String noticeTitle;
    private String noticeContent;
    private String noticeTime;

    private int encrytype = 0;// 加密类型
    private String encrykey = "";// 加密密码

    private int topColor = 0xFF2999FC; //页面头部皮肤

    // 用户列表
    private JSONArray staffArray;

    private JSONArray dimArrayData;

    private String firstDay, latestDay;
    private String firstMon, latestMon;

    private boolean isHomePage = false;

    // 是否是插件体系启动
    private boolean isLaunchedFromPlugin = false;

    private JSONArray questionnaireArray;

    // 竞争信息上报-筛选调教
    private JSONArray crcTypes;
    private JSONArray criTypes;

    private Map<String,String> commonMapParams;
    private JSONObject commonJsonParams;

    public Map<String, String> getCommonMapParams() {
        return commonMapParams;
    }

    public void setCommonMapParams(Map<String, String> commonMapParams) {
        this.commonMapParams = commonMapParams;
    }

    public JSONObject getCommonJsonParams() {
        return commonJsonParams;
    }

    public void setCommonJsonParams(JSONObject commonJsonParams) {
        this.commonJsonParams = commonJsonParams;
    }

    public JSONArray getCrcTypes() {
        return crcTypes;
    }

    public void setCrcTypes(JSONArray crcTypes) {
        this.crcTypes = crcTypes;
    }

    public JSONArray getCriTypes() {
        return criTypes;
    }

    public void setCriTypes(JSONArray criTypes) {
        this.criTypes = criTypes;
    }

    public JSONArray getStaffInfoArray() {
        return staffInfoArray;
    }

    public void setStaffInfoArray(JSONArray staffInfoArray) {
        this.staffInfoArray = staffInfoArray;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public JSONArray getCityList() {
        return cityList;
    }

    public void setCityList(JSONArray cityList) {
        this.cityList = cityList;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getSimStr() {
        return simStr;
    }

    public void setSimStr(String simStr) {
        this.simStr = simStr;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public JSONArray getStaffArray() {
        return staffArray;
    }

    public void setStaffArray(JSONArray staffArray) {
        this.staffArray = staffArray;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getChnlId() {
        return chnlId;
    }

    public void setChnlId(String chnlId) {
        this.chnlId = chnlId;
    }

    public JSONArray getDimArrayData() {
        return dimArrayData;
    }

    public void setDimArrayData(JSONArray dimArrayData) {
        this.dimArrayData = dimArrayData;
    }

    public List<JSONObject> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<JSONObject> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public JSONArray getAreaTwoLinkageArray() {
        return areaTwoLinkageArray;
    }

    public void setAreaTwoLinkageArray(JSONArray areaTwoLinkageArray) {
        this.areaTwoLinkageArray = areaTwoLinkageArray;

    }

    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getLatestDay() {
        return latestDay;
    }

    public void setLatestDay(String latestDay) {
        this.latestDay = latestDay;
    }

    public String getFirstMon() {
        return firstMon;
    }

    public void setFirstMon(String firstMon) {
        this.firstMon = firstMon;
    }

    public String getLatestMon() {
        return latestMon;
    }

    public void setLatestMon(String latestMon) {
        this.latestMon = latestMon;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean isInit) {
        this.isInit = isInit;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public boolean isNewHome() {
        return isNewHome;
    }

    public void setNewHome(boolean isNewHome) {
        this.isNewHome = isNewHome;
    }

    public boolean isRedirectFlag() {
        return redirectFlag;
    }

    public void setRedirectFlag(boolean redirectFlag) {
        this.redirectFlag = redirectFlag;
    }

    public boolean isFromNewHome() {
        return isFromNewHome;
    }

    public void setFromNewHome(boolean isFromNewHome) {
        this.isFromNewHome = isFromNewHome;
    }

    public boolean isHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(boolean hasNotice) {
        this.hasNotice = hasNotice;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public int getEncrytype(Context context) {
        if (encrykey.length() == 0) {
            CmssSharedPreferences mPrefs = CmssSharedPreferences.getInstance(context);
            encrytype = mPrefs.getInt("encrytype", 0);
            encrykey = mPrefs.getString("encrykey", "");
        }
        return encrytype;
    }

    public void setEncrytype(int encrytype, Context context) {
        CmssSharedPreferences mPrefs = CmssSharedPreferences.getInstance(context);
        mPrefs.saveInt("encrytype", encrytype);
        this.encrytype = encrytype;
    }

    public String getEncrykey(Context context) {
        if (encrykey.length() == 0) {
            CmssSharedPreferences mPrefs = CmssSharedPreferences.getInstance(context);
            encrytype = mPrefs.getInt("encrytype", 0);
            encrykey = mPrefs.getString("encrykey", "");
        }
        return encrykey;
    }

    public void setEncrykey(String encrykey, Context context) {
        CmssSharedPreferences mPrefs = CmssSharedPreferences.getInstance(context);
        mPrefs.saveString("encrykey", encrykey);
        this.encrykey = encrykey;
    }

    public int getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = Color.parseColor(topColor);
    }

    public boolean isHomePage() {
        return isHomePage;
    }

    public void setHomePage(boolean isHomePage) {
        this.isHomePage = isHomePage;
    }

    public boolean isLaunchedFromPlugin() {
        return isLaunchedFromPlugin;
    }

    public void setLaunchedFromPlugin(boolean launchedFromPlugin) {
        isLaunchedFromPlugin = launchedFromPlugin;
    }

    public JSONArray getQuestionnaireArray() {
        return questionnaireArray;
    }

    public void setQuestionnaireArray(JSONArray questionnaireArray) {
        this.questionnaireArray = questionnaireArray;
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

    public String getAboutAppContent() {
        return aboutAppContent;
    }

    public void setAboutAppContent(String aboutAppContent) {
        this.aboutAppContent = aboutAppContent;
    }

    public String getStaffDeptName() {
        return staffDeptName;
    }

    public void setStaffDeptName(String staffDeptName) {
        this.staffDeptName = staffDeptName;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public JSONArray getPageGuideConfigJsonArray() {
        return pageGuideConfigJsonArray;
    }

    public void setPageGuideConfigJsonArray(JSONArray pageGuideConfigJsonArray) {
        this.pageGuideConfigJsonArray = pageGuideConfigJsonArray;
    }

    public JSONArray getAfreshGuideMoudle() {
        return afreshGuideMoudle;
    }

    public void setAfreshGuideMoudle(JSONArray afreshGuideMoudle) {
        this.afreshGuideMoudle = afreshGuideMoudle;
    }

    public String getPatternLock() {
        return patternLock == null ? "" : patternLock;
    }

    public void setPatternLock(String patternLock) {
        this.patternLock = patternLock;
    }
}
