package cat.tecnocampus.apps_p1_grup102_7.Room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class PetViewModel extends AndroidViewModel {

    private PetRepository mRepository;
    private LiveData<List<Pet>> mAllPets;

    public PetViewModel(Application application) {
        super(application);
        mRepository = new PetRepository(application);
        mAllPets = mRepository.getAllPets();
    }

    public LiveData<List<Pet>> getAllPets() {
        return mAllPets;
    }

    public void insert(Pet pet) {
        mRepository.insert(pet);
    }

    public void update(Pet pet) {
        mRepository.update(pet);
    }

    public void delete(Pet pet) {
        mRepository.delete(pet);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
} 