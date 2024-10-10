# Discord Profanity Filter and Mute Bot

This Java-based Discord bot listens to messages in a guild and detects whether a user has said a blacklisted word. If a blacklisted word is detected, the bot mutes the user and logs the action to a specific location (a text channel named `logs`). The list of blacklisted words can either come from a custom database or a `words.txt` file.

## Features

- **Customizable Word Filtering**: The bot can filter messages based on custom blacklisted words stored in MongoDB or default words stored in a `words.txt` file.
- **Automatic User Mute**: When a user sends a message containing a blacklisted word, they are automatically muted.
- **Mute Logging**: Mutes are logged to a `logs` channel. If no such channel exists, the bot creates one.
- **Dynamic Filtering**: Supports two modes of word filtering:
  - **Custom Mode**: Retrieves blacklisted words from a MongoDB collection.
  - **Default Mode**: Uses blacklisted words from a local file (`words.txt`).

## Installation

### Prerequisites

- Java 11 or higher
- MongoDB instance (for storing custom blacklisted words)
- `words.txt` file containing blacklisted words (one word per line)
- A running Discord bot using the [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)

### Libraries Used

- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA) for interacting with Discord.
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/) for handling file input and output.
- [MongoDB Java Driver](https://mongodb.github.io/mongo-java-driver/) for database interaction.

### Setup

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/discord-profanity-filter-bot.git

2. Install required libraries via Maven or manually include the dependencies in your project:
```xml
<dependencies>
    <dependency>
        <groupId>net.dv8tion</groupId>
        <artifactId>JDA</artifactId>
        <version>5.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.11.0</version>
    </dependency>
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongodb-driver-sync</artifactId>
        <version>4.7.0</version>
    </dependency>
</dependencies>
```

3. Set up your words.txt file in the root directory. This file will hold the default blacklisted words (one word per line). Example:
```txt
badword1
badword2
offensivephrase
```

4. Configure MongoDB connection if you want to use custom blacklisted words. Ensure your MongoDB instance has a collection for guilds with fields GuildId, BannedWords, and FilterType (set to "custom" for custom filtering).

  * *This feature was specifically added to practice usage of mongodb, and was useful if hosting the bot for multiple users; however, if cloning locally just set the words you want blacklisted in the words.txt, and it will work as intended. This restricts changing the filtering mode.* 

5. Run the bot with your Discord token by editing the Bot.java class to include your bot token in the JDA initialization:
```java
JDA jda = JDABuilder.createDefault("YOUR_DISCORD_BOT_TOKEN").build();
```

# Usage
Once the bot is running in your Discord server:

1. Users' messages are automatically checked for blacklisted words.
2. If a blacklisted word is found:
* The user is muted.
* The mute action is logged in the logs channel (the bot will create this channel if it doesn't exist).
3. The logs will show the muted user's information, the blacklisted word they used, and the full message content (if provided).
# Example Logs
The bot sends an embedded message to the `logs` channel:

```markdown
User muted:
- User: @username
- Reason: **Using profanity**
- Duration: **Permanent**
- Blocked Word: ||badword||
- Full Message: link-to-message (if available)
```

# Customizing Filter Behavior
* Custom Filtering Mode: If the MongoDB document for the guild has "FilterType": "custom", the bot will use the blacklisted words stored in the BannedWords field.
* Default Filtering Mode: If the MongoDB document does not exist or does not specify "custom", the bot will use the words from the words.txt file.

# Logging Mutes
Whenever a user is muted, the bot sends an embedded message to a text channel named `logs`. If this channel doesn't exist, the bot creates it and configures it so that regular users cannot view it.

# Contributing
Feel free to open issues or submit pull requests with improvements or bug fixes. Contributions are always welcome!
