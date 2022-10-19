import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class SeedRoll
{
    private final ShardManager shardManager;
    private final Dotenv config;



    public SeedRoll() throws LoginException
    {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.watching("for Rollplayers"));
        shardManager = builder.build();
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
