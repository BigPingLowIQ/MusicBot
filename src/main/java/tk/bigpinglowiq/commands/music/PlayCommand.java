package tk.bigpinglowiq.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class PlayCommand extends ListenerAdapter implements IMusic{

//    @Override
//    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
//        String[] message = e.getMessage().getContentRaw().split(" ");
//
//        if(!message[0].equalsIgnoreCase("!!play")) {
//            return;
//        }
//
//
//        TextChannel channel = e.getChannel();
//        Member self = e.getGuild().getSelfMember();
//        Member member = e.getMember();
//        GuildVoiceState memberVoiceState = member.getVoiceState();
//        GuildVoiceState selfVoiceState = self.getVoiceState();
//
//        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
//        AudioPlayer audioPlayer = musicManager.audioPlayer;
//
//        if(audioPlayer.isPaused()){
//            audioPlayer.setPaused(false);
//            channel.sendMessage("Resumed the current track.").queue();
//            return;
//        }
//
//
//        if(!selfVoiceState.inVoiceChannel()){
//            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
//            return;
//        }
//
//
//        if(!memberVoiceState.inVoiceChannel()){
//            channel.sendMessage("You need to be in a voice channel for this to work.").queue();
//            return;
//        }
//
//        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
//            channel.sendMessage("You need to be in the same voice channel as me for this work").queue();
//            return;
//        }
//
//        if(message.length==1){
//            channel.sendMessage("Correct usage is `!!play <youtube link>`").queue();
//            return;
//        }
//
//        String link = String.join(" ", Arrays.copyOfRange(message,1,message.length));
//        boolean isUrl = true;
//        if(!isUrl(link)){
//            link = "ytsearch:" + link;
//            isUrl = false;
//        }
//
//        PlayerManager.getInstance().loadAndPlay(channel,link,isUrl,e());
//
//
//
//
//    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("play") && e.isFromGuild()) return;


        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        //-------------------------------------
        Member member = e.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        boolean isInVoice = e.getGuild().getSelfMember().getVoiceState().inVoiceChannel(); //is in voice channel?
        boolean recentJoinVc = false;
        if(!isUserVoice(e)){
            return;
        }else if(!isInVoice){
            AudioManager audioManager = e.getGuild().getAudioManager();
            VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
            recentJoinVc = true;
        }
        //-------------------------------------

        if(!recentJoinVc && !isSelfInMemberVoice(e)){
            return;
        }


        String link;
        try{
            link = e.getOption("song").getAsString();
        }catch (NullPointerException ignored){
            if(audioPlayer.isPaused()){
                audioPlayer.setPaused(false);
                e.reply("Resumed the current track.").queue();
            }else{
                e.reply("Nothing to do here.").queue();
            }
            return;
        }




        boolean isUrl = isUrl(link);

        if(!isUrl){
            link = "ytsearch:" + link;
            isUrl = false;
        }

        PlayerManager.getInstance().loadAndPlay(e,link,isUrl);



    }

    private boolean isUrl(String url){
        try{
            new URL(url);
            return true;
        }catch (MalformedURLException e){
            return false;
        }

    }
}
