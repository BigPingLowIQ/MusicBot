package tk.bigpinglowiq.dataBase;

import javax.persistence.*;

@Entity
@Table(name = "GuildData")
public class GuildData {
    @Id
    private Long guild_id;
    @Column(name="user_id",nullable = false)
    private Long user_id;

    public Long getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(Long guild_id) {
        this.guild_id = guild_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
