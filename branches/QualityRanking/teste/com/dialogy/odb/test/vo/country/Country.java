package com.dialogy.odb.test.vo.country;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;
    private City capital;
    private List<City> cities;

    public Country(){
        cities = new ArrayList<City>();
    }
    public Country(String name){
        this();
        this.name = name;
    }

    public City getCapital() {
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
        addCity(capital);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " - Capital = " + (capital!=null?capital.getName():"null");
    }

    public void addCity(City city){
        cities.add(city);
    }

    public List<City> getCities(){
        return cities;
    }

}
