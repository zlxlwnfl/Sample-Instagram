package com.xnx.sample_instagram;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostingDTO implements Serializable {
    private String imageUrl;
    private String description;
    private String userId;
    private Date createdAt;
    private int good;

    public PostingDTO(String imageUrl, String description, String userId, Date createdAt, int good) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
        this.good = good;
    }

    public Map<String, Object> getPostingDTOMap(){
        Map<String, Object> docData = new HashMap<>();

        docData.put("imageUrl",imageUrl);
        docData.put("description",description);
        docData.put("userId",userId);
        docData.put("createdAt",createdAt);
        docData.put("good",good);

        return  docData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }
}
