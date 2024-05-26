package id.adnan.noxcipes;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.adnan.noxcipes.fragments.FavoritesFragment;
import id.adnan.noxcipes.fragments.HomeFragment;
import id.adnan.noxcipes.fragments.ProfileFragment;
import id.adnan.noxcipes.fragments.SearchFragment;

public class HomeActivity extends AppCompatActivity {
    private MenuItem navHome;
    private MenuItem navSearch;
    private MenuItem navFavorites;
    private MenuItem navProfile;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                if (menuItem.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (menuItem.getItemId() == R.id.nav_search) {
                    selectedFragment = new SearchFragment();
                } else if (menuItem.getItemId() == R.id.nav_favorites) {
                    selectedFragment = new FavoritesFragment();
                } else if (menuItem.getItemId() == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .addToBackStack(null)
                            .commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().

                beginTransaction()
                        .

                replace(R.id.fragment_container, new HomeFragment())
                        .

                commit();
    }
}