package tk.bigpinglowiq.commands.music;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;

import java.util.Objects;

public class SeekCommand extends ListenerAdapter implements IMusic {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(!message[0].equalsIgnoreCase("!!seek")){
            return;
        }
        if(message.length==1){
            e.getMessage().reply("Example use: `!!seek 2s 4m 1h`, or `!!seek 2s 4m`...").queue();
            return;
        }
        long time = parseTime(message);


        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());

        long duration = musicManager.audioPlayer.getPlayingTrack().getDuration();
        if(time > duration || time < 0){
            e.getMessage().reply("Position is out of bounds.").queue();
            return;
        }
        musicManager.audioPlayer.getPlayingTrack().setPosition(time);
        e.getMessage().reply("Seeking to "+formatTime(time)).queue();
        // !!seek 1h 7m 10s

    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("seek") && e.isFromGuild()) return;

        if(isSelfNeedVoice(e) || !isSelfInMemberVoice(e)){
            return;
        }
//        long time = e.getOptionsByName("seconds").;
//        e.getOptionsByName("minutes")
//                e.getOptionsByName("hours")
        long seconds,minutes,hours;
        try {
             seconds = Objects.requireNonNull(e.getOption("seconds")).getAsLong();
        }catch (NullPointerException ignored){
            seconds = 0;
        }
        try{
            minutes = Objects.requireNonNull(e.getOption("minutes")).getAsLong();
        }catch (NullPointerException ignored){
            minutes = 0;
        }
        try{
            hours = Objects.requireNonNull(e.getOption("hours")).getAsLong();
        }catch (NullPointerException ignored){
            hours = 0;
        }
        long time = seconds + minutes*60 + hours*3600;
        time*=1000;
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());

        long duration = musicManager.audioPlayer.getPlayingTrack().getDuration();
        if(time > duration || time < 0){
            e.reply("Position is out of bounds.").queue();
            return;
        }
        musicManager.audioPlayer.getPlayingTrack().setPosition(time);
        e.reply("Seeking to "+formatTime(time)).queue();
    }

    private long parseTime(String[] time){
        int sec = 0;
        for(int i = 1;i<time.length;i++){
            String s = time[i];
            String sTime = s.substring(0,s.length()-1);
            String unit = s.substring(s.length()-1);
            int temp;
            try{
                temp = Integer.parseInt(sTime);
            }catch (NumberFormatException ignored){
                return -1;
            }
            switch (unit) {
                case "s":
                    sec += temp;
                    break;
                case "m":
                    sec += temp * 60;
                    break;
                case "h":
                    sec += temp * 3600;
                    break;
                default:
                    return -1;
            }
        }
        if(sec<=0)
            return -1;
        return sec* 1000L;
    }
    private String formatTime(long millis){
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        if(hour==0)
            return String.format("%02d:%02d", minute, second);
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
