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

public class CommitteeBookCommand extends Command {

    public CommitteeBookCommand() {
        super("CommitteeBook", new String[]{"CommBook", "CommSign"}, "Signs a book by the committee.");
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
        book.setTagInfo("title", new NBTTagString("COMMITTEE ON TOP"));

        final NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("Cat girls owo\nI'm going to cum all over your face.\nSend thigh pics uwu.\nNya~"));

        book.setTagInfo("pages", pages);

        packetBuffer.writeItemStack(book);

        final CPacketCustomPayload bookPacket = new CPacketCustomPayload("MC|BSign", packetBuffer);
        mc.player.connection.sendPacket(bookPacket);


    }
}
