package cat.tecnocampus.apps_p1_grup102_7;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import cat.tecnocampus.apps_p1_grup102_7.R;
import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class AddPetFragment extends Fragment {

    private View view;
    private EditText nameEditText, breedEditText, ageEditText, weightEditText, colorEditText;
    private EditText descriptionEditText, ownerEditText, phoneEditText, addressEditText;
    private Spinner typeSpinner;
    private CheckBox vaccinatedCheckBox, neuteredCheckBox;
    private Button saveButton, cancelButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AddPetFragment() {
        // Required empty public constructor
    }

    public static AddPetFragment newInstance() {
        return new AddPetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        
        // Initialize Firebase here, not in onCreate
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        initializeViews();
        setupSpinner();
        setupListeners();
        
        return this.view;
    }

    private void initializeViews() {
        nameEditText = view.findViewById(R.id.edit_text_name);
        typeSpinner = view.findViewById(R.id.spinner_type);
        breedEditText = view.findViewById(R.id.edit_text_breed);
        ageEditText = view.findViewById(R.id.edit_text_age);
        weightEditText = view.findViewById(R.id.edit_text_weight);
        colorEditText = view.findViewById(R.id.edit_text_color);
        descriptionEditText = view.findViewById(R.id.edit_text_description);
        ownerEditText = view.findViewById(R.id.edit_text_owner);
        phoneEditText = view.findViewById(R.id.edit_text_phone);
        addressEditText = view.findViewById(R.id.edit_text_address);
        vaccinatedCheckBox = view.findViewById(R.id.checkbox_vaccinated);
        neuteredCheckBox = view.findViewById(R.id.checkbox_neutered);
        saveButton = view.findViewById(R.id.button_save);
        cancelButton = view.findViewById(R.id.button_cancel);
    }

    private void setupSpinner() {
        String[] petTypes = {
            getString(R.string.pet_type_dog),
            getString(R.string.pet_type_cat),
            getString(R.string.pet_type_bird),
            getString(R.string.pet_type_fish),
            getString(R.string.pet_type_other)
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            getContext(), 
            android.R.layout.simple_spinner_item, 
            petTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> validateAndSave());
        cancelButton.setOnClickListener(v -> showCancelDialog());
    }

    private void validateAndSave() {
        // Clear previous errors
        clearErrors();
        
        boolean isValid = true;
        
        // Validate name (required)
        String name = nameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_name_required));
            isValid = false;
        }
        
        // Validate type (required)
        String type = typeSpinner.getSelectedItem().toString();
        if (type.equals(getString(R.string.pet_type_dog)) && typeSpinner.getSelectedItemPosition() == 0) {
            // This is a workaround since we can't easily validate spinner selection
            // In a real app, you might want to add a "Select type" option
        }
        
        // Validate age (positive number)
        String ageStr = ageEditText.getText().toString().trim();
        int age = 0;
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0) {
                    ageEditText.setError(getString(R.string.error_age_invalid));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                ageEditText.setError(getString(R.string.error_age_invalid));
                isValid = false;
            }
        }
        
        // Validate weight (positive number)
        String weightStr = weightEditText.getText().toString().trim();
        double weight = 0.0;
        if (!TextUtils.isEmpty(weightStr)) {
            try {
                weight = Double.parseDouble(weightStr);
                if (weight <= 0) {
                    weightEditText.setError(getString(R.string.error_weight_invalid));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                weightEditText.setError(getString(R.string.error_weight_invalid));
                isValid = false;
            }
        }
        
        // Validate phone (optional but if provided, must be valid)
        String phone = phoneEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !Patterns.PHONE.matcher(phone).matches()) {
            phoneEditText.setError(getString(R.string.error_phone_invalid));
            isValid = false;
        }
        
        // Validate owner email (optional but if provided, must be valid)
        String owner = ownerEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(owner) && !Patterns.EMAIL_ADDRESS.matcher(owner).matches()) {
            ownerEditText.setError(getString(R.string.error_email_invalid));
            isValid = false;
        }
        
        if (isValid) {
            savePet();
        }
    }

    private void clearErrors() {
        nameEditText.setError(null);
        ageEditText.setError(null);
        weightEditText.setError(null);
        phoneEditText.setError(null);
        ownerEditText.setError(null);
    }

    private void savePet() {
        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();
        
        // Get values with safe parsing
        String name = nameEditText.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();
        String breed = breedEditText.getText().toString().trim();
        
        int age = 0;
        try {
            String ageStr = ageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(ageStr)) {
                age = Integer.parseInt(ageStr);
            }
        } catch (NumberFormatException e) {
            age = 0;
        }
        
        double weight = 0.0;
        try {
            String weightStr = weightEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(weightStr)) {
                weight = Double.parseDouble(weightStr);
            }
        } catch (NumberFormatException e) {
            weight = 0.0;
        }
        
        String color = colorEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String ownerEmail = ownerEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        
        Pet pet = new Pet(
            name,
            type,
            breed,
            age,
            weight,
            color,
            description,
            ownerEmail,
            phoneNumber,
            address,
            "https://via.placeholder.com/300x300?text=Pet+Image", // Default image
            vaccinatedCheckBox.isChecked(),
            neuteredCheckBox.isChecked(),
            userId
        );
        
        db.collection("pets").add(pet)
            .addOnSuccessListener(documentReference -> {
                if (getActivity() != null && isAdded()) {
                    try {
                        Toast.makeText(getContext(), getString(R.string.pet_added_successfully), Toast.LENGTH_SHORT).show();
                        // Navigate back to home instead of MyPets to avoid potential crash
                        ((MainActivity) getActivity()).changeFragment(HomeFragment.newInstance());
                    } catch (Exception e) {
                        Log.e("AddPetFragment", "Error navigating after save: " + e.getMessage());
                        Toast.makeText(getContext(), "Pet saved but navigation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(e -> {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void showCancelDialog() {
        if (getActivity() != null) {
            new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.warning_upper))
                .setMessage("Are you sure you want to cancel? All data will be lost.")
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    ((MainActivity) getActivity()).changeFragment(HomeFragment.newInstance());
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
        }
    }
} 