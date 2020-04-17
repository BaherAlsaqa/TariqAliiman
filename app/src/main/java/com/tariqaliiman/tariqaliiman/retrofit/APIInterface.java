package com.tariqaliiman.tariqaliiman.retrofit;

import com.tariqaliiman.tariqaliiman.Models.FirstLevel.FirstLevelBody;
import com.tariqaliiman.tariqaliiman.Models.LevelHadeth.LevelHadethBody;
import com.tariqaliiman.tariqaliiman.Models.books.BooksBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by baher on 09/01/17.
 */

public interface APIInterface {

    @GET("books")
    Call<BooksBody> getAllBooks(@Query("page") int nextPage);

    @GET("book_first_levels/{book_id}")
    Call<FirstLevelBody> getFirstLevel(@Path("book_id") int bookId,
                                       @Query("page") int nextPage);

    @GET("sub_levels/{book_id}")
    Call<FirstLevelBody> getSubLevel(@Path("book_id") int bookId,
                                       @Query("page") int nextPage);

    @GET("level_hadiths/{level_id}")
    Call<LevelHadethBody> getLevelHadiths(@Path("level_id") int levelId,
                                          @Query("page") int nextPage);

    //TODO //////////////POST///////////////////////////////////////////////////////////////////

    /*@FormUrlEncoded
    @POST("users/user")
    Call<UserResp> createUser(@Header("API-KEY") String apiKey,
                              @Header("API-USERNAME") String apiUsername,
                              @Header("API-PASSWORD") String apiPassword,
                              @Header("Content-Type") String contentType,
                              @Field("token") String token);*/
}
