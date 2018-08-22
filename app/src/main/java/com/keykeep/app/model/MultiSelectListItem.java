package com.keykeep.app.model;

/**
 * Created by aakashsharma on 13/9/17.
 */
public class MultiSelectListItem {

    private String id;
    private String listTitle;

    private boolean isSelected;

    public MultiSelectListItem(String id, String listTitle) {
        this.id = id;
        this.listTitle = listTitle;
    }

    public MultiSelectListItem() {
    }

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
