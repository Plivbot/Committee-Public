package me.committee.backend;

import me.committee.packetapi.Packet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private final Socket socket;
    public String name = "Unauthenticated";

    public AtomicBoolean isChatEnabled = new AtomicBoolean(true);

    private final ArrayBlockingQueue<Packet> packetQueue = new ArrayBlockingQueue<>(100);
    private final ArrayBlockingQueue<Packet> receivedPacketQueue = new ArrayBlockingQueue<>(100);

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void start() {
        Thread connectionThread = new Thread(() -> {
            System.out.printf("Socket Thread for %s started%n", socket.getInetAddress().getHostAddress());

            Scanner inputScanner = null;
            try {
                inputScanner = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (socket.isConnected() && !Server.shuttingDown.get()) {

                if (inputScanner.hasNextLine()) {
                    System.out.println("read line");
                    int id = Integer.parseInt(inputScanner.nextLine());
                    long length = Long.parseLong(inputScanner.nextLine());

                    byte[] bytes = new byte[(int) length];

                    for (int i = 0; i < length; i++) {
                        bytes[i] = inputScanner.nextByte();
                    }

                    Packet packet = new Packet(id, length);
                    packet.setData(bytes);

                    receivedPacketQueue.add(packet);
                }

                if (!packetQueue.isEmpty()) {
                    Packet packet = null;
                    try {
                        packet = packetQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (packet == null) {
                        continue;
                    }
                    try {
                        OutputStream os = socket.getOutputStream();

                        PrintWriter writer = new PrintWriter(os);

                        writer.println(packet.getID());
                        writer.println(packet.getLength());
                        writer.close();

                        BufferedOutputStream bos = new BufferedOutputStream(os);
                        bos.write(packet.getData());
                        bos.close();

                    } catch (IOException ignored) {

                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.printf("Socket Thread for %s stopped%n", socket.getInetAddress().getHostAddress());
        });

        Thread mainThread = new Thread(() -> {
            System.out.printf("Socket Thread for %s started%n", socket.getInetAddress().getHostAddress());
            while (socket.isConnected() && !Server.shuttingDown.get()) {
                if (receivedPacketQueue.isEmpty()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                final Packet next = receivedPacketQueue.poll();

                if (next == null) {
                    System.err.println("Next packet is null even though packet queue was not empty! " + socket.getInetAddress().getHostAddress());
                    continue;
                }

                switch (next.getID()) {
                    case Packet.TOGGLE_CHAT:
                        isChatEnabled.set(!isChatEnabled.get());
                        break;
                    case Packet.CHAT:
                        String content = "<" + name + "> " + new String(next.getData(), StandardCharsets.UTF_8);

                        if (content.length() > 512) {
                            String errormsg = "that message is too long!";
                            Packet chatPacket = new Packet(Packet.CHAT, errormsg.length());
                            chatPacket.setData(errormsg.getBytes(StandardCharsets.UTF_8));
                            packetQueue.add(chatPacket);
                        }

                        Packet chatPacket = new Packet(Packet.CHAT, content.length());
                        chatPacket.setData(content.getBytes(StandardCharsets.UTF_8));
                        Server.sendAll(chatPacket, true);
                        break;
                    case Packet.KICK:
                    case Packet.INVALID:
                        // todo: do we want to kick the client for sending an invalid (client should be unable to send) packet?
                        break;
                }
            }
        });


        connectionThread.setDaemon(true);
        connectionThread.start();
        mainThread.setDaemon(false);
        mainThread.start();
    }

    public void queuePacket(Packet packet) {
        packetQueue.add(packet);
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendChatMessage(String message) {
        Packet chatPacket = new Packet(Packet.CHAT, message.length());
        chatPacket.setData(message.getBytes(StandardCharsets.UTF_8));
        packetQueue.add(chatPacket);
    }
}
