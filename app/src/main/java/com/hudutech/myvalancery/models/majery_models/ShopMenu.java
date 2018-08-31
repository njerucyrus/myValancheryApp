package com.hudutech.myvalancery.models.majery_models;

public class ShopMenu {
    private String menuTitle;
    private int resId;
    private Class<?> gotoClass;

    public ShopMenu(String menuTitle, int resId, Class<?> gotoClass) {
        this.menuTitle = menuTitle;
        this.resId = resId;
        this.gotoClass = gotoClass;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Class<?> getGotoClass() {
        return gotoClass;
    }

    public void setGotoClass(Class<?> gotoClass) {
        this.gotoClass = gotoClass;
    }
}
