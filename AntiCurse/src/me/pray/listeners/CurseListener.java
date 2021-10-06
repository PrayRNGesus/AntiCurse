package me.pray.listeners;

import java.awt.Color;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CurseListener extends ListenerAdapter {

	//this is the bad word listener class, created by @Pray#0001 on October 6th, 2021
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var content = event.getMessage().getContentRaw();
		var muted = event.getGuild().getRolesByName("muted", true);
		String[] args = content.split("\\s+");
		
		//looping through each argument in the provided message's raw
		for (int i = 0; i < args.length; i++) {
			
			//checking for swear word
			if (Check.checkForSwear(args[i])) {
				
				//checking for muted role, creating if not created already
				if (muted.isEmpty()) {
					event.getGuild().createRole().setName("muted").setColor(Color.DARK_GRAY).queue(role -> {
						for (TextChannel channel : event.getGuild().getTextChannels()) {
							channel.createPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
						}
					});
				}

				//giving user the muted role
				event.getGuild().addRoleToMember(event.getMember().getIdLong(), muted.get(0)).queue();
				
				//sending a message to the general chat
				event.getChannel().sendMessageEmbeds(Check.userMuted(event)).queue();
				
				//logging the mute to #logs
				Check.logMute(event, event.getAuthor());
			}
		}

	}

}
