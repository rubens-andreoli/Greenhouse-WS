package br.unip.greenhouse.model;

public class City {
    public final int id;
    public final String name;
    public final String state;
    public final String country;

    public City(int id, String name, String state, String country) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
    }

}
