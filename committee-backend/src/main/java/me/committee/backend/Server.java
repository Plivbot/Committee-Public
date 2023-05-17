package me.committee.backend;

import me.committee.backend.commands.CommandManager;
import me.committee.packetapi.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    public static final int PORT = 6666;

    public static AtomicBoolean shuttingDown = new AtomicBoolean(false);
    public static boolean doneSafeShutdown = false;

    public static final CommandManager commandManager = new CommandManager();

    public static final List<Client> connections = new ArrayList<>();
    public static final List<Socket> authenticating = new ArrayList<>();

    public static void main(String[] args) {

        // todo:
        //  auth: client & server
        // the credentials need to be written from the client right after it connects so we need to make the user
        // login before connecting, rn it does not write them so the server gets stuck waiting for them

        System.out.println("Starting...");

        commandManager.init();
        Thread inputThread = new Thread(() -> {

            Scanner scanner = new Scanner(System.in);

            String next;

            while (!shuttingDown.get()) {
                next = scanner.nextLine(); // the thread stops here until stdin has gotten a new line
                commandManager.parseAndExec(next);
            }
            System.out.println("Input thread stopped.");
        });

        Thread shutdownTread = new Thread(() -> { // the only purpose of this is to keep the process alive so the other threads don't get killed
            while (!doneSafeShutdown) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Thanks for using the backend lul.");
        });

        Thread connectionThread = new Thread(() -> {

            ServerSocket ss = null;
            try {
                ss = new ServerSocket(PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ss == null || !ss.isBound() || ss.isClosed()) {
                System.err.printf("Most likely to bind to port, make sure you have no other sockets running in the port %d%n", PORT);
                System.err.println("If you don't know how this would be possible, and the server has crashed, try killing all java processes");
                System.err.println("with the command \"killall -9 java\"");
                System.exit(1);
            }

            while (!shuttingDown.get()) {

                Socket s = null;

                try {
                    s = ss.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (s == null) {
                    System.err.println("Failed to accept connection, disgarding it an continuing on as normal!");
                    continue;
                }

                authenticating.add(s);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Connection thread stopped.");
        });

        Thread authThread = new Thread(() -> {
            System.out.println("Auth thread start");

            while (!shuttingDown.get()) {

                for (Socket socket : authenticating) {
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(socket.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (scanner.hasNextLine()) {
                        System.out.println("Trying to auth " + socket.getInetAddress().getHostAddress());
                        AuthData auth = isCorrectAuth(scanner.nextLine());
                        if (auth.correct) {

                            final Client client = new Client(socket);
                            client.name = auth.name;

                            System.out.println("Auth correct for " + socket.getInetAddress().getHostAddress() + ", username: " + client.name);
                            connections.add(client);
                            client.start();
                        }
                    }
                }

                try {
                    if (!connections.isEmpty()) {
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Auth thread stopped");
        });


        shutdownTread.setDaemon(false);

        inputThread.start();
        authThread.start();
        connectionThread.start();
        shutdownTread.start();
    }

    public static AuthData isCorrectAuth(String auth) {
        String[] credentials = auth.split(":");
        if (credentials.length < 2) {
            return new AuthData(false, null, null);
        }
        String username = credentials[0];
        String passwd = credentials[1];

        // todo: password auth and store in file hashed or smthn
        // i don't want to set up a db for this lol
        return new AuthData(true, username, passwd);
    }

    /**
     * Sends a packet to all connected clients
     *
     * @param packet             the packet to send
     * @param checkIsChatEnabled do we want to check if the client has chat enabled? this does something only if
     *                           the packet is a chat packet (id = 2)
     */
    public static void sendAll(Packet packet, boolean checkIsChatEnabled) {
        synchronized (connections) {
            for (Client connection : connections) {
                if (packet.getID() == Packet.CHAT && !connection.isChatEnabled.get() && checkIsChatEnabled) {
                    continue;
                }
                connection.queuePacket(packet);
            }
        }
    }
}
