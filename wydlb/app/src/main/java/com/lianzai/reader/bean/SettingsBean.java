package com.lianzai.reader.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SettingsBean implements Serializable{
    private Map<String,Boolean> map=new HashMap<>();

    public Map<String, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<String, Boolean> map) {
        this.map = map;
    }
}
