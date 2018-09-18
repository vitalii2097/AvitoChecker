package main;

import checker.AvitoChecker;
import logic.LogicHandler;
import logic.LogicModule;
import observers.TeleObserver;
import observers.VkObserver;
import storage.FileStorage;
import storage.Storage;
import sun.rmi.runtime.Log;
import telegram.TeleBot;
import vk.VkBot;

public class Main {

    private final AvitoChecker avitoChecker;
    private final Storage storage;

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        avitoChecker = new AvitoChecker();
        storage = new FileStorage();

        //load();

        VkBot vkBot = VkBot.getInstance();
        LogicModule vkLogicModule = new LogicModule(avitoChecker, VkObserver.class);
        vkBot.setLogicModule(vkLogicModule);

        TeleBot teleBot = TeleBot.getInstance();
        LogicModule teleLogicModule = new LogicModule(avitoChecker, TeleObserver.class);
        teleBot.setLogicModule(teleLogicModule);
    }

    /*private void load() {
        for (Pair<String, Integer> pair : storage.load()) {
            try {
                logicModule.addRequest(new VkConversation(pair.getValue()), new AvitoUrl(pair.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
