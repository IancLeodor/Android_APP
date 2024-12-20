package com.example.gustoheaven;

import java.util.List;

public class Recipe {
    private String id;
    private String name;
    private String thumbnail;
    private String numServings;
    private String totalTime;
    private List<String> ingredients;

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", numServings='" + numServings + '\'' +
                ", totalTime='" + totalTime + '\'' +
                '}';
    }

    public Recipe(String id, String name, String thumbnail, String numServings, String totalTime) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.numServings = numServings;
        this.totalTime = totalTime;
    }

    public Recipe(String id, String name, String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public Recipe(String id, String name, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNumServings() {
        return numServings;
    }

    public void setNumServings(String numServings) {
        this.numServings = numServings;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
