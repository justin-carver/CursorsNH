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
        if (args.length > 0 && args[0].equalsIgnoreCase("scale")) {
            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("Usage: /cursortest scale <number>"));
                return;
            }
            try {
                Config.cursorScale = Float.parseFloat(args[1]);
                sender.addChatMessage(new ChatComponentText("Scale set to " + Config.cursorScale));
            } catch (NumberFormatException e) {
                sender.addChatMessage(new ChatComponentText("Not a number: " + args[1]));
            }
            Config.save();
            return;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("native")) {
            Config.cursorNative = !Config.cursorNative;
            Config.save();
            sender.addChatMessage(new ChatComponentText("Native mode: " + Config.cursorNative));
            return;
        }

        // no args: load the test cursor
        String name = args.length > 0 ? args[0] : "default";
        CursorRegistry.Entry entry = CursorRegistry.get(name);

        if (entry == null) {
            sender.addChatMessage(new ChatComponentText("No cursor loaded: " + name));
            return;
        }

        CursorManager.setCursor(entry.image, entry.xHot, entry.yHot);
        sender.addChatMessage(new ChatComponentText("Set cursor: " + name));

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 0 = everyone can use, 2 = OP only
    }
}
