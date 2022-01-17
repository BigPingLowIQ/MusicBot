package tk.bigpinglowiq.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("!!help")){

            TextChannel channel = e.getChannel();
            MessageAction messageAction = channel.sendMessage("```Commands\n");
            messageAction
                    .append("!!join - makes the bot join your voice channel\n")
                    .append("!!leave - makes the bot leave your voice channel\n")
                    .append("!!nowplaying - shows the currently palying track\n")
                    .append("!!play (song)- play music\n")
                    .append("!!queue - shows the queue of songs\n")
                    .append("!!repeat - repeats the current song\n")
                    .append("!!skip - skips the current song\n")
                    .append("!!stop - stops the music and deletes the queue\n")
                    .append("!!leave - makes the bot leave your voice channel\n")
                    .append("!!volume (1-200) - sets the volume```").queue();


        }

    }
}
