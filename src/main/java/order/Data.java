package order;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("_id")
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}