package cat.tecnocampus.apps_p1_grup102_7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import cat.tecnocampus.apps_p1_grup102_7.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInSignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInSignUpFragment extends Fragment {

    private static final String TAG = "LogInSignUpFragment";
    private static final String PREFS_NAME = "PetManagerPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    
    private View view;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private TextView titleText;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    public LogInSignUpFragment() {
        // Required empty public constructor
    }

    public static LogInSignUpFragment newInstance() {
        return new LogInSignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        
        try {
            this.view = inflater.inflate(R.layout.fragment_log_in_sign_up, container, false);
            Log.d(TAG, "Layout inflated successfully");
            
            // Initialize Firebase here, not in onCreate
            mAuth = FirebaseAuth.getInstance();
            sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
            
            initializeViews();
            setupListeners();
            checkLoginStatus();
            
            Log.d(TAG, "Fragment setup completed");
            return this.view;
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            // Return a simple view to prevent crash
            TextView errorView = new TextView(getContext());
            errorView.setText("Error loading login screen");
            return errorView;
        }
    }

    private void initializeViews() {
        try {
            Log.d(TAG, "Initializing views");
            
            titleText = view.findViewById(R.id.loginText);
            emailEditText = view.findViewById(R.id.username);
            passwordEditText = view.findViewById(R.id.password);
            loginButton = view.findViewById(R.id.loginButton);
            signUpButton = view.findViewById(R.id.SignUpButton);
            
            Log.d(TAG, "Views initialized successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
        }
    }

    private void setupListeners() {
        try {
            Log.d(TAG, "Setting up listeners");
            
            loginButton.setOnClickListener(v -> {
                Log.d(TAG, "Login button clicked");
                validateAndLogin();
            });
            
            signUpButton.setOnClickListener(v -> {
                Log.d(TAG, "Sign up button clicked");
                validateAndSignUp();
            });
            
            Log.d(TAG, "Listeners setup completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up listeners: " + e.getMessage(), e);
        }
    }

    private void checkLoginStatus() {
        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, navigate to home
            ((MainActivity) getActivity()).showBottomNavigation(true);
            ((MainActivity) getActivity()).changeFragment(HomeFragment.newInstance());
        }
    }

    private void validateAndLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Clear previous errors
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        // Show loading
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Perform Firebase login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                    
                    if (task.isSuccessful()) {
                        // Save login status to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                        editor.putString(KEY_USER_EMAIL, email);
                        editor.apply();

                        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).showBottomNavigation(true);
                        ((MainActivity) getActivity()).changeFragment(HomeFragment.newInstance());
                    } else {
                        String errorMessage = task.getException() != null ? 
                            task.getException().getMessage() : "Login failed";
                        Toast.makeText(getContext(), "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Login failed: " + errorMessage);
                    }
                });
    }

    private void validateAndSignUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Clear previous errors
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        // Show loading
        signUpButton.setEnabled(false);
        signUpButton.setText("Creating account...");

        // Perform Firebase signup
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    signUpButton.setEnabled(true);
                    signUpButton.setText("Sign Up");
                    
                    if (task.isSuccessful()) {
                        // Save login status to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                        editor.putString(KEY_USER_EMAIL, email);
                        editor.apply();

                        Toast.makeText(getContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).showBottomNavigation(true);
                        ((MainActivity) getActivity()).changeFragment(HomeFragment.newInstance());
                    } else {
                        String errorMessage = task.getException() != null ? 
                            task.getException().getMessage() : "Sign up failed";
                        Toast.makeText(getContext(), "Sign up failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Sign up failed: " + errorMessage);
                    }
                });
    }
}