package com.hudutech.mymanjeri.models;

import java.util.List;

public class CategoryMenu implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String menuCategory;
    private String menuName;
    private Class<?> gotoGlass;
    private String optionalUrl;
    private int iconResId;

    private List<CategoryMenu> menuList;

    public CategoryMenu() {
    }

    public CategoryMenu(String menuCategory,String menuName,  Class<?> gotoGlass, int iconResId) {
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.gotoGlass = gotoGlass;
        this.iconResId = iconResId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public Class<?> getGotoGlass() {
        return gotoGlass;
    }

    public void setGotoGlass(Class<?> gotoGlass) {
        this.gotoGlass = gotoGlass;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public List<CategoryMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<CategoryMenu> menuList) {
        this.menuList = menuList;
    }

    public String getOptionalUrl() {
        return optionalUrl;
    }

    public void setOptionalUrl(String optionalUrl) {
        this.optionalUrl = optionalUrl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
