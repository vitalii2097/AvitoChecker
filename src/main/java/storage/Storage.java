package storage;

import javafx.util.Pair;

import java.util.List;

public interface Storage {

    List<Pair<String, Integer>> load();

    void add(Pair<String, Integer> element);

}
