package main;

import bot.telegram.TgBot;
import checker.AvitoChecker;
import org.apache.http.HttpHost;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main();

    }

    public Main() throws Exception {
        AvitoChecker avitoChecker = new AvitoChecker();

        //VkBot vkBot = new VkBot(avitoChecker);
        TgBot bot = new TgBot(avitoChecker, new HttpHost("142.93.242.159", 54321));
    }

}
