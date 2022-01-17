package tk.bigpinglowiq.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;

public class PauseCommand extends ListenerAdapter implements IMusic{

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(!message[0].equalsIgnoreCase("!!pause")) {
            return;
        }

        TextChannel channel = e.getChannel();
        Member self = e.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I need to be in a voice channel for this to work").queue();
            return;
        }

        Member member = e.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this to work.").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("You need to be in the same voice channel as me for this work").queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.isPaused()){
            audioPlayer.setPaused(false);
            channel.sendMessage("Resumed the current track.").queue();
            return;
        }
        audioPlayer.setPaused(true);
        channel.sendMessage("Paused the current track.").queue();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("pause") && e.isFromGuild()) return;

        if(isSelfNeedVoice(e) || !isSelfInMemberVoice(e)){
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.isPaused()){
            audioPlayer.setPaused(false);
            e.reply("Resumed the current track.").queue();
            return;
        }
        audioPlayer.setPaused(true);
        e.reply("Paused the current track.").queue();
    }
}
