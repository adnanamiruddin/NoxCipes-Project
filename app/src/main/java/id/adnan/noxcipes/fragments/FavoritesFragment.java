package id.adnan.noxcipes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.adnan.noxcipes.R;
import id.adnan.noxcipes.adapters.RecipesAdapter;
import id.adnan.noxcipes.config.DbConfig;
import id.adnan.noxcipes.models.Recipe;
import id.adnan.noxcipes.network.ApiService;
import id.adnan.noxcipes.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {
    private String apiKey = "9d2af32623msh23f8fd8a64896afp165e61jsn66cf318f7d48";
    private String apiHost = "the-vegan-recipes-db.p.rapidapi.com";
    private View favoritesLoadingView;
    private ImageView ivFavoritesNotFound;
    private RecyclerView rvFavoritesRecipes;
    private RecipesAdapter recipesAdapter;
    private List<Recipe> recipes;
    private ApiService service;
    private DbConfig dbConfig;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoritesLoadingView = view.findViewById(R.id.favorites_loading_view);
        ivFavoritesNotFound = view.findViewById(R.id.iv_favorites_not_found);
        rvFavoritesRecipes = view.findViewById(R.id.rv_favorites_recipes);

        service = RetrofitClient.getClient().create(ApiService.class);

        dbConfig = new DbConfig(requireActivity());
        preferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);

        Cursor cursor = dbConfig.getFavoritesByUserId(userId);
        ArrayList<Integer> favoritesRecipeId = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id"));
                favoritesRecipeId.add(recipeId);
            } while (cursor.moveToNext());
        }

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        favoritesLoadingView.setVisibility(View.VISIBLE);
        rvFavoritesRecipes.setVisibility(View.GONE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Recipe>> call = service.getRecipes(apiKey, apiHost);
                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                        if (response.isSuccessful()) {
                            recipes = response.body();

                            for (int i = 0; i < recipes.size(); i++) {
                                if (!favoritesRecipeId.contains(recipes.get(i).getId())) {
                                    recipes.remove(i);
                                    i--;
                                }
                            }

                            recipesAdapter = new RecipesAdapter(getParentFragmentManager(), recipes, userId);
                            rvFavoritesRecipes.setAdapter(recipesAdapter);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    favoritesLoadingView.setVisibility(View.GONE);
                                    if (recipesAdapter.getItemCount() == 0) {
                                        ivFavoritesNotFound.setVisibility(View.VISIBLE);
                                        rvFavoritesRecipes.setVisibility(View.GONE);
                                    } else {
                                        ivFavoritesNotFound.setVisibility(View.GONE);
                                        rvFavoritesRecipes.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                favoritesLoadingView.setVisibility(View.GONE);
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ErrorNoConnectionFragment())
                                        .commit();
                            }
                        });
                    }
                });
            }
        });
    }
}