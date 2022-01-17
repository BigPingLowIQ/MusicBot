package tk.bigpinglowiq.lavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final ConcurrentHashMap<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new ConcurrentHashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandEvent e, String trackURL, boolean isURI) {
        GuildMusicManager musicManager = this.getMusicManager(e.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                MessageBuilder message = new MessageBuilder()
                        .append("Adding to queue: `")
                        .append(track.getInfo().title)
                        .append("` by `")
                        .append(track.getInfo().author)
                        .append("`");
                e.reply(message.build()).queue();

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                try {

                    if (isURI) {

                        MessageBuilder message = new MessageBuilder("Adding the following songs");

                        int count = 0;
                        for (AudioTrack track : tracks) {
                            if (count < 20) {
                                message.append("Adding to queue: `")
                                        .append(track.getInfo().title)
                                        .append("` by `")
                                        .append(track.getInfo().author)
                                        .append("`\n");
                            }
                            musicManager.scheduler.queue(track);
                            count++;

                        }
                        e.reply(message.build()).queue();

                        return;
                    }

                    AudioTrack track = tracks.get(0);
                    musicManager.scheduler.queue(track);

                    MessageBuilder message = new MessageBuilder("Adding to queue: `")
                            .append(track.getInfo().title)
                            .append("` by `")
                            .append(track.getInfo().author)
                            .append("`");
                    e.reply(message.build()).queue();

                } catch (IndexOutOfBoundsException ignored) {

                }


            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });

    }


    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public Set<Long> getGID() {
        Set<Long> ids = musicManagers.keySet();

        return Set.copyOf(ids);
    }

}
