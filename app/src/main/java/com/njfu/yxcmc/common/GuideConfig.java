package com.njfu.yxcmc.common;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.njfu.yxcmc.base.GloabApp;

public class GuideConfig {
    private static GuideConfig mInstance = null;
    private CmssSharedPreferences mCmssSharedPreferences = null;
    private String frontCodeForPageCode = "HasPageGuideUsed";

    private GuideConfig() {
        this.mCmssSharedPreferences = GloabApp.getInstance().getPrefs();
    }

    public static GuideConfig getInstance() {
        if (mInstance == null) mInstance = new GuideConfig();
        return mInstance;
    }


    public boolean isNeedShowGuidePage(String pageGuideCode) {
        if (TextUtils.isEmpty(pageGuideCode)) return false;
        return !mCmssSharedPreferences.getBoolean(frontCodeForPageCode + pageGuideCode, false);
    }

    public void disableGuide(String pageGuideCode) {
        if (TextUtils.isEmpty(pageGuideCode)) return;
        mCmssSharedPreferences.saveBoolean(frontCodeForPageCode + pageGuideCode, true);
    }

    public void reEnableGuide(String pageGuideCode) {
        if (TextUtils.isEmpty(pageGuideCode)) return;
        mCmssSharedPreferences.removeBoolean(frontCodeForPageCode + pageGuideCode);
    }

    public enum PageCodeConfig {
//        HOME("queryHomeInfo", FragmentNormalHome.class),// 首页
//        KPI_PANEL_BY_TYPE("queryKpisByType", FragmentKpiPanelKpiList.class),// 指标看板-分类列表
//        KPI_PANEL_HOME("queryKpiInfo", FragmentKpiPanelHome.class),// 指标看板
//        KPI_DETAIL("kpiDetail", FragmentKpiPanelKpiDetail.class),//指标详情
//        REPORT_PANEL_HOME("queryReportInfo", FragmentReportPanelHome.class),// 报表中心
//        REPORT_DETAIL("reportDetail", FragmentReportDetail.class),//报表详情
//        KPI_REF_ANALYSIS_QUERY_ANALYSIS_DETAIL("kpiRefAnalysis", FragmentKpiPanelKpiRelationAnalysisDetail.class),//指标关联分析
//        MY_FAVORITE_HOME("queryMyFavorite", FragmentCollection.class),// 我的收藏首页
//        MESSAGE_HOME("queryMessage", FragmentMessage.class),// 消息首页
//        ;

        KPI_DETAIL("kpiDetail", null);

        private String pageGuideCode;
        private Class pageClass;

        PageCodeConfig(@NonNull String pageGuideCode, @NonNull Class pageClass) {
            this.pageGuideCode = pageGuideCode;
            this.pageClass = pageClass;
        }

        public String getPageGuideCode() {
            return pageGuideCode;
        }

        public Class getPageClass() {
            return pageClass;
        }
    }
}
