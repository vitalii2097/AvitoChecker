package avito;

import java.util.Objects;

public class QueryParameters implements Url {

    private String name;
    private City city;
    private Category category;

    public QueryParameters(String name, City city, Category category) {
        this.name = name;
        this.city = city;
        this.category = category;
    }

    City getCity() {
        return city;
    }

    Category getCategory() {
        return category;
    }

    String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        String domain = "https://www.avito.ru";
        domain += "/" + getCity().getAvitoName();
        domain += "/" + getCategory().getAvitoName();
        domain += "?s=104&s_trg=3&q=" + getName().replace(' ', '+');
        return domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryParameters that = (QueryParameters) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "QueryParameters{" +
                "name='" + name + '\'' +
                ", city=" + city +
                ", category=" + category +
                '}';
    }
}


