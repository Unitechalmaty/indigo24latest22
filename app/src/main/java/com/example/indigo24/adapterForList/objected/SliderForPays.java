package com.example.indigo24.adapterForList.objected;

import android.graphics.drawable.Drawable;

import org.json.JSONObject;

public class SliderForPays {
    public String name;
    public String icon;
    public String defoultUrl;
    public int id;
    public JSONObject dataForPay;

    public SliderForPays(String _name, String _icon, String _defoultUrl, int _id, JSONObject _dataForPay) {
        name = _name;
        icon = _icon;
        defoultUrl = _defoultUrl;
        id = _id;
        dataForPay =_dataForPay;
    }
}
