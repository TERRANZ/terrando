package ru.terra.ndo.server;

import ru.terra.server.ServerBoot;

import java.io.IOException;

/**
 * Date: 24.06.14
 * Time: 17:25
 */
public class Main {
    public static void main(String... args) throws IOException {
        new ServerBoot().start();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
