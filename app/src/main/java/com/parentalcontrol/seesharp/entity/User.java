package com.parentalcontrol.seesharp.entity;

import java.util.ArrayList;

public class User {
    public String accountId, fullName, userType;
    public ArrayList<String> connectedAccountId;

    public boolean appBlockingFlag;
    public ArrayList<String> blockedApplications;
    public ArrayList<String> installedApplications;

    public User() {
        this.connectedAccountId = new ArrayList<>();
    }

    public User(String accountId, String fullName, String userType) {
        this.accountId = accountId;
        this.fullName = fullName;
        this.userType = userType;
        this.connectedAccountId = new ArrayList<>();

        if (this.userType == "Child") {
            this.appBlockingFlag = false;
            this.blockedApplications = new ArrayList<>();
            this.installedApplications = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "accountId='" + accountId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userType='" + userType + '\'' +
                ", connectedAccountId=" + connectedAccountId +
                '}';
    }
}
