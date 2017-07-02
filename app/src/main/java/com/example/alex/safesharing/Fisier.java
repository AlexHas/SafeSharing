package com.example.alex.safesharing;

/**
 * Created by alexHash on 15.06.2017.
 */

public class Fisier {
    private String name;
    private String location;
    private String size;

    public Fisier(String name, String location, String size) {
        this.name = name;
        this.location=location;
        this.size=size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }



    public Fisier(){

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fisier{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
