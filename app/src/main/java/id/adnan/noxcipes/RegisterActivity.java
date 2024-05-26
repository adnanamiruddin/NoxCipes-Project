package id.adnan.noxcipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import id.adnan.noxcipes.config.DbConfig;

public class RegisterActivity extends AppCompatActivity {
    private EditText etRegisterName, etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword;
    private TextView tvErrorMessage;
    private Button btnRegister;
    private DbConfig dbConfig;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
        tvErrorMessage = findViewById(R.id.tv_register_error_message);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            String name = etRegisterName.getText().toString().trim();
            String email = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();
            String confirmPassword = etRegisterConfirmPassword.getText().toString().trim();

            if (name.isEmpty()) {
                etRegisterName.setError("Please enter your name");
            } else if (email.isEmpty()) {
                etRegisterEmail.setError("Please enter your email");
            } else if (password.isEmpty()) {
                etRegisterPassword.setError("Please enter your password");
            } else if (confirmPassword.isEmpty()) {
                etRegisterConfirmPassword.setError("Please enter your confirm password");
            } else if (!password.equals(confirmPassword)) {
                etRegisterConfirmPassword.setError("Password and confirm password must be same");
            } else {
                // Insert user to database
                dbConfig = new DbConfig(this);
                preferences = getSharedPreferences("user_pref", MODE_PRIVATE);
                try {
                    Cursor cursor = dbConfig.insertUser(name, email, password);
                    if (cursor.moveToFirst()) {
                        do {
                            SharedPreferences.Editor editor = preferences.edit();
                            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                            editor.putInt("user_id", userId);
                            editor.putBoolean("is_logged_in", true);
                            editor.apply();
                            Intent toHomeActivity = new Intent(this, HomeActivity.class);
                            startActivity(toHomeActivity);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}