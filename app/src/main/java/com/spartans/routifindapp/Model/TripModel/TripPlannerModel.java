package com.spartans.routifindapp.Model.TripModel;

public class TripPlannerModel
{
    private String title;
    private String content;

    public TripPlannerModel()
    {

    }

    public TripPlannerModel(String title, String content)
    {
        this.title=title;
        this.content=content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
