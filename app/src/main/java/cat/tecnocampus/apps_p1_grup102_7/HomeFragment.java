package cat.tecnocampus.apps_p1_grup102_7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import cat.tecnocampus.apps_p1_grup102_7.R;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String PREFS_NAME = "PetManagerPrefs";
    private static final String KEY_TOTAL_PETS_API = "totalPetsFromAPI";
    
    private View view;
    private TextView welcomeText;
    private TextView totalPetsText;
    private TextView myPetsText;
    private Button addPetButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
            this.view = inflater.inflate(R.layout.fragment_home, container, false);
            Log.d(TAG, "Layout inflated successfully");
            
            // Initialize Firebase here, not in onCreate
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
            requestQueue = Volley.newRequestQueue(getContext());
            
            initializeViews();
            setupListeners();
            loadData();
            
            Log.d(TAG, "Fragment setup completed");
            return this.view;
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            // Return a simple view to prevent crash
            TextView errorView = new TextView(getContext());
            errorView.setText("Error loading home screen");
            return errorView;
        }
    }

    private void initializeViews() {
        try {
            Log.d(TAG, "Initializing views");
            
            welcomeText = view.findViewById(R.id.welcome_text);
            totalPetsText = view.findViewById(R.id.total_pets_text);
            myPetsText = view.findViewById(R.id.my_pets_text);
            addPetButton = view.findViewById(R.id.add_pet_button);
            
            Log.d(TAG, "Views initialized successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
        }
    }

    private void setupListeners() {
        try {
            Log.d(TAG, "Setting up listeners");
            
            addPetButton.setOnClickListener(v -> {
                Log.d(TAG, "Add pet button clicked");
                // Navigate to AddPetFragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).changeFragment(AddPetFragment.newInstance());
                }
            });
            
            Log.d(TAG, "Listeners setup completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up listeners: " + e.getMessage(), e);
        }
    }

    private void loadData() {
        try {
            Log.d(TAG, "Loading data");
            
            // Set welcome message
            welcomeText.setText(getString(R.string.welcome_message));
            
            // Load total pets from Firestore (all users)
            loadTotalPetsFromFirestore();
            
            // Load user's pets from Firestore
            loadUserPets();
            
            Log.d(TAG, "Data loaded successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error loading data: " + e.getMessage(), e);
        }
    }

    private void loadTotalPetsFromFirestore() {
        db.collection("pets").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (getActivity() != null && isAdded()) {
                    int totalPets = queryDocumentSnapshots.size();
                    
                    // Save to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(KEY_TOTAL_PETS_API, totalPets);
                    editor.apply();
                    
                    // Display the count
                    totalPetsText.setText(getString(R.string.total_pets, totalPets));
                    Log.d(TAG, "Total pets loaded: " + totalPets);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading total pets: " + e.getMessage());
                // Use cached value or default
                int cachedValue = sharedPreferences.getInt(KEY_TOTAL_PETS_API, 0);
                totalPetsText.setText(getString(R.string.total_pets, cachedValue));
            });
    }

    private void loadUserPets() {
        if (mAuth.getCurrentUser() == null) {
            myPetsText.setText(getString(R.string.my_pets_count, 0));
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();
        
        db.collection("pets").whereEqualTo("userId", userId).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (getActivity() != null) {
                    int userPetCount = queryDocumentSnapshots.size();
                    myPetsText.setText(getString(R.string.my_pets_count, userPetCount));
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading user pets: " + e.getMessage());
                myPetsText.setText(getString(R.string.my_pets_count, 0));
            });
    }
} 