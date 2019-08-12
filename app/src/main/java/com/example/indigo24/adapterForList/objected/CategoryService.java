package com.example.indigo24.adapterForList.objected;

import org.json.JSONArray;

public class CategoryService {
    public String name;
    public String icon;
    public JSONArray servicess;


    public CategoryService(String _name, String _icon, JSONArray _servicess) {
        name = _name;
        icon = _icon;
        servicess = _servicess;
    }
}
