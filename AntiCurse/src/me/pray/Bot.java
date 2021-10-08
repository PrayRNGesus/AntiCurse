package me.pray;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import me.pray.cmds.AllowUsage;
import me.pray.cmds.DenyUsage;
import me.pray.cmds.HelpCmd;
import me.pray.cmds.SetFilterType;
import me.pray.listeners.CurseListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;

public class Bot {
	
	public MongoClient mongoClient;
	public MongoDatabase db;
	//short for specificBannedWords
	public MongoCollection<Document> sbw;
	
	//constructor for bot
	public Bot() {
		JDA jda = null;
		
		//create a class named "Token" and make a public static String variable named "TOKEN" and set it to your token.
		JDABuilder builder = JDABuilder.createDefault(HiddenInfo.TOKEN);
		builder.setChunkingFilter(ChunkingFilter.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_VOICE_STATES);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching("people swearing"));
		builder.addEventListeners(new CurseListener(), new HelpCmd(), new DenyUsage(this), new AllowUsage(this), new SetFilterType(this));

		//surrounded with try catch so we don't need to add any throws to our psvm in our Main class
		try {
			jda = builder.build().awaitReady();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			loadDb(jda);
		} catch (NullPointerException exc) {
			System.out.println("Shutting down, bot cannot perform properly without a loaded database");
			System.exit(0);
		}
		
	}
	
	//if you don't understand this then you should probably research more mongodb
	public void loadDb(JDA jda) {
		if(jda == null) {
			throw new NullPointerException("JDA variable is null");
		}
		
		ConnectionString connectionString = new ConnectionString("mongodb+srv://" + HiddenInfo.MONGOUSERNAME + ":" + HiddenInfo.MONGOPWD + "@cluster0.bulay.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		
		mongoClient = MongoClients.create(settings);
		db = mongoClient.getDatabase("AntiCurse");
		sbw = db.getCollection("SpecificBannedWords");
		
		for(Guild guild : jda.getGuilds()) {
			var query = eq("GuildId", guild.getIdLong()); 
			if(sbw.find(query).first() == null) {
				Document doc = new Document();
				doc.append("GuildId", guild.getIdLong());
				ArrayList<String> bannedWordsList = new ArrayList<>();
				doc.append("BannedWords", bannedWordsList);
				doc.append("FilterType", "High");
				sbw.insertOne(doc);
			}
		}
		
	}
	
}
