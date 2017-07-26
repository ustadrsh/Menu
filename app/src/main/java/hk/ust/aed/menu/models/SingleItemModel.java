package hk.ust.aed.menu.models;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class SingleItemModel {

    private String name;
    private String url;
    private String description;
    private int imageResource;


    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url, int imageResource) {
        this.name = name;
        this.url = url;
        this.imageResource = imageResource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource(){
        return this.imageResource;
    }

    public void setImageResource(int imageResource){
        this.imageResource = imageResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
