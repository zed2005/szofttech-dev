package com.szofttech.backbone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StringToFunctionList {
    private final Map<String, Consumer<List<Object>>> stringToFuncMap;
    private static StringToFunctionList instance;
    
    private StringToFunctionList() {
        stringToFuncMap = new HashMap<>();
    }

    public static StringToFunctionList getInsance() {
        if(instance == null) {
            instance = new StringToFunctionList();
        }
        return instance;
    }

    public void addToList(String name, Consumer<List<Object>> function) {
        stringToFuncMap.put(name, function);
    } 

    public Consumer<List<Object>> getFuncFromString(String name) {
        return stringToFuncMap.get(name);
    }
}
