package avito.net;

import avito.Category;
import avito.City;
import avito.QueryParameters;

public class AvitoDriverTest {

    @org.junit.Test
    public void getAnnouncements() {
        AvitoDriver avitoDriver = new AvitoDriver();

        QueryParameters queryParameters =
                new QueryParameters("iphone 5s",
                        City.getCurrentCity("Санкт-Петербург"),
                        Category.getCurrentCategory("Телефоны"));

        for (Announcement announcement : avitoDriver.getAnnouncements(queryParameters)) {
            System.out.println(announcement);
        }
    }
}