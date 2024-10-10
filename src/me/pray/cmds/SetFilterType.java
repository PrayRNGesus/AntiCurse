package me.pray.cmds;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.model.Updates;

import me.pray.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SetFilterType extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var content = event.getMessage().getContentRaw();
		String[] args = content.split("\\s+");
		String redX = "<:RED_CROSS:895872167080783892>";
		String greenCheck = "<:GREEN_CHECK:895872055436791819>";
		var query = eq("GuildId", event.getGuild().getIdLong());

		// checking if the first argument is $filter
		if (args[0].equalsIgnoreCase("$filter")) {

			// if the user doesn't have admin, send no perm msg
			if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage(redX + "No permission.").queue();
				return;
			}

			if (args.length != 2) {
				event.getChannel().sendMessage(redX + "Invalid format: `$filter [custom|high]`").queue();
				return;
			}

			if (args[1].equalsIgnoreCase("custom")) {
				var update = Updates.set("FilterType", "Custom");
				Bot.sbw.updateOne(query, update);
				event.getChannel().sendMessage(greenCheck
						+ "Set filter type to custom. To allow or deny words, use `$allow [word]` or `$deny [word]`")
						.queue();
				return;
			}

			if (args[1].equalsIgnoreCase("high")) {
				var update = Updates.set("FilterType", "High");
				Bot.sbw.updateOne(query, update);
				event.getChannel().sendMessage(greenCheck + "Set filter type to high.").queue();
				return;
			}

		}
	}

}
