package tk.bigpinglowiq.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JoinCommand extends ListenerAdapter implements IMusic {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("!!join")){

            TextChannel channel = e.getChannel();
            Member self = e.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if(selfVoiceState.inVoiceChannel()){
                channel.sendMessage("I'm already in a voice channel").queue();
                return;
            }

            Member member = e.getMember();
            GuildVoiceState memberVoiceState = member.getVoiceState();

            if(!memberVoiceState.inVoiceChannel()){
                channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
                return;
            }

            AudioManager audioManager = e.getGuild().getAudioManager();
            VoiceChannel memberChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberChannel);
            channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`",memberChannel.getName()).queue();

        }


    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if (!e.getName().equals("join") && e.isFromGuild()) return; // make sure we handle the right command


        Member member = e.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(isSelfVoice(e) || !isUserVoice(e)){
            return;
        }

        AudioManager audioManager = e.getGuild().getAudioManager();
        VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);

        e.replyFormat("Connecting to `\uD83D\uDD0A %s`",memberChannel.getName()).queue();
    }



}
