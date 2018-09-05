package main;

import avito.AvitoChecker;
import avito.AvitoUrl;
import core.VkListener;
import javafx.util.Pair;
import org.eclipse.jetty.server.Server;
import storage.FileStorage;
import storage.Storage;
import vk.Bot;
import vk.Conversation;
import vk.VkConversation;

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
        bot = Bot.getInstance();
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
            logicModule.addRequest(new VkConversation(pair.getValue()), new AvitoUrl(pair.getKey()));
        }
    }
}
