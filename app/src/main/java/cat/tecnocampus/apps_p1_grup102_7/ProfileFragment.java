package cat.tecnocampus.apps_p1_grup102_7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import cat.tecnocampus.apps_p1_grup102_7.R;

public class ProfileFragment extends Fragment {

    private View view;
    private TextView emailText;
    private EditText nameEditText, phoneEditText, addressEditText;
    private Button updateButton, logoutButton;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        mAuth = FirebaseAuth.getInstance();
        preferences = getActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        
        initializeViews();
        loadUserData();
        setupListeners();
        
        return this.view;
    }

    private void initializeViews() {
        emailText = view.findViewById(R.id.text_email);
        nameEditText = view.findViewById(R.id.edit_text_name);
        phoneEditText = view.findViewById(R.id.edit_text_phone);
        addressEditText = view.findViewById(R.id.edit_text_address);
        updateButton = view.findViewById(R.id.button_update);
        logoutButton = view.findViewById(R.id.button_logout);
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            emailText.setText(mAuth.getCurrentUser().getEmail());
            
            // Load saved profile data from SharedPreferences
            nameEditText.setText(preferences.getString("user_name", ""));
            phoneEditText.setText(preferences.getString("user_phone", ""));
            addressEditText.setText(preferences.getString("user_address", ""));
        }
    }

    private void setupListeners() {
        updateButton.setOnClickListener(v -> updateProfile());
        logoutButton.setOnClickListener(v -> showLogoutDialog());
    }

    private void updateProfile() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        
        // Clear previous errors
        nameEditText.setError(null);
        phoneEditText.setError(null);
        addressEditText.setError(null);
        
        boolean isValid = true;
        
        // Validate name (required)
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            isValid = false;
        }
        
        // Validate phone (optional but if provided, must be valid)
        if (!phone.isEmpty() && !android.util.Patterns.PHONE.matcher(phone).matches()) {
            phoneEditText.setError("Invalid phone number");
            isValid = false;
        }
        
        // Validate address (required)
        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            isValid = false;
        }
        
        if (isValid) {
            // Save to SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_name", name);
            editor.putString("user_phone", phone);
            editor.putString("user_address", address);
            editor.apply();
            
            if (getActivity() != null) {
                Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLogoutDialog() {
        if (getActivity() != null) {
            new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.warning_upper))
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    logout();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
        }
    }

    private void logout() {
        mAuth.signOut();
        
        // Clear SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        
        // Navigate back to login
        if (getActivity() != null) {
            ((MainActivity) getActivity()).changeFragment(LogInSignUpFragment.newInstance());
        }
    }
} 