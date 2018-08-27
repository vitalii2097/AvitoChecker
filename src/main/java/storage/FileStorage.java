package storage;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileStorage implements Storage {

    private File file;

    public FileStorage() throws IOException {
        file = new File("storage.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public List<Pair<String, Integer>> load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<Pair<String, Integer>> elements = new ArrayList<>();

            reader.lines().forEach(line -> {
                int spaceIndex = line.indexOf(' ');
                String url = line.substring(0, spaceIndex);
                int peer_id = Integer.valueOf(line.substring(spaceIndex + 1));

                elements.add(new Pair<>(url, peer_id));
            });

            return elements;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void add(Pair<String, Integer> element) {
        try (FileWriter writer = new FileWriter(file, true);) {
            writer.write(element.getKey() + " " + element.getValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
