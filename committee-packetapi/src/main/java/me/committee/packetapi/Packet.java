package me.committee.packetapi;

public class Packet {

    public static final int INVALID     = -1;
    public static final int AUTH        = 0;
    public static final int KICK        = 1;
    public static final int CHAT        = 2;
    public static final int TOGGLE_CHAT = 3;

    private final int id;
    private final long length;

    private byte[] data = null;

    public Packet(int id, long length) {
        this.id = id;
        this.length = length;
    }

    /**
     * The packet id
     * -1 = invalid
     * 0  = Auth
     * 1  = disconnect or kick
     * 2  = chat
     */
    public int getID() {
        return id;
    }

    /**
     * The length of the packet, bytes after side is sent
     */
    public long getLength() {
        return length;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
