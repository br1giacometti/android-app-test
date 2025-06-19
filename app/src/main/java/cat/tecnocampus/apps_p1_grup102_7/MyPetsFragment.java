package cat.tecnocampus.apps_p1_grup102_7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class MyPetsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private PetAdapter petAdapter;
    private Spinner filterSpinner;
    private Button filterButton, addPetButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Pet> pets;

    public MyPetsFragment() {
        // Required empty public constructor
    }

    public static MyPetsFragment newInstance() {
        return new MyPetsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_my_pets, container, false);
        
        try {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            pets = new ArrayList<>();
            
            initializeViews();
            setupRecyclerView();
            setupFilterSpinner();
            setupListeners();
            loadPets();
            
            return this.view;
        } catch (Exception e) {
            Log.e("MyPetsFragment", "Error in onCreateView: " + e.getMessage(), e);
            // Return a simple view to prevent crash
            TextView errorView = new TextView(getContext());
            errorView.setText("Error loading pets list");
            return errorView;
        }
    }

    private void initializeViews() {
        recyclerView = view.findViewById(R.id.recycler_view_pets);
        filterSpinner = view.findViewById(R.id.spinner_filter);
        filterButton = view.findViewById(R.id.button_filter);
        addPetButton = view.findViewById(R.id.button_add_pet);
    }

    private void setupRecyclerView() {
        petAdapter = new PetAdapter(pets, new PetAdapter.OnPetClickListener() {
            @Override
            public void onPetClick(Pet pet) {
                showPetDetails(pet);
            }

            @Override
            public void onShareClick(Pet pet) {
                sharePet(pet);
            }

            @Override
            public void onDeleteClick(Pet pet) {
                showDeleteDialog(pet);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(petAdapter);
    }

    private void setupFilterSpinner() {
        String[] filterOptions = {
            getString(R.string.all_pets),
            getString(R.string.pet_type_dog),
            getString(R.string.pet_type_cat),
            getString(R.string.pet_type_bird),
            getString(R.string.pet_type_fish),
            getString(R.string.pet_type_other)
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            getContext(), 
            android.R.layout.simple_spinner_item, 
            filterOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        filterButton.setOnClickListener(v -> applyFilter());
        addPetButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).changeFragment(AddPetFragment.newInstance());
            }
        });
    }

    private void loadPets() {
        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            Log.d("MyPetsFragment", "User not logged in");
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();
        Log.d("MyPetsFragment", "Loading pets for user: " + userId);
        
        db.collection("pets").whereEqualTo("userId", userId).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (getActivity() != null && isAdded()) {
                    try {
                        pets.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                Pet pet = document.toObject(Pet.class);
                                if (pet != null) {
                                    pet.setId(document.getId().hashCode()); // Use hash as ID for now
                                    pets.add(pet);
                                    Log.d("MyPetsFragment", "Added pet: " + pet.getName());
                                }
                            } catch (Exception e) {
                                Log.e("MyPetsFragment", "Error parsing pet document: " + e.getMessage());
                            }
                        }
                        if (petAdapter != null) {
                            petAdapter.notifyDataSetChanged();
                        }
                        Log.d("MyPetsFragment", "Loaded " + pets.size() + " pets");
                    } catch (Exception e) {
                        Log.e("MyPetsFragment", "Error processing pets: " + e.getMessage());
                    }
                }
            })
            .addOnFailureListener(e -> {
                Log.e("MyPetsFragment", "Error loading pets: " + e.getMessage());
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Error loading pets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void applyFilter() {
        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            Log.d("MyPetsFragment", "User not logged in for filter");
            return;
        }
        
        String selectedFilter = filterSpinner.getSelectedItem().toString();
        String userId = mAuth.getCurrentUser().getUid();
        Log.d("MyPetsFragment", "Applying filter: " + selectedFilter);
        
        if (selectedFilter.equals(getString(R.string.all_pets))) {
            loadPets();
        } else {
            db.collection("pets")
                .whereEqualTo("userId", userId)
                .whereEqualTo("type", selectedFilter)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (getActivity() != null && isAdded()) {
                        try {
                            pets.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                try {
                                    Pet pet = document.toObject(Pet.class);
                                    if (pet != null) {
                                        pet.setId(document.getId().hashCode());
                                        pets.add(pet);
                                    }
                                } catch (Exception e) {
                                    Log.e("MyPetsFragment", "Error parsing pet document in filter: " + e.getMessage());
                                }
                            }
                            if (petAdapter != null) {
                                petAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e("MyPetsFragment", "Error processing filtered pets: " + e.getMessage());
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("MyPetsFragment", "Error applying filter: " + e.getMessage());
                    if (getActivity() != null && isAdded()) {
                        Toast.makeText(getContext(), "Error applying filter: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void showPetDetails(Pet pet) {
        // Navigate to pet details fragment
        if (getActivity() != null) {
            ((MainActivity) getActivity()).changeFragment(PetDetailsFragment.newInstance(pet));
        }
    }

    private void sharePet(Pet pet) {
        // Intent implícito para compartir
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Pet: " + pet.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_pet_message, pet.getName()));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    private void showDeleteDialog(Pet pet) {
        if (getActivity() != null) {
            new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_pet))
                .setMessage(getString(R.string.confirm_delete_pet))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    deletePet(pet);
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
        }
    }

    private void deletePet(Pet pet) {
        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();
        
        db.collection("pets")
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", pet.getName())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    document.getReference().delete();
                }
                loadPets(); // Reload the list
            }).addOnFailureListener(e -> {
                // Handle error silently
            });
    }
} 