package com.example.indigo24.adapterForList.objected;

import org.json.JSONObject;

public class InpService {
    public String name;
    public boolean qr;
    public JSONObject autoPay;
    public String defoultValue;
    public String type;
    public String placeHolder;
    public String mask;

    public  InpService(String _name, boolean _qr, JSONObject _autoPay, String _defoultValue, String _type, String _placeHolder, String _mask) {
        name = _name;
        qr = _qr;
        autoPay =_autoPay;
        defoultValue = _defoultValue;
        type = _type;
        placeHolder = _placeHolder;
        mask = _mask;
    }
}
