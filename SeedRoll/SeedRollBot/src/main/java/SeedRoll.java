
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

public class SeedRoll extends ListenerAdapter {

    public static Database database;
    public static Dotenv config;
    public static JDA jda;

    public static void main(String[] args) {
        database = new Database();
        config = Dotenv.configure().directory("SeedRoll/SeedRollBot/.env").ignoreIfMissing().load();
        String token = config.get("TOKEN");
        jda = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("my sanity decline"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .build();
        jda.addEventListener(new SeedRoll());
        jda.updateCommands().addCommands(
                Commands.slash("passtime", "Use after a meeting to reset and set new priorities"),
                Commands.message("Log Reactions"),
                Commands.slash("selectplayers", "Selects x players to be assigned to a DnD session")
                        .addOption(OptionType.INTEGER, "number", "The number of players to select")
        ).queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains("SeedRoll")) {
            event.getChannel().sendMessage("I'm awake!").queue();
        }
    }

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (event.getName().equals("Log Reactions")) {
            MessageReaction userList = event.getTarget().getReaction(Emoji.fromUnicode("U+1F44D"));
            if (userList == null) {
                event.reply("No such reaction exists").setEphemeral(true).queue();
            } else {
                database.markParticipation(userList.retrieveUsers());
                event.reply("Reactions Logged!").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("selectplayers")) {
            if (database.noPlayerLeft()) {
                event.reply("No players remain to be assigned a game").queue();
            } else {
                String returnString = "Players selected for this game are: \n";
                int numPlayers = event.getOption("number").getAsInt();
                if (numPlayers >= database.size()) {
                    int size = database.size();
                    returnString += sendPlayers(size);
                } else {
                    returnString += sendPlayers(numPlayers);
                }
                event.reply(returnString + "Enjoy the Game!").queue();
            }
        }
        if (event.getName().equals("passtime")) {
            database.progressTime();
            event.reply("Players reset and priorities updated!").queue();
        }
    }

    public String sendPlayers(int size) {
        String returnString = "";
        for (int i = 0; i < size; i++) {
            String userID = database.selectPlayer();
            User user = jda.getUserById(userID);
            if (user != null) {
                returnString += user.getName() + "\n";
            } else {
                returnString += ("Invalid user ID: " + userID + "\n");
            }
        }
        return returnString;
    }
}