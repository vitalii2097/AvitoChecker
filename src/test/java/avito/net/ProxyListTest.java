package avito.net;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProxyListTest {

    @Test
    public void getProxyServer() {
        ProxyList proxyList = new ProxyList();
        System.out.println(proxyList.getProxyServer());
    }

    @Test
    public void getProxyServerCycle() {
        ProxyList proxyList = new ProxyList();
        for (int i = 0; i < 20; i++) {
            System.out.println(proxyList.getProxyServer());
        }
    }
}