import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import listeners.RollEventListeners;
import javax.security.auth.login.LoginException;

public class SeedRoll
{
    private final ShardManager shardManager;
    private final Dotenv config;



    public SeedRoll() throws LoginException
    {
        // Load environment variables from .env
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.watching("for Rollplayers"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS);
        shardManager = builder.build();



        // Register listeners
        shardManager.addEventListener(new RollEventListeners());

    }

    public Dotenv getConfig()
    {
        return config;
    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }

    public static void main(String[] args)
    {
        try
        {
            SeedRoll seedRoll = new SeedRoll();
        } catch (LoginException e)
        {
            System.out.println("ERROR: Invalid Bot token.");
        }
    }
}
