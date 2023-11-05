package kr.co.fastcampus.eatgo.domain;

public class Restaurant {

    private final String address;
    private final String name;

    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getInformation() {
        return name + " in Seoul";
    }
}
