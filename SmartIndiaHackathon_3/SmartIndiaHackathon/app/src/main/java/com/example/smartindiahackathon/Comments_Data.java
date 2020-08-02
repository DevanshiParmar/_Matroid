package com.example.smartindiahackathon;

public class Comments_Data {
    private String comment;
    private String name_commentor;
    private String commentedBy;
    private String thumbnail_image_url;
    private String time;
    private String type;

    public Comments_Data(String comment, String name_commentor, String commentedBy, String thumbnail_image_url, String time, String type) {
        this.comment = comment;
        this.name_commentor = name_commentor;
        this.commentedBy = commentedBy;
        this.time = time;
        this.thumbnail_image_url = thumbnail_image_url;
        this.type = type;
    }

    public Comments_Data() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName_commentor() {
        return name_commentor;
    }

    public void setName_commentor(String comment) {
        this.name_commentor = comment;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getThumbnail_image_url() {
        return thumbnail_image_url;
    }

    public void setThumbnail_image_url(String thumbnail_image_url) {
        this.thumbnail_image_url = thumbnail_image_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
