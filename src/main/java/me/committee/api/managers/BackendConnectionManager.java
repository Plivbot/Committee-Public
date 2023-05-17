package me.committee.api.managers;

import me.committee.Committee;
import me.committee.api.util.MessageSendHelper;
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

public class BackendConnectionManager {

    private final ArrayBlockingQueue<Packet> packetQueue = new ArrayBlockingQueue<>(100);
    private final ArrayBlockingQueue<Packet> receivedPacketQueue = new ArrayBlockingQueue<>(100);
    private Socket socket = null;

    /**
     * Is the client going to disconnect ASAP
     */
    private final AtomicBoolean disconnecting = new AtomicBoolean(false);

    /**
     * If false, the client is fully disconnected
     * True does not mean that the client would be fully connected + authed
     */
    private final AtomicBoolean connected = new AtomicBoolean(false);

    /**
     * Spawns a new thread and connects with that
     *
     * @param ip   the ip to connect to
     * @param port port
     */
    public void connect(String ip, int port) {
        disconnecting.set(false);

        Thread connectionThread = new Thread(() -> {
            try {
                Committee.LOGGER.info("Connecting to {}:{}", ip, port);
                socket = new Socket(ip, port);
                connected.set(true);
            } catch (IOException e) {
                e.printStackTrace();
                Committee.LOGGER.warn("Failed to connect!");
                MessageSendHelper.sendMessage("Failed to connect!", MessageSendHelper.Level.WARN);
                return;
            }

            while (!disconnecting.get()) {
                Scanner inputScanner = null;
                try {
                    inputScanner = new Scanner(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (inputScanner.hasNextLine()) {
                    int id = Integer.parseInt(inputScanner.nextLine());
                    long length = Long.parseLong(inputScanner.nextLine());

                    byte[] bytes = new byte[(int) length];

                    for (int i = 0; i < length; i++) {
                        bytes[i] = inputScanner.nextByte();
                    }

                    Packet packet = new Packet(id, length);
                    packet.setData(bytes);

                    if (packet.getID() == 1) {
                        disconnect();
                    }

                    receivedPacketQueue.add(packet);
                    Committee.LOGGER.info("added packetid: {}", id);
                }

                if (!packetQueue.isEmpty()) {
                    Packet packet = null;

                    try {
                        synchronized (packetQueue) {
                            packet = packetQueue.take();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (packet == null) {
                        continue;
                    }

                    try {
                        final OutputStream os = socket.getOutputStream();

                        final PrintWriter writer = new PrintWriter(os);

                        writer.println(packet.getID());
                        writer.println(packet.getLength());
                        writer.close();

                        final BufferedOutputStream bos = new BufferedOutputStream(os);
                        bos.write(packet.getData());
                        bos.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            resetConnectionData();
        });

        Thread receivedPacketManagerThread = new Thread(() -> {
            while (!disconnecting.get()) {

                if (receivedPacketQueue.isEmpty()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else  {

                    Packet packet = receivedPacketQueue.poll();

                    switch (packet.getID()) {
                        case Packet.CHAT:
                            String msg = new String(packet.getData(), StandardCharsets.UTF_8);
                            MessageSendHelper.sendMessage(msg, MessageSendHelper.PrefixType.IRC);
                            break;
                    }
                }
            }
        });

        connectionThread.start();
        receivedPacketManagerThread.start();
    }

    private void resetConnectionData() {
        connected.set(false);
    }

    /**
     * Makes the client disconnect **the next time it checks if it should**
     */
    public void disconnect() {
        disconnecting.set(true);
    }

    /**
     * is the client connected and not trying to disconnect
     *
     * @return connected and not disconnecting
     */
    public boolean isConnected() {
        return connected.get() && !disconnecting.get();
    }

    /**
     * Enables or disables irc
     */
    public void toggleChat() {
        // todo: make server send response on which state it is in after it gets the packet
        sendPacket(new Packet(Packet.TOGGLE_CHAT, 0));
    }


    /**
     * Sends a packet to the server
     *
     * @param packet the packet to send
     */
    public void sendPacket(Packet packet) {
        packetQueue.add(packet);
    }

    /**
     * @param username the username (probably want to be set on first launch and then saved in config)
     * @return -1 = channel not open
     * 0 = authed, channel open
     * 1 = registered, needs login
     * 2 = not registered
     */
    public int currentAuth(String username) {
        return -1; // todo: auth
    }
}
