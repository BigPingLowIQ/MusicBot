package tk.bigpinglowiq.commands.music;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StopCommand extends ListenerAdapter implements IMusic{

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("!!stop")){

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
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();
            musicManager.scheduler.repeating = false;

            channel.sendMessage("The player has been stopped and the queue has been cleared.").queue();

        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("stop") && e.isFromGuild()) return;

        if(isSelfNeedVoice(e) || !isSelfInMemberVoice(e)){
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.repeating = false;

        e.reply("The player has been stopped and the queue has been cleared.").queue();

    }
}
