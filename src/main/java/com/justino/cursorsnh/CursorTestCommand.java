package com.justino.cursorsnh;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CursorTestCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cursortest";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cursortest - Outputs debug information for time being.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Cursor testing in progress!"));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 0 = everyone can use, 2 = OP only
    }
}
