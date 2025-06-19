package cat.tecnocampus.apps_p1_grup102_7.Room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

public class PetRepository {

    private PetDao mPetDao;
    private LiveData<List<Pet>> mAllPets;

    PetRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mPetDao = db.petDao();
        mAllPets = mPetDao.getAllPets();
    }

    LiveData<List<Pet>> getAllPets() {
        return mAllPets;
    }

    void insert(Pet pet) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mPetDao.insert(pet);
        });
    }

    void update(Pet pet) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mPetDao.update(pet);
        });
    }

    void delete(Pet pet) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mPetDao.delete(pet);
        });
    }

    void deleteAll() {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            mPetDao.deleteAll();
        });
    }
} 