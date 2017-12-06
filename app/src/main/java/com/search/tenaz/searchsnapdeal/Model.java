package com.search.tenaz.searchsnapdeal;

/**
 * Created by tenaz on 12/5/17.
 */

public class Model {

    String description;

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }

    boolean isFooter = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String imageUrl;
    String price;

}
