package me.pray.listeners;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.mongodb.client.model.Filters;

import me.pray.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Check {

	static File file = new File("words.txt");

	// looping through the provided content to see if the content is the same as a
	// blocked word in words.txt
	public static boolean checkForSwear(String content, GuildMessageReceivedEvent event) {
		var query = Filters.eq("GuildId", event.getGuild().getIdLong());
		List<String> bannedWords = Bot.sbw.find(query).first().getList("BannedWords", String.class);
		
		if (Bot.sbw.find(query).first().getString("FilterType").equalsIgnoreCase("custom")) {
			if (bannedWords.contains(content)) {
				return true;
			}
		} else {
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
		}

		return false;
	}

	// loging the mute to a channel named "logs", creating one if one doesn't exist
	public static void logMute(GuildMessageReceivedEvent event, User userMuted, String blockedWord, String link) {
		if (event.getGuild().getTextChannelsByName("logs", true).isEmpty()) {
			event.getGuild().createTextChannel("logs").complete()
					.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
		}

		for (int i = 0; i < event.getGuild().getTextChannelsByName("logs", true).size(); i++) {
			event.getGuild().getTextChannelsByName("logs", true).get(i)
					.sendMessageEmbeds(userMutedWithLink(event, blockedWord, link)).queue();
		}
	}

	// when a user is muted, this is the embed sent
	public static MessageEmbed userMutedWithLink(GuildMessageReceivedEvent event, String blockedWord, String link) {

		return new EmbedBuilder().setColor(Color.GRAY).setTitle("User muted: ")
				.setDescription("User: " + event.getAuthor().getAsMention() + "\nReason: **Using profanity**"
						+ "\nDuration: **Permanent**" + "\nBlocked Word: ||" + blockedWord + "||" + "\nFull Message: "
						+ link)
				.setThumbnail(event.getAuthor().getAvatarUrl())
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl())
				.setTimestamp(getTime(event)).build();

	}

	public static MessageEmbed userMutedWithoutLink(GuildMessageReceivedEvent event, String blockedWord) {

		return new EmbedBuilder().setColor(Color.GRAY).setTitle("User muted: ")
				.setDescription("User: " + event.getAuthor().getAsMention() + "\nReason: **Using profanity**"
						+ "\nDuration: **Permanent**" + "\nBlocked Word: ||" + blockedWord + "||")
				.setThumbnail(event.getAuthor().getAvatarUrl())
				.setFooter("Powered By: @Pray#0001", event.getJDA().getSelfUser().getAvatarUrl())
				.setTimestamp(getTime(event)).build();

	}

	// getting the time of the messages creation
	public static OffsetDateTime getTime(GuildMessageReceivedEvent e) {
		return e.getMessage().getTimeCreated();
	}

}
