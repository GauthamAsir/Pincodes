package a.gautham.temp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PincodeService {
    @GET("/api/pincode/{pin}")
    public Call<PinModel> getDetails(@Path("pin") String pin);
}
