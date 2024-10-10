package me.pray.cmds;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import com.mongodb.client.model.Updates;

import me.pray.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DenyUsage extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var content = event.getMessage().getContentRaw();
		String[] args = content.split("\\s+");
		String redX = "<:RED_CROSS:895872167080783892>";
		String greenCheck = "<:GREEN_CHECK:895872055436791819>";
		String warning = "<:Down:895893224403652619>";
		var query = eq("GuildId", event.getGuild().getIdLong());
		var first = Bot.sbw.find(query).first();

		//checking if the first argument is $deny
		if (args[0].equalsIgnoreCase("$deny")) {
			
			//if the user doesn't have admin, send no perm msg
			if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(redX + "No permission.").queue();
				return;
			}

			//if the arguments are not correct, send invalid format msg
			if (args.length != 2) {
				event.getChannel().sendMessage(redX + "Invalid format: `$deny [word]`").queue();
				return;
			}
			
			//getting the list from mongodb
			List<String> list = first.getList("BannedWords", String.class);

			//checking if the list already contains the word to ban
			if (list.contains(args[1])) {
				event.getChannel().sendMessage(redX + "You have already denied the word: " + args[1]).queue();
				return;
			}

			//updating mongodb
			var update = Updates.push("BannedWords", args[1]);
			Bot.sbw.updateOne(query, update);
			
			//success message
			event.getChannel().sendMessage(greenCheck + "Success! Banned the word " + args[1]).queue();

			//if they aren't set to custom, let them know
			if (!first.getString("FilterType").equalsIgnoreCase("custom")) {
				event.getChannel().sendMessage(warning + "Warning: your server's filter is set to `"
						+ first.getString("FilterType")
						+ "`. This means the denied word will not be filtered unless you run the command: `$filter custom`"
						+ warning).queue();
			}

		}
	}

}
