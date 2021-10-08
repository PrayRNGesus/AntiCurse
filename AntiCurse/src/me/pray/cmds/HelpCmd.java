package me.pray.cmds;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;

public class HelpCmd extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equalsIgnoreCase("$help")) {
			Builder helpMenu = SelectionMenu.create("help:menu");
			helpMenu.addOption("About us", "help:menu:aboutus", Emoji.fromMarkdown("<a:DC_discord1_support:895904162485768222>"));
			helpMenu.addOption("Filter Help".replace("E:", ""), "help:menu:filter", Emoji.fromMarkdown("\uD83D\uDCDC"));
			
			event.getChannel().sendMessageEmbeds(helpCmd(event)).setActionRow(helpMenu.build()).queue();
		}
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		if(event.getValues().get(0).equalsIgnoreCase("help:menu:filter")) {
			event.deferEdit().queue();
			event.getMessage().editMessageEmbeds(filterType(event)).queue();
		} else if(event.getValues().get(0).equalsIgnoreCase("help:menu:aboutus")) {
			event.deferEdit().queue();
			event.getMessage().editMessageEmbeds(helpCmd(event)).queue();
		}
	}
	

	public MessageEmbed helpCmd(GuildMessageReceivedEvent event) {

		return new EmbedBuilder().setColor(new Color(26, 56, 176)).setTitle("Helpful information: ")
				.setDescription("**About us:** "
						+ "\n> AntiCurse was created for the sole purpose of stopping users from swearing!"
						+ "\n> Because many servers like to have a clean look to their server, with our bot you can be sure to mute anyone swearing without the hassle!"
						+ "\n\nWant to invite this bot to your server? Use our link: https://tinyurl.com/AnticurseInv"
						+ "\n\nNeed support or found a bug? Join our support server: https://discord.gg/uYU8Xkkc3g"
						+ "\n\n*This bot is in very early development, and is also open source! Source code found here: https://github.com/PrayRNGesus/AntiCurse*")
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl()).setTimestamp(getTime(event)).build();

	}
	
	public MessageEmbed helpCmd(SelectionMenuEvent event) {

		return new EmbedBuilder().setColor(new Color(26, 56, 176)).setTitle("Helpful information: ")
				.setDescription("**About us:** "
						+ "\n> AntiCurse was created for the sole purpose of stopping users from swearing!"
						+ "\n> Because many servers like to have a clean look to their server, with our bot you can be sure to mute anyone swearing without the hassle!"
						+ "\n\nWant to invite this bot to your server? Use our link: https://tinyurl.com/AnticurseInv"
						+ "\n\nNeed support or found a bug? Join our support server: https://discord.gg/uYU8Xkkc3g"
						+ "\n\n*This bot is in very early development, and is also open source! Source code found here: https://github.com/PrayRNGesus/AntiCurse*")
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl()).setTimestamp(getTime(event)).build();

	}
	
	public MessageEmbed filterType(SelectionMenuEvent event) {

		return new EmbedBuilder().setColor(new Color(26, 56, 176)).setTitle("Filter information: ")
				.setDescription("Filters choices *(for now)* are either `hard` or `custom`."
				+ "\n\nIf you decide to use a custom filter, you have access to the following:"
				+ "\n> `$deny [word]`" + "\n> Denies a word from usage"
				+ "\n\n> `$allow [word]`" + "\n> Allows a word which was previously denied")
				.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl()).setTimestamp(getTime(event)).build();

	}

	public OffsetDateTime getTime(GuildMessageReceivedEvent e) {
		return e.getMessage().getTimeCreated();
	}
	
	public OffsetDateTime getTime(SelectionMenuEvent e) {
		return e.getMessage().getTimeCreated();
	}

	
}
