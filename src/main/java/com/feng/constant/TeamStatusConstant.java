package com.feng.constant;
public enum TeamStatusConstant {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");

    public static TeamStatusConstant getTeamKey(Integer status) {

        if (status==null){
            return null;
        }
        TeamStatusConstant[] values = TeamStatusConstant.values();
        for (TeamStatusConstant value : values) {
            if (value.getKey()==status){
                return value;
            }
        }
        return null;
    }
    private int key;

    private String value;

    TeamStatusConstant(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
