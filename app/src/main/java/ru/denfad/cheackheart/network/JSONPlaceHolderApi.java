package ru.denfad.cheackheart.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.denfad.cheackheart.models.ClientDiagnose;
import ru.denfad.cheackheart.models.Diagnose;
import ru.denfad.cheackheart.models.User;

public interface JSONPlaceHolderApi {

    @POST("/users/auth")
    public Call<User> authUser(@Query("login") String login, @Query("password") String password);

    @GET("/users/diagnose/{id}")
    public Call<List<ClientDiagnose>> getDiagnoses(@Path("id") Integer userId);

    @PUT("/users/diagnose/{id}")
    public Call<String> saveDiagnoses(@Path("id") int userId, @Body Diagnose diagnose, @Query("year") int year, @Query("month") int month);

    @PUT("/users/update")
    public Call<String> updateUser(@Body User user);
}
