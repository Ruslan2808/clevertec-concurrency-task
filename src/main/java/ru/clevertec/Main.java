package ru.clevertec;

import ru.clevertec.service.Client;
import ru.clevertec.service.Server;

public class Main {

    public static void main(String[] args) {
        Client client = new Client(100, 10);
        Server server = new Server();

        client.sendRequests(server);

        System.out.println(client.getAccumulator());
    }
}
