package id.adnan.noxcipes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Map;
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

public class RecipeDetailFragment extends Fragment {
    private String apiKey = "93181b98d6mshf93653b16bf5cbcp1da356jsnf7b6c9615a0b";
    private String apiHost = "the-vegan-recipes-db.p.rapidapi.com";
    private int recipeId;
    private View recipeDetailLoadingView;
    private LinearLayout linearLayoutRecipeDetailMainView;
    private ImageView ivRecipeImage;
    private TextView tvRecipeTitle;
    private TextView tvRecipeDifficulty;
    private TextView tvRecipeDescription;
    private TextView tvRecipeTime;
    private TextView tvRecipePortion;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeMethod;
    private Recipe recipe;
    private ApiService service;
    private ImageView ivFavoriteButton;
    private boolean isFavorite = false;
    private DbConfig dbConfig;
    private SharedPreferences preferences;

    public RecipeDetailFragment(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeDetailLoadingView = view.findViewById(R.id.recipe_detail_loading_view);
        linearLayoutRecipeDetailMainView = view.findViewById(R.id.recipe_detail_main_view);
        ivRecipeImage = view.findViewById(R.id.iv_recipe_image);
        tvRecipeTitle = view.findViewById(R.id.tv_recipe_title);
        tvRecipeDifficulty = view.findViewById(R.id.tv_recipe_difficulty);
        tvRecipeDescription = view.findViewById(R.id.tv_recipe_description);
        tvRecipeTime = view.findViewById(R.id.tv_recipe_time);
        tvRecipePortion = view.findViewById(R.id.tv_recipe_portion);
        tvRecipeIngredients = view.findViewById(R.id.tv_recipe_ingredients);
        tvRecipeMethod = view.findViewById(R.id.tv_recipe_method);
        ivFavoriteButton = view.findViewById(R.id.iv_favorite_button);

        service = RetrofitClient.getClient().create(ApiService.class);

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        recipeDetailLoadingView.setVisibility(View.VISIBLE);
        linearLayoutRecipeDetailMainView.setVisibility(View.GONE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Recipe> call = service.getRecipeById(recipeId, apiKey, apiHost);
                call.enqueue(new Callback<Recipe>() {
                    @Override
                    public void onResponse(@NonNull Call<Recipe> call, @NonNull Response<Recipe> response) {
                        if (response.isSuccessful()) {
                            recipe = response.body();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    recipeDetailLoadingView.setVisibility(View.GONE);
                                    linearLayoutRecipeDetailMainView.setVisibility(View.VISIBLE);

                                    Picasso.get().load(recipe.getImage()).into(ivRecipeImage);
                                    tvRecipeTitle.setText(recipe.getTitle());
                                    tvRecipeDifficulty.setText(recipe.getDifficulty());
                                    tvRecipeDescription.setText(recipe.getDescription());
                                    tvRecipeTime.setText(recipe.getTime());
                                    tvRecipePortion.setText(recipe.getPortion());

                                    StringBuilder ingredients = new StringBuilder();
                                    for (String ingredient : recipe.getIngredients()) {
                                        ingredients.append("\u2022 ").append(ingredient).append("\n");
                                    }
                                    tvRecipeIngredients.setText(ingredients.toString().trim());

                                    StringBuilder method = new StringBuilder();
                                    for (Map<String, String> step : recipe.getMethod()) {
                                        for (Map.Entry<String, String> entry : step.entrySet()) {
                                            method.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n\n");
                                        }
                                    }
                                    tvRecipeMethod.setText(method.toString().trim());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Recipe> call, @NonNull Throwable t) {
                        Log.e("RecipeDetailFragment", "onFailure: " + t.getMessage());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                recipeDetailLoadingView.setVisibility(View.GONE);
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

        dbConfig = new DbConfig(requireActivity());
        preferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);
        isFavorite = dbConfig.isFavorite(userId, recipeId);

        updateFavoriteIcon();

        ivFavoriteButton.setOnClickListener(v -> {
            if (isFavorite) {
                dbConfig.deleteFavorite(userId, recipeId);
            } else {
                dbConfig.insertFavorite(userId, recipeId);
            }
            isFavorite = !isFavorite;
            updateFavoriteIcon();
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            ivFavoriteButton.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            ivFavoriteButton.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }
}
