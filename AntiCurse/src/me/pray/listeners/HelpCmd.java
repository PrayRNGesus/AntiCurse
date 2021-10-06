package me.pray.listeners;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCmd extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equalsIgnoreCase("$help")) {
			event.getChannel().sendMessageEmbeds(userMuted(event)).queue();
		}
	}

	public MessageEmbed userMuted(GuildMessageReceivedEvent event) {

		return new EmbedBuilder().setColor(new Color(26, 56, 176)).setTitle("Helpful information: ").setDescription(
				"**\\*cricket noises\\* ** Welp, nothing to see here... but let's look at some upcoming features!\n\nOur upcoming features are: "
						+ "\n• **Profanity Filter Level** (Low, Medium, High)" + "\n• **Unmute Command**"
						+ "\n• **Timed mutes and other forms of punishment**" + "\n\n**We are up for suggestions!**"
						+ "\nLet us know what you would like to see in the future by making a suggestion in our discord: https://discord.gg/w6RUKrK5SA")
				.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl()).setTimestamp(getTime(event)).build();

	}

	public OffsetDateTime getTime(GuildMessageReceivedEvent e) {
		return e.getMessage().getTimeCreated();
	}

	
}
