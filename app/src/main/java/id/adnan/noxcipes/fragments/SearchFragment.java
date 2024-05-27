package id.adnan.noxcipes.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import id.adnan.noxcipes.R;
import id.adnan.noxcipes.adapters.SearchRecipesAdapter;
import id.adnan.noxcipes.models.Recipe;
import id.adnan.noxcipes.network.ApiService;
import id.adnan.noxcipes.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private String apiKey = "181a8b5e4fmsh489e90afab480d3p10f137jsn76a17b77b8f5";
    private String apiHost = "the-vegan-recipes-db.p.rapidapi.com";
    private EditText etSearchRecipes;
    private ImageView ivNotFound;
    private View searchLoadingView;
    private RecyclerView rvSearchRecipes;
    private List<Recipe> recipesFull;
    private SearchRecipesAdapter searchRecipesAdapter;
    private ApiService service;
    private boolean isSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearchRecipes = view.findViewById(R.id.et_search_recipes);
        ivNotFound = view.findViewById(R.id.iv_search_not_found);
        searchLoadingView = view.findViewById(R.id.search_loading_view);
        rvSearchRecipes = view.findViewById(R.id.rv_search_recipes);

        service = RetrofitClient.getClient().create(ApiService.class);

        ErrorNoConnectionFragment errorNoConnectionFragment = new ErrorNoConnectionFragment();
        Handler handler = new Handler(Looper.getMainLooper());

        Call<List<Recipe>> call = service.getRecipes(apiKey, apiHost);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipesFull = response.body();
                    searchRecipesAdapter = new SearchRecipesAdapter(getParentFragmentManager(), new ArrayList<>(), recipesFull);
                    rvSearchRecipes.setAdapter(searchRecipesAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, errorNoConnectionFragment)
                        .commit();
            }
        });

        etSearchRecipes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchLoadingView.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchRecipesAdapter.searchRecipe(s.toString());
                        searchLoadingView.setVisibility(View.GONE);
                        isSearch = !s.toString().isEmpty();
                        if (searchRecipesAdapter.getItemCount() == 0 && isSearch) {
                            ivNotFound.setVisibility(View.VISIBLE);
                        } else {
                            ivNotFound.setVisibility(View.GONE);
                        }
                    }
                }, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}