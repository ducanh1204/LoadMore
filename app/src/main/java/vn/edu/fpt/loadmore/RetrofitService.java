package vn.edu.fpt.loadmore;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.edu.fpt.loadmore.model.GetModel;

public interface RetrofitService {

    @POST("services/rest/")
    @FormUrlEncoded
    Call<GetModel> postHttp(@Field("api_key") String api_key,
                            @Field("user_id") String user_id,
                            @Field("extras") String extras,
                            @Field("format") String format,
                            @Field("method") String method,
                            @Field("nojsoncallback") String nojsoncallback,
                            @Field("per_page") int per_page,
                            @Field("page") int page);
}
