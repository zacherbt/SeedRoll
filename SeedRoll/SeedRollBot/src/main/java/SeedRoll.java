import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class SeedRoll
{
    public static void main(String[] args) throws LoginException
    {
        // replace token with better implementation outside of source code
        final String TOKEN = "Token";
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);

        jdaBuilder.build();



    }
}
