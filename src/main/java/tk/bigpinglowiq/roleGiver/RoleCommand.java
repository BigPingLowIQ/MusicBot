package tk.bigpinglowiq.roleGiver;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RoleCommand extends ListenerAdapter {
    private static final String MODERATOR_ROLE_ID = "932718343323009044";
    private static final String MEMBER_ROLE_ID = "886230337196285993";
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split( " ");
        Member user = e.getMember();
        assert user != null;
        if(user.getRoles().stream().noneMatch((role)-> role.getId().equals(MODERATOR_ROLE_ID))){
         return;
        }
        //<@&880846186322489345>
        if(message.length == 1 && message[0].equals(">role")){
            e.getMessage().reply("Use \">role (tagged user)\"").queue();
            return;
        }
        if(message.length != 2)return;
        String targetId = extractUserId(message[1]);
        if(targetId==null)return;
        Member target = e.getGuild().getMemberById(targetId);
        e.getGuild().addRoleToMember(target, e.getGuild().getRoleById(MEMBER_ROLE_ID)).queue();
        e.getMessage().reply("Successfully added the role!").queue();
    }

    private String extractUserId(String s){
        if(!s.startsWith("<@!") && s.endsWith(">"))return null;
        return s.substring(3,s.length()-1);
    }
}
