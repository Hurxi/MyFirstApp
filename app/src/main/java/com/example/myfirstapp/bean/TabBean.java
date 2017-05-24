package com.example.myfirstapp.bean;

/**
 * Created by 若希 on 2017/4/28.
 */

public class TabBean {
    private String title;
    private int selected;
    private int unSelected;
    private int type;//种类  包含图文 单图片  0-没有文字  1-图文
    private int index;//下标

    public TabBean(String title, int selected, int unSelected, int type) {
        this.title = title;
        this.selected = selected;
        this.unSelected = unSelected;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getUnSelected() {
        return unSelected;
    }

    public void setUnSelected(int unSelected) {
        this.unSelected = unSelected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
