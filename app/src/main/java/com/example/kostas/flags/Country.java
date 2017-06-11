package com.example.kostas.flags;

/**
 * Created by Kostas on 11/6/2017.
 */

public class Country {

    private String name;
    private String population;
    private String iconURL;

    public Country(String name, String population, String iconURL) {
        super();
        this.name=name;
        this.population=population;
        this.iconURL=iconURL;
    }

    public String getName() {
        return name;
    }

    public String getPopulation() {
        return population;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
}
