package cat.tecnocampus.apps_p1_grup102_7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private List<Pet> pets;
    private OnPetClickListener listener;

    public interface OnPetClickListener {
        void onPetClick(Pet pet);
        void onShareClick(Pet pet);
        void onDeleteClick(Pet pet);
    }

    public PetAdapter(List<Pet> pets, OnPetClickListener listener) {
        this.pets = pets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = pets.get(position);
        
        holder.petName.setText(pet.getName());
        holder.petType.setText(pet.getType());
        holder.petBreed.setText(pet.getBreed());
        holder.petAge.setText(pet.getAge() + " years");
        
        // Load image with Glide
        Glide.with(holder.itemView.getContext())
                .load(pet.getImageUrl())
                .placeholder(R.drawable.baseline_pets_24)
                .into(holder.petImage);
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPetClick(pet);
            }
        });
        
        holder.shareButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShareClick(pet);
            }
        });
        
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(pet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public void updatePets(List<Pet> newPets) {
        this.pets = newPets;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView petImage;
        TextView petName, petType, petBreed, petAge;
        ImageButton shareButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.pet_image);
            petName = itemView.findViewById(R.id.pet_name);
            petType = itemView.findViewById(R.id.pet_type);
            petBreed = itemView.findViewById(R.id.pet_breed);
            petAge = itemView.findViewById(R.id.pet_age);
            shareButton = itemView.findViewById(R.id.button_share);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
} 