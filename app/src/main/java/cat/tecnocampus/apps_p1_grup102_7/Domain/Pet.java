package cat.tecnocampus.apps_p1_grup102_7.Domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pet_table")
public class Pet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    
    @ColumnInfo(name = "name")
    private String name;
    
    @ColumnInfo(name = "type")
    private String type;
    
    @ColumnInfo(name = "breed")
    private String breed;
    
    @ColumnInfo(name = "age")
    private int age;
    
    @ColumnInfo(name = "weight")
    private double weight;
    
    @ColumnInfo(name = "color")
    private String color;
    
    @ColumnInfo(name = "description")
    private String description;
    
    @ColumnInfo(name = "owner")
    private String owner;
    
    @ColumnInfo(name = "phone")
    private String phone;
    
    @ColumnInfo(name = "address")
    private String address;
    
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    
    @ColumnInfo(name = "vaccinated")
    private boolean vaccinated;
    
    @ColumnInfo(name = "neutered")
    private boolean neutered;
    
    @ColumnInfo(name = "userId")
    private String userId;

    // Constructor vacío requerido por Firestore y Room
    public Pet() {
        // Required empty constructor for Firestore and Room
    }

    @Ignore
    public Pet(String name, String type, String breed, int age, double weight, 
               String color, String description, String owner, String phone, 
               String address, String imageUrl, boolean vaccinated, boolean neutered, String userId) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.color = color;
        this.description = description;
        this.owner = owner;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
        this.vaccinated = vaccinated;
        this.neutered = neutered;
        this.userId = userId;
    }

    @Ignore
    public Pet(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.type = pet.getType();
        this.breed = pet.getBreed();
        this.age = pet.getAge();
        this.weight = pet.getWeight();
        this.color = pet.getColor();
        this.description = pet.getDescription();
        this.owner = pet.getOwner();
        this.phone = pet.getPhone();
        this.address = pet.getAddress();
        this.imageUrl = pet.getImageUrl();
        this.vaccinated = pet.isVaccinated();
        this.neutered = pet.isNeutered();
        this.userId = pet.getUserId();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", vaccinated=" + vaccinated +
                ", neutered=" + neutered +
                ", userId='" + userId + '\'' +
                '}';
    }
} 