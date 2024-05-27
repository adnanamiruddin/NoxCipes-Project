package id.adnan.noxcipes.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.adnan.noxcipes.MainActivity;
import id.adnan.noxcipes.R;
import id.adnan.noxcipes.config.DbConfig;

public class ProfileFragment extends Fragment {
    private EditText etProfileName;
    private EditText etProfileEmail;
    private EditText etProfilePassword;
    private EditText etProfileConfirmPassword;
    private TextView tvErrorMessage;
    private Button btnSaveProfile;
    private Button btnLogout;
    private SharedPreferences preferences;
    private DbConfig dbConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etProfileName = view.findViewById(R.id.et_profile_name);
        etProfileEmail = view.findViewById(R.id.et_profile_email);
        etProfilePassword = view.findViewById(R.id.et_profile_password);
        etProfileConfirmPassword = view.findViewById(R.id.et_profile_confirm_password);
        tvErrorMessage = view.findViewById(R.id.tv_profile_error_message);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        preferences = requireActivity().getSharedPreferences("user_pref", requireActivity().MODE_PRIVATE);
        dbConfig = new DbConfig(requireActivity());

        int userId = preferences.getInt("user_id", 0);
        Cursor cursor = dbConfig.getUserDataById(userId);
        if (cursor.moveToFirst()) {
            do {
                etProfileName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                etProfileEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                etProfilePassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                etProfileConfirmPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            } while (cursor.moveToNext());
        }

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProfileName.getText().toString();
                String email = etProfileEmail.getText().toString();
                String password = etProfilePassword.getText().toString();
                String confirmPassword = etProfileConfirmPassword.getText().toString();

                if (name.isEmpty()) {
                    etProfileName.setError("Please enter your name");
                } else if (email.isEmpty()) {
                    etProfileEmail.setError("Please enter your email");
                } else if (password.isEmpty()) {
                    etProfilePassword.setError("Please enter your password");
                } else if (confirmPassword.isEmpty()) {
                    etProfileConfirmPassword.setError("Please enter your confirm password");
                } else if (!password.equals(confirmPassword)) {
                    etProfileConfirmPassword.setError("Password and confirm password must be same");
                } else {
                    try {
                        dbConfig.updateProfile(userId, name, email, password);
                        Toast.makeText(requireActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment())
                                .commit();
                        // Activate menu item home
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    } catch (Exception e) {
                        tvErrorMessage.setVisibility(View.VISIBLE);
                        Toast.makeText(requireActivity(), "Email already registered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {
        // Show dialog
        new AlertDialog.Builder(requireActivity())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_id", null);
                    editor.putBoolean("is_logged_in", false);
                    editor.apply();
                    Intent toMainActivity = new Intent(requireActivity(), MainActivity.class);
                    startActivity(toMainActivity);
                })
                .setNegativeButton("No", null)
                .show();
    }
}