package com.example.indigo24.adapterForList.objected;

import org.json.JSONObject;

public class myPay {
    public String name;
    public String icon;
    public String defoultUrl;
    public int id;
    public JSONObject dataForPay;
    public String summ;
    public String defoultValue;
    public Boolean showParametrs;

    public myPay(String _name, String _icon, String _defoultUrl, int _id, JSONObject _dataForPay, String _summ, String _defoultValue, Boolean _showParametrs) {
        name = _name;
        icon = _icon;
        defoultUrl = _defoultUrl;
        id = _id;
        dataForPay =_dataForPay;
        summ = _summ;
        defoultValue = _defoultValue;
        showParametrs = _showParametrs;
    }
}
