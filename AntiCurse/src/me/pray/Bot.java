package me.pray;

import javax.security.auth.login.LoginException;

import me.pray.listeners.CurseListener;
import me.pray.listeners.HelpCmd;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;

public class Bot {

	//constructor for bot
	public Bot() {
		//create a class named "Token" and make a public static String variable named "TOKEN" and set it to your token.
		JDABuilder builder = JDABuilder.createDefault(Token.TOKEN);
		builder.setChunkingFilter(ChunkingFilter.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_VOICE_STATES);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching("people swearing"));
		builder.addEventListeners(new CurseListener(), new HelpCmd());

		//surrounded with try catch so we don't need to add any throws to our psvm in our Main class
		try {
			builder.build().awaitReady();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
