package hk.ust.aed.menu.models;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class SingleItemModel {

    private String name;
    private String pictureName;
    private int defaultImageResource;

    public void setName(String name) {
        this.name = name;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public void setDefaultImageResource(int defaultImageResource) {
        this.defaultImageResource = defaultImageResource;
    }

    public String getName() {
        return name;
    }

    public String getPictureName() {
        return pictureName;
    }

    public int getDefaultImageResource() {
        return defaultImageResource;
    }

    public SingleItemModel(String name, String pictureName, int imageResource) {
        this.name = name;
        this.pictureName = pictureName;
        this.defaultImageResource = imageResource;

    }
}