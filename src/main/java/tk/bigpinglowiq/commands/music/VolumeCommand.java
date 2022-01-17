package tk.bigpinglowiq.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VolumeCommand extends ListenerAdapter implements IMusic{

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("!!volume")) {
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


            if(message.length!=2){
                channel.sendMessage("Correct usage is `!!volume [1-100]`").queue();
                return;
            }
            int volume;
            try {
                volume = Integer.parseInt(message[1]);

                if(volume>1000 || volume < 1){
                    channel.sendMessage("Correct usage is `!!volume [1-100]`").queue();
                    return;
                }else{
                    GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
                    musicManager.audioPlayer.setVolume(volume);
                }

            }catch (NumberFormatException ignored){
                channel.sendMessage("Correct usage is `!!volume [1-100]`").queue();
                return;
            }


        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        if(!e.getName().equals("volume") && e.isFromGuild()) return;

        OptionMapping volumeOption = e.getOption("volume");
        int volume;
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
        try {
            volume = (int) volumeOption.getAsDouble();
        }catch (NullPointerException ignored){
            e.reply("Current volume is " + musicManager.audioPlayer.getVolume()).queue();
            return;
        }


        if(isSelfNeedVoice(e) || !isSelfInMemberVoice(e)){
            return;
        }


        if(volume>1000 || volume < 1){
            e.reply("Please enter a value 1-100").queue();
            return;
        }


        musicManager.audioPlayer.setVolume(volume);
        e.reply("Done.").queue();

    }
}
