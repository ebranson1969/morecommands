package com.mrnobody.morecommands.command.server;

import java.util.HashMap;
import java.util.Map;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.ServerCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.util.ServerPlayerSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
		name = "climb",
		description = "command.climb.description",
		example = "command.climb.example",
		syntax = "command.climb.syntax",
		videoURL = "command.climb.videoURL"
		)
public class CommandClimb extends ServerCommand {
	private Map<EntityPlayerMP, Boolean> playerClimbMapping = new HashMap<EntityPlayerMP, Boolean>();

	@Override
	public String getCommandName() {
		return "climb";
	}

	@Override
	public String getUsage() {
		return "command.climb.usage";
	}

	@Override
	public void execute(CommandSender sender, String[] params)throws CommandException {
		EntityPlayerMP player = (EntityPlayerMP) sender.getMinecraftISender();
		ServerPlayerSettings settings = ServerPlayerSettings.getPlayerSettings(player);
    	
        if (params.length > 0) {
        	if (params[0].equalsIgnoreCase("enable") || params[0].equalsIgnoreCase("1")
            	|| params[0].equalsIgnoreCase("on") || params[0].equalsIgnoreCase("true")) {
        		settings.climb = true;
            	sender.sendLangfileMessage("command.climb.on");
            }
            else if (params[0].equalsIgnoreCase("disable") || params[0].equalsIgnoreCase("0")
            		|| params[0].equalsIgnoreCase("off") || params[0].equalsIgnoreCase("false")) {
            	settings.climb = false;
            	sender.sendLangfileMessage("command.climb.off");
            }
            else throw new CommandException("command.climb.failure", sender);
        }
        else {
        	settings.climb = !settings.climb;
        	sender.sendLangfileMessage(settings.climb ? "command.climb.on" : "command.climb.off");
        }
        
        MoreCommands.getMoreCommands().getPacketDispatcher().sendS03Climb(player, settings.climb);
	}
	
	@Override
	public Requirement[] getRequirements() {
		return new Requirement[] {Requirement.MODDED_CLIENT, Requirement.PATCH_ENTITYCLIENTPLAYERMP};
	}
	
	@Override
	public void unregisterFromHandler() {}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(ICommandSender sender) {
		return sender instanceof EntityPlayerMP;
	}
}
