package tk.bigpinglowiq.commands;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tk.bigpinglowiq.commands.music.IMusic;
import tk.bigpinglowiq.lavaPlayer.GuildMusicManager;
import tk.bigpinglowiq.lavaPlayer.PlayerManager;

import java.util.List;

public class RandomTeam extends ListenerAdapter implements IMusic {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {

        String[] message = e.getMessage().getContentRaw().split(" ");

        if(!message[0].equalsIgnoreCase("!!team")) return;


        TextChannel channel = e.getChannel();

        Member member = e.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this to work.").queue();
            return;
        }

        List<Member> members = member.getVoiceState().getChannel().getMembers();

        int teamA = 0;
        int teamB = 0;
        StringBuilder teamAS = new StringBuilder();
        StringBuilder teamBS = new StringBuilder();
        while(teamA<members.size()/2){
            if(Math.random()<=0.5){
                teamAS.append(members.get(teamA+teamB).getUser()).append('\n');
                teamA++;
            }else{
                teamBS.append(members.get(teamA+teamB).getUser()).append('\n');
                teamB++;
            }
        }
        while(teamA+teamB<members.size()){
            if(teamA>teamB){
                teamBS.append(members.get(teamA+teamB).getUser()).append('\n');
                teamB++;
            }else{
                teamAS.append(members.get(teamA+teamB).getUser()).append('\n');
                teamA++;
            }
        }

        e.getMessage().reply("Team 1:\n"+teamAS+"Team 2:\n"+teamBS).queue();


    }
}
