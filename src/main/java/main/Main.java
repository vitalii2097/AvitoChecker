package main;

import avito.Activity;
import avito.AvitoChecker;
import avito.AvitoUrl;
import core.VkListener;
import javafx.util.Pair;
import org.eclipse.jetty.server.Server;
import storage.FileStorage;
import storage.Storage;
import vk.Bot;
import vk.Conversation;

public class Main {

    private final AvitoChecker avitoChecker;
    private final Bot bot;
    private final LogicModule logicModule;
    private final Storage storage;

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        avitoChecker = new AvitoChecker();
        bot = new Bot();
        storage = new FileStorage();
        logicModule = new LogicModule(avitoChecker, bot, storage);

        load();

        Server server = new Server(80);
        server.setHandler(new RequestHandler(logicModule, "ee825fd8"));


        server.start();
        server.join();
    }

    private void load() {
        for (Pair<String, Integer> pair : storage.load()) {
            avitoChecker.addListener(
                    new VkListener(bot, new Conversation(pair.getValue())),
                    new AvitoUrl(pair.getKey()),
                    Activity.MEDIUM);

        }
    }
}
