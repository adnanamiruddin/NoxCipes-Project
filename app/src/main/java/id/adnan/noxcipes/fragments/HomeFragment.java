package id.adnan.noxcipes.fragments;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

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

public class HomeFragment extends Fragment {
    private String apiKey = "9d2af32623msh23f8fd8a64896afp165e61jsn66cf318f7d48";
    private String apiHost = "the-vegan-recipes-db.p.rapidapi.com";
    private View homeLoadingView;
    private TextView tvHomeFullName;
    private RecyclerView rvHomeRecipes;
    private RecipesAdapter recipesAdapter;
    private List<Recipe> recipes;
    private ApiService service;
    private SharedPreferences preferences;
    private DbConfig dbConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeLoadingView = view.findViewById(R.id.home_loading_view);
        tvHomeFullName = view.findViewById(R.id.tv_home_full_name);
        rvHomeRecipes = view.findViewById(R.id.rv_home_recipes);

        service = RetrofitClient.getClient().create(ApiService.class);
        preferences = requireActivity().getSharedPreferences("user_pref", requireActivity().MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);

        dbConfig = new DbConfig(requireActivity());
        Cursor cursor = dbConfig.getUserDataById(userId);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                tvHomeFullName.setText("Hi, " + name + " !");
            } while (cursor.moveToNext());
        }

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ErrorNoConnectionFragment errorNoConnectionFragment = new ErrorNoConnectionFragment();

        homeLoadingView.setVisibility(View.VISIBLE);
        rvHomeRecipes.setVisibility(View.GONE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Recipe>> call = service.getRecipes(apiKey, apiHost);
                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                        if (response.isSuccessful()) {
                            recipes = response.body();
                            recipesAdapter = new RecipesAdapter(getParentFragmentManager(), recipes, userId);
                            rvHomeRecipes.setAdapter(recipesAdapter);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    homeLoadingView.setVisibility(View.GONE);
                                    rvHomeRecipes.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                homeLoadingView.setVisibility(View.GONE);
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, errorNoConnectionFragment)
                                        .commit();
                            }
                        });
                    }
                });
            }
        });
    }
}