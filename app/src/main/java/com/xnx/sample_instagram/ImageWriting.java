package com.xnx.sample_instagram;

public class ImageWriting {

    public String imageUrl;
    public String description;
    public String userId;

    ImageWriting() {};

    ImageWriting(String imageUrl, String description, String userId) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.userId = userId;
    }

}
