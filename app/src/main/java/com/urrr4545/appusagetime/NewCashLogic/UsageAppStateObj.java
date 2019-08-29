package com.urrr4545.appusagetime.NewCashLogic;

public class UsageAppStateObj {

    public int index = -1;
    public String appPackageName = "";
    public String appName = "";
    public long appStartTime = 0L;
    public long appEndTime = 0L;
    public String appIcon = "";
    public int appCategroy = -1;

    public int getAppUseTime() {
        int useTime = (int)((this.appEndTime - this.appStartTime)/ 1000);
        if(useTime < 0){
            useTime = 0;
        }
        return useTime;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getAppStartTime() {
        return appStartTime;
    }

    public void setAppStartTime(Long appStartTime) {
        this.appStartTime = appStartTime;
    }

    public Long getAppEndTime() {
        return appEndTime;
    }

    public void setAppEndTime(Long appEndTime) {
        this.appEndTime = appEndTime;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public int getAppCategroy() {
        return appCategroy;
    }

    public void setAppCategroy(int appCategroy) {
        this.appCategroy = appCategroy;
    }
}

