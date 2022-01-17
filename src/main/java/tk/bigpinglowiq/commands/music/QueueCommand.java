package tk.bigpinglowiq.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand extends ListenerAdapter implements IMusic{

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("!!queue")){

            TextChannel channel = e.getChannel();
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
            BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

            if(queue.isEmpty()){
                channel.sendMessage("The queue is empty.").queue();
                return;
            }

            int trackCount = Math.min(queue.size(),20);
            ArrayList<AudioTrack> trackList = new ArrayList<>(queue);
            MessageAction messageAction = channel.sendMessage("**Current queue:**\n");
            for(int i = 0 ; i<trackCount ; i++){
                AudioTrack track = trackList.get(i);
                AudioTrackInfo info = track.getInfo();

                messageAction.append("#")
                        .append(String.valueOf(i+1))
                        .append(" `")
                        .append(info.title)
                        .append(" by ")
                        .append(info.author)
                        .append("` [`")
                        .append(formatTime(track.getDuration()))
                        .append("`]\n");
            }

            if(trackList.size()>trackCount){
                messageAction.append("And `")
                        .append(String.valueOf(trackList.size()-trackCount))
                        .append("` more...");
            }

            messageAction.queue();

        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("queue") && e.isFromGuild()) return;

        if(isSelfNeedVoice(e)){
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.isEmpty()){
            e.reply("The queue is empty.").queue();
            return;
        }

        int trackCount = Math.min(queue.size(),20);
        ArrayList<AudioTrack> trackList = new ArrayList<>(queue);
        StringBuilder replyMessage = new StringBuilder("**Current queue:**\n");
        for(int i = 0 ; i<trackCount ; i++){
            AudioTrack track = trackList.get(i);
            AudioTrackInfo info = track.getInfo();

            replyMessage.append("#")
                    .append(i + 1)
                    .append(" `")
                    .append(info.title)
                    .append(" by ")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if(trackList.size()>trackCount){
            replyMessage.append("And `")
                    .append(trackList.size() - trackCount)
                    .append("` more...");
        }

        e.reply(replyMessage.toString()).queue();

    }

    private String formatTime(long duration) {
        long hours = duration / TimeUnit.HOURS.toMillis(1);
        long minutes = duration / TimeUnit.MINUTES.toMillis(1);
        long seconds = duration % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }




}
