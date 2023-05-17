package me.committee.impl.commands;

import io.netty.buffer.Unpooled;
import me.committee.api.command.Command;
import me.committee.api.util.MessageSendHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class CommitteeBook2Command extends Command {

    public CommitteeBook2Command() {
        super("CommitteeBook2", new String[]{"CommBook2", "CommSign2"}, "Signs a book by the committee.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[0][];
    }

    @Override
    public void onExec(String[] args) {
        if (mc.player == null)
            return;

        if (mc.player.getHeldItemMainhand().getItem() != Items.WRITABLE_BOOK) {
            MessageSendHelper.sendMessage("You must be holding a book and quill to use this command.", MessageSendHelper.Level.WARN);
            return;
        }

        final ItemStack book = mc.player.getHeldItemMainhand();

        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());

        book.setTagInfo("author", new NBTTagString(mc.player.getName()));
        book.setTagInfo("title", new NBTTagString("COMMITTEE ON TOP 2"));

        final NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("Spicy Big Daddy has big balls, Kmatias is finnish (ew), Pliv is retarded (autist), Status is gay, TrollFaceFan is a nazi, Filthy is massive (sex), Duisco is a fokugn isrish, Kaey is polish (alchogolsic), Badger (badger), TBM lazy, Hamburger when send toString, gato when cato, bjot hujh."));

        book.setTagInfo("pages", pages);

        packetBuffer.writeItemStack(book);

        final CPacketCustomPayload bookPacket = new CPacketCustomPayload("MC|BSign", packetBuffer);
        mc.player.connection.sendPacket(bookPacket);


    }
}
