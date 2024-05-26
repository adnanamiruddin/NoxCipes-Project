package id.adnan.noxcipes.network;

import java.util.List;

import id.adnan.noxcipes.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/")
    Call<List<Recipe>> getRecipes(
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost
    );

    @GET("/{id}")
    Call<Recipe> getRecipeById(
            @Path("id") int recipeId,
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost
    );

}
