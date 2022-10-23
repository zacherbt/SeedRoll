package listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RollEventListeners extends ListenerAdapter
{
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event)
    {
        User user = event.getUser();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        String message = event.getMessage().getContentRaw();
        if(message.contains("SeedRoll"))
        {
            event.getChannel().sendMessage("Im awake!").queue();
        }
    }

}
