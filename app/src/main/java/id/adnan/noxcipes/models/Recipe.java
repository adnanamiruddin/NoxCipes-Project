package id.adnan.noxcipes.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {
    private int id;
    private String title;
    private String difficulty;
    private String portion;
    private String time;
    private String description;
    private List<String> ingredients;
    private List<Map<String, String>> method;
    private String image;

    public Recipe(int id, String title, String difficulty, String portion, String time, String description, List<String> ingredients, List<Map<String, String>> method, String image) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.portion = portion;
        this.time = time;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Map<String, String>> getMethod() {
        return method;
    }

    public void setMethod(List<Map<String, String>> method) {
        this.method = method;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
