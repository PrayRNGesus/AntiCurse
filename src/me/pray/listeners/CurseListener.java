package me.pray.listeners;

import java.awt.Color;
import java.io.IOException;

import me.kaimu.hastebin.Hastebin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CurseListener extends ListenerAdapter {

	//this is the bad word listener class, created by @Pray#0001 on October 6th, 2021
	
	Hastebin hastebin = new Hastebin();
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var content = event.getMessage().getContentRaw();
		var muted = event.getGuild().getRolesByName("muted", true);
		String[] args = content.split("\\s+");
		
		//looping through each argument in the provided message's raw
		for (int i = 0; i < args.length; i++) {
			
			//checking for swear word
			if (Check.checkForSwear(args[i], event)) {
				
				if(event.getAuthor().equals(event.getJDA().getSelfUser())) {
					return;
				}
				
//				if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
//					return;
//				}
				
				//checking for muted role, creating if not created already
				if (muted.isEmpty()) {
					event.getGuild().createRole().setName("muted").setColor(Color.DARK_GRAY).queue(role -> {
						for (TextChannel channel : event.getGuild().getTextChannels()) {
							channel.createPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
						}
					});
				}

				//giving user the muted role && getting the blocked word
				try {
					event.getGuild().addRoleToMember(event.getMember().getIdLong(), muted.get(0)).queue();
				} catch (InsufficientPermissionException ipe) {
					event.getChannel().sendMessage("I don't have permission to mute this user! Please make sure you give me the MANAGE_ROLES permsison").queue();
					return;
				} catch (HierarchyException he) {
					event.getChannel().sendMessage("My role is under the `muted` role in the role heirarchy! Please move my role above the muted role, or else I cannot enforce your serve!").queue();
					return;
				}
				
				String blockedWord = args[i];
				
				//making a hastebin file with the entire message using the hastebin api
				String fullMessage = event.getMessage().getContentRaw();
				boolean raw = true;
				String url = null;
				try {
					url = hastebin.post(fullMessage, raw);
				} catch (IOException e) {
					url = "*error generating hastebin file*";
				}
				
				//sending a message to the general chat
				event.getChannel().sendMessageEmbeds(Check.userMutedWithoutLink(event, blockedWord)).queue();
				
				//logging the mute to #logs
				Check.logMute(event, event.getAuthor(), blockedWord, url);
				return;
			}
		}

	}

}
