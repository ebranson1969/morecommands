package com.mrnobody.morecommands.command.server;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.patch.EntityPlayerMP;
import com.mrnobody.morecommands.util.ServerPlayerSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;

import net.minecraft.command.ICommandSender;

@Command(
		name = "climb",
		description = "command.climb.description",
		example = "command.climb.example",
		syntax = "command.climb.syntax",
		videoURL = "command.climb.videoURL"
		)
public class CommandClimb extends StandardCommand implements ServerCommandProperties {
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
		EntityPlayerMP player = getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class);
		ServerPlayerSettings settings = getPlayerSettings(player);
		
		try {player.setOverrideOnLadder(parseTrueFalse(params, 0, player.overrideOnLadder()));}
		catch (IllegalArgumentException ex) {throw new CommandException("command.climb.failure", sender);}
		
		sender.sendLangfileMessage(player.overrideOnLadder() ? "command.climb.on" : "command.climb.off");
        MoreCommands.INSTANCE.getPacketDispatcher().sendS02Climb(player, player.overrideOnLadder());
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[] {CommandRequirement.MODDED_CLIENT, CommandRequirement.PATCH_ENTITYPLAYERSP, CommandRequirement.PATCH_ENTITYPLAYERMP};
	}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}
