package a.gautham.temp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String PINCODE_BASE_URL = "http://www.postalpincode.in/";

    public static Retrofit build(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(PINCODE_BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

}
