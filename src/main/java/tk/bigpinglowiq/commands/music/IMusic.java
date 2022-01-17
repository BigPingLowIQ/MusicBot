package tk.bigpinglowiq.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IMusic {

    // returns true if the bot is in a voice channel
    default boolean isSelfVoice(SlashCommandEvent e){
        boolean b = e.getGuild().getSelfMember().getVoiceState().inVoiceChannel();
        if(b){
            e.reply("I'm already in a voice channel").queue();
            return true;
        }
        return false;
    }

    default boolean isSelfNeedVoice(SlashCommandEvent e){
        boolean b = e.getGuild().getSelfMember().getVoiceState().inVoiceChannel();
        if(!b){
            e.reply("I need to be in a voice channel for this to work").queue();
            return true;
        }
        return false;
    }

    // returns true if the member is in a voice channel
    default boolean isUserVoice(SlashCommandEvent e){
        boolean b = e.getMember().getVoiceState().inVoiceChannel();
        if(!b){
            e.reply("You need to be in a voice channel for this command to work.").queue();
            return false;
        }
        return true;
    }

    default boolean isSelfInMemberVoice(SlashCommandEvent e){
        GuildVoiceState memberVoiceState = e.getMember().getVoiceState();
        if(memberVoiceState.getChannel()!=null && e.getGuild().getSelfMember().getVoiceState().getChannel().equals(memberVoiceState.getChannel())){
            return true;
        }
        e.reply("You need to be in the same voice channel as me for this work").queue();
        return false;
    }



}
