package me.pray.listeners;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Check {

	static File file = new File("words.txt");

	//looping through the provided content to see if the content is the same as a blocked word in words.txt
	public static boolean checkForSwear(String content) {
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				if (content.equalsIgnoreCase(line)) {
					return true;
				}
			}
		} finally {
			it.close();
		}
		return false;
	}

	//loging the mute to a channel named "logs", creating one if one doesn't exist
	public static void logMute(GuildMessageReceivedEvent event, User userMuted) {
		if (event.getGuild().getTextChannelsByName("logs", true).isEmpty()) {
			event.getGuild().createTextChannel("logs").complete()
					.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
		}

		for (int i = 0; i < event.getGuild().getTextChannelsByName("logs", true).size(); i++) {
			event.getGuild().getTextChannelsByName("logs", true).get(i).sendMessageEmbeds(userMuted(event)).queue();
		}
	}

	//when a user is muted, this is the embed sent
	public static MessageEmbed userMuted(GuildMessageReceivedEvent event) {

		return new EmbedBuilder().setColor(Color.GRAY).setTitle("User muted: ")
				.setDescription("User: " + event.getAuthor().getAsMention() + "\nReason: **Using profanity**"
						+ "\nDuration: **Permanent**")
				.setThumbnail(event.getAuthor().getAvatarUrl())
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl()).setTimestamp(getTime(event)).build();

	}

	//getting the time of the messages creation
	public static OffsetDateTime getTime(GuildMessageReceivedEvent e) {
		return e.getMessage().getTimeCreated();
	}

}
