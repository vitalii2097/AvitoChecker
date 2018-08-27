package avito;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public enum Category {
    PHONES("telefony", "Телефоны"),
    PCS("nastolnye_kompyutery", "Настольные компьютеры"),
    COMPUTER_PRODUCTS("tovary_dlya_kompyutera", "Товары для компьютера");

    private String avitoName, rusName;

    Category(String avitoName, String rusName) {
        this.avitoName = avitoName;
        this.rusName = rusName;
    }

    public String getAvitoName() {
        return avitoName;
    }

    public String getRusName() {
        return rusName;
    }

    public static Category getCurrentCategory(String name) {
        for (Category category : values()) {
            if (category.rusName.toLowerCase().equals(name.toLowerCase())) {
                return category;
            }
        }
        throw new NoSuchElementException();
    }

    public static List<Category> getPossibleCities(String name) {
        List<Category> categories = new ArrayList<>();
        for (Category category : values()) {
            if (category.rusName.toLowerCase().contains(name.toLowerCase())) {
                categories.add(category);
            }
        }
        return categories;
    }
}
