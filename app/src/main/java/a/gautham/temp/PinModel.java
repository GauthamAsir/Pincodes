package a.gautham.temp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PinModel {

    @SerializedName("Status")
    private String status;

    @SerializedName("PostOffice")
    private List<PostOffice> postOffices;

    public String getStatus() {
        return status;
    }

    public List<PostOffice> getPostOffices() {
        return postOffices;
    }
}

class PostOffice {

    @SerializedName("State")
    private String state;

    @SerializedName("District")
    private String district;

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }
}