package id.adnan.noxcipes.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.adnan.noxcipes.R;
import id.adnan.noxcipes.models.Recipe;
import id.adnan.noxcipes.network.ApiService;
import id.adnan.noxcipes.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorNoConnectionFragment extends Fragment {
    private String apiKey = "9d2af32623msh23f8fd8a64896afp165e61jsn66cf318f7d48";
    private String apiHost = "the-vegan-recipes-db.p.rapidapi.com";
    private View erorNoConnectionLoadingView;
    private Button btnRetry;
    private ApiService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error_no_connection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        erorNoConnectionLoadingView = view.findViewById(R.id.eror_no_connection_loading_view);
        btnRetry = view.findViewById(R.id.btn_retry);

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        service = RetrofitClient.getClient().create(ApiService.class);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erorNoConnectionLoadingView.setVisibility(View.VISIBLE);

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);

                            Call<List<Recipe>> call = service.getRecipes(apiKey, apiHost);
                            call.enqueue(new Callback<List<Recipe>>() {
                                @Override
                                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                                    if (response.isSuccessful()) {
                                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                                    erorNoConnectionLoadingView.setVisibility(View.GONE);
                                }
                            });
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
}