package cat.tecnocampus.apps_p1_grup102_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class PetDetailsFragment extends Fragment {

    private View view;
    private ImageView petImage;
    private TextView petName, petType, petBreed, petAge, petWeight, petColor;
    private TextView petDescription, petOwner, petPhone, petAddress;
    private TextView petVaccinated, petNeutered;
    private Button shareButton, backButton;
    private Pet pet;

    public PetDetailsFragment() {
        // Required empty public constructor
    }

    public static PetDetailsFragment newInstance(Pet pet) {
        PetDetailsFragment fragment = new PetDetailsFragment();
        fragment.pet = pet;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_pet_details, container, false);
        
        initializeViews();
        loadPetData();
        setupListeners();
        
        return this.view;
    }

    private void initializeViews() {
        petImage = view.findViewById(R.id.pet_image);
        petName = view.findViewById(R.id.pet_name);
        petType = view.findViewById(R.id.pet_type);
        petBreed = view.findViewById(R.id.pet_breed);
        petAge = view.findViewById(R.id.pet_age);
        petWeight = view.findViewById(R.id.pet_weight);
        petColor = view.findViewById(R.id.pet_color);
        petDescription = view.findViewById(R.id.pet_description);
        petOwner = view.findViewById(R.id.pet_owner);
        petPhone = view.findViewById(R.id.pet_phone);
        petAddress = view.findViewById(R.id.pet_address);
        petVaccinated = view.findViewById(R.id.pet_vaccinated);
        petNeutered = view.findViewById(R.id.pet_neutered);
        shareButton = view.findViewById(R.id.button_share);
        backButton = view.findViewById(R.id.button_back);
    }

    private void loadPetData() {
        if (pet != null) {
            petName.setText(pet.getName());
            petType.setText(pet.getType());
            petBreed.setText(pet.getBreed());
            petAge.setText(pet.getAge() + " years");
            petWeight.setText(pet.getWeight() + " kg");
            petColor.setText(pet.getColor());
            petDescription.setText(pet.getDescription());
            petOwner.setText(pet.getOwner());
            petPhone.setText(pet.getPhone());
            petAddress.setText(pet.getAddress());
            petVaccinated.setText(pet.isVaccinated() ? "Yes" : "No");
            petNeutered.setText(pet.isNeutered() ? "Yes" : "No");
            
            // Load image with Glide
            Glide.with(this)
                    .load(pet.getImageUrl())
                    .placeholder(R.drawable.baseline_pets_24)
                    .into(petImage);
        }
    }

    private void setupListeners() {
        shareButton.setOnClickListener(v -> sharePet());
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).changeFragment(MyPetsFragment.newInstance());
            }
        });
        
        // Add explicit intent to open pet info website
        petImage.setOnClickListener(v -> openPetInfoWebsite());
    }

    private void sharePet() {
        // Intent implícito para compartir
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Pet: " + pet.getName());
        
        String shareText = String.format(
            "Check out my pet %s!\n\n" +
            "Type: %s\n" +
            "Breed: %s\n" +
            "Age: %d years\n" +
            "Weight: %.1f kg\n" +
            "Color: %s\n\n" +
            "Description: %s",
            pet.getName(), pet.getType(), pet.getBreed(), 
            pet.getAge(), pet.getWeight(), pet.getColor(), pet.getDescription()
        );
        
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    private void openPetInfoWebsite() {
        // Intent explícito para abrir una página web específica
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(android.net.Uri.parse("https://www.aspca.org/pet-care"));
        startActivity(webIntent);
    }
} 