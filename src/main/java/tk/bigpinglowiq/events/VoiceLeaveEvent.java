package tk.bigpinglowiq.events;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;

import java.util.List;

public class VoiceLeaveEvent implements EventListener {


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof GuildVoiceLeaveEvent){
            GuildVoiceLeaveEvent e = (GuildVoiceLeaveEvent) event;

            GuildVoiceState vs = e.getGuild().getSelfMember().getVoiceState();

            if(vs == null || !vs.inVoiceChannel()){
                return;
            }
            if(!vs.getChannel().getId().equals(e.getChannelLeft().getId())){
                return;
            }

            List<Member> members = e.getChannelLeft().getMembers();


            if(members.size()==1){
                AudioManager manager = e.getGuild().getAudioManager();
                manager.closeAudioConnection();

                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
                musicManager.scheduler.player.stopTrack();
                musicManager.scheduler.queue.clear();
                musicManager.scheduler.repeating = false;

            }



        }
    }
}
