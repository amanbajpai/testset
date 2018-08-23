package com.keykeep.app.model;

/**
 * Created by aakashsharma on 13/9/17.
 */
public class MultiSelectItem {

    private String id;
    private String listTitle;

    private boolean isSelected;

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
