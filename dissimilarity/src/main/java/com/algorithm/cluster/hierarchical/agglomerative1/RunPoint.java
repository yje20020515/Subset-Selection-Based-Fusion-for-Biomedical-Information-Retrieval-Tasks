package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;

import java.util.HashMap;

public class RunPoint {
    private String systemName;
    private HashMap<HashMapDocKey, HashMapDocValue> dimensionList;

    public RunPoint() {
    }

    public RunPoint(String systemName, HashMap<HashMapDocKey, HashMapDocValue> dimensionList) {
        this.systemName = systemName;
        this.dimensionList = dimensionList;
    }

    public RunPoint(String systemName) {
        this.systemName = systemName;
        dimensionList = new HashMap<>();
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public HashMap<HashMapDocKey, HashMapDocValue> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(HashMap<HashMapDocKey, HashMapDocValue> dimensionList) {
        this.dimensionList = dimensionList;
    }
}
