package avito;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public enum City {
    SAINT_PETERSBURG("sankt-peterburg", "Санкт-Петербург"), MOSCOW("moskva", "Москва");

    private String avitoName, rusName;

    City(String avitoName, String rusName) {
        this.avitoName = avitoName;
        this.rusName = rusName;
    }

    public String getAvitoName() {
        return avitoName;
    }

    public String getRusName() {
        return rusName;
    }

    public static City getCurrentCity(String name) {
        for (City city : values()) {
            if (city.rusName.toLowerCase().equals(name.toLowerCase())) {
                return city;
            }
        }
        throw new NoSuchElementException();
    }

    public static List<City> getPossibleCities(String name) {
        List<City> cities = new ArrayList<>();
        for (City city : values()) {
            if (city.rusName.toLowerCase().contains(name.toLowerCase())) {
                cities.add(city);
            }
        }
        return cities;
    }
}
