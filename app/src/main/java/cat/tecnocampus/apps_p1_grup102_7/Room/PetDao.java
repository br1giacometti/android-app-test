package cat.tecnocampus.apps_p1_grup102_7.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cat.tecnocampus.apps_p1_grup102_7.Domain.Pet;

@Dao
public interface PetDao {

    @Query("SELECT * FROM pet_table ORDER BY name ASC")
    LiveData<List<Pet>> getAllPets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("DELETE FROM pet_table")
    void deleteAll();
} 