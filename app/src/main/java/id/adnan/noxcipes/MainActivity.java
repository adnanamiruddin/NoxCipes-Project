package id.adnan.noxcipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private Button btnMainLogin, btnMainRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        btnMainLogin = findViewById(R.id.btn_main_login);
        btnMainRegister = findViewById(R.id.btn_main_register);

        btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(toLoginActivity);
            }
        });

        btnMainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(toRegisterActivity);
            }
        });

        preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
        if (preferences.getBoolean("is_logged_in", false)) {
            Intent toHomeActivity = new Intent(this, HomeActivity.class);
            startActivity(toHomeActivity);
            finish();
        }
    }
}