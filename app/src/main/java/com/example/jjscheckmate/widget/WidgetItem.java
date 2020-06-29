package com.example.jjscheckmate.widget;

/**
 * WidgetRemoteViewsFactory 활용시 사용
 */
public class WidgetItem {
    private int id;
    private String title;

    public WidgetItem(int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getId(){ return this.id; }
    public void setId(int id){ this.id = id; }

    public String getTitle(){ return this.title; }
    public void setTitle(String title){ this.title = title; }

}
