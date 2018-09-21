package main;

import bot.telegram.TgBot;
import checker.AvitoChecker;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main();

    }

    public Main() throws Exception {
        AvitoChecker avitoChecker = new AvitoChecker();

        //VkBot vkBot = new VkBot(avitoChecker);
        TgBot bot = new TgBot(avitoChecker, true);
    }

}
