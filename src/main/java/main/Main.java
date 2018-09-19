package main;

import bot.vk.VkBot;
import checker.AvitoChecker;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        AvitoChecker avitoChecker = new AvitoChecker();

        VkBot vkBot = new VkBot(avitoChecker);
    }

}
