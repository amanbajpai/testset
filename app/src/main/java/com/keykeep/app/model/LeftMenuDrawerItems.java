package com.keykeep.app.model;

public class LeftMenuDrawerItems {


    private String title;
    private int selectedIcon, deSelectedIcon;

    private boolean menuIsSelected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMenuIsSelected() {
        return menuIsSelected;
    }

    public void setMenuIsSelected(boolean menuIsSelected) {
        this.menuIsSelected = menuIsSelected;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getDeSelectedIcon() {
        return deSelectedIcon;
    }

    public void setDeSelectedIcon(int deSelectedIcon) {
        this.deSelectedIcon = deSelectedIcon;
    }
}
