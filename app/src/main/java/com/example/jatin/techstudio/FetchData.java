package com.example.jatin.techstudio;

public class FetchData {

    private int id;
    private int categoryid;
    private String title;
    private String content;
    private String image;

    public FetchData(int id, int categoryid, String title, String content, String image) {
        this.id = id;
        this.categoryid = categoryid;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
