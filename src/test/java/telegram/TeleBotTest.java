package telegram;

import org.junit.Test;

import static org.junit.Assert.*;

public class TeleBotTest {

    @Test
    public void startTest() {
        TeleBot instance = TeleBot.getInstance();
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}