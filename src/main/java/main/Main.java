package main;

import checker.AvitoChecker;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;
import observers.TestObserver;

public class Main {

    private final AvitoChecker avitoChecker;

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        avitoChecker = new AvitoChecker();

        Observer observer = new TestObserver();
        AvitoUrl avitoUrl = new AvitoUrl("https://www.avito.ru/rossiya/telefony/iphone?pmax=50000&pmin=3000&s=104&user=1&s_trg=3");

        avitoChecker.addObserver(observer, avitoUrl);

    }

}
