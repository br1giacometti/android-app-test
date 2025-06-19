package cat.tecnocampus.apps_p1_grup102_7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import cat.tecnocampus.apps_p1_grup102_7.R;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PetManagerPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
        
        // Check if user is logged in
        if (mAuth.getCurrentUser() != null) {
            // User is logged in, show home
            changeFragment(HomeFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            // User is not logged in, show login
            changeFragment(LogInSignUpFragment.newInstance());
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = HomeFragment.newInstance();
            } else if (item.getItemId() == R.id.navigation_my_pets) {
                selectedFragment = MyPetsFragment.newInstance();
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = ProfileFragment.newInstance();
            }
            if (selectedFragment != null) {
                changeFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fLayoutMainActFragmentList, fragment);
        ft.commit();
    }
    
    public void showBottomNavigation(boolean show) {
        bottomNavigationView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}