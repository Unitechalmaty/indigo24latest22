package com.example.indigo24.adapterForList.objected;

public class Contacts {
    public  String name;
    public String phone;
    public String colorBack;
    public String colorText;
    public String avatar;
    public int image;
    public boolean box;
    public String defoultImage;
    public int type;

    public Contacts(String _describe, String _phone, int _image, boolean _box, String _colorBack, String _colorText, String _avatar, String _defoultImage, int _type) {
        name = _describe;
        phone = _phone;
        image = _image;
        box = _box;
        colorBack = _colorBack;
        colorText = _colorText;
        avatar = _avatar;
        defoultImage = _defoultImage;
        type = _type;
    }

}
