package tk.bigpinglowiq;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import tk.bigpinglowiq.commands.HelpCommand;
import tk.bigpinglowiq.commands.RandomTeam;
import tk.bigpinglowiq.commands.music.*;
import tk.bigpinglowiq.events.VoiceLeaveEvent;
import tk.bigpinglowiq.roleGiver.RoleCommand;

import javax.security.auth.login.LoginException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

public class Application {
    private JDA jda;
    private final String key;
    private boolean isCrashed = false;

    public Application(String apiKey) {
        key=apiKey;
    }

    public void start() throws ErrorResponseException {

        JDABuilder builder = JDABuilder.createDefault(key);
        builder.addEventListeners(
                new JoinCommand(),
                new PlayCommand(),
                new StopCommand(),
                new SkipCommand(),
                new NowPlaying(),
                new QueueCommand(),
                new RepeatCommand(),
                new LeaveCommand(),
                new VolumeCommand(),
                new HelpCommand(),
                new PauseCommand(),
                new ShuffleCommand(),
                new VoiceLeaveEvent(),
                new SeekCommand(),
                new RoleCommand(),
                new RandomTeam());
        builder.setAutoReconnect(true);
        builder.setMaxReconnectDelay(32);
        builder.setActivity(Activity.listening("manele"));

        try{
            jda = builder.build();
            isCrashed = false;


            jda.updateCommands().addCommands(
                    new CommandData("join","Joins your voice channel."),
                    new CommandData("leave","Disconnects the bot from the channel."),
                    new CommandData("now","Displays details about the current song."),
                    new CommandData("pause","Pauses or resumes the current song."),
                    new CommandData("play","Plays a song or a playlist.").addOption(OptionType.STRING,"song","Use the song name or a link"),
                    new CommandData("queue","Displays the queue."),
                    new CommandData("repeat","Repeats the current song."),
                    new CommandData("shuffle","Shuffles the queue."),
                    new CommandData("skip","Skips the current song."),
                    new CommandData("seek","Skips the song to a desired time")
                            .addOption(OptionType.INTEGER,"seconds","Seconds",false)
                            .addOption(OptionType.INTEGER,"minutes","Minutes",false)
                            .addOption(OptionType.INTEGER,"hours","Hours",false),
                    new CommandData("stop","Stops from playing and clears the queue."),
                    new CommandData("volume","Sets the volume.").addOption(OptionType.INTEGER,"volume","1-100")
            ).queue();

            jda.awaitReady();

        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            System.out.println("APIKey invalid, quitting.");
        } catch (ErrorResponseException e) {
            e.printStackTrace();
            System.out.println("Bot has crashed, that sucks!");
            try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("crashlog.txt", true))) {

                StringBuilder toWrite = new StringBuilder();
                toWrite.append("Log\n")
                        .append(Instant.now().toString())
                        .append('\n')
                        .append(e.getMessage())
                        .append('\n');

                for (StackTraceElement element : e.getStackTrace()) {
                    toWrite.append(element.toString()).append("\n");
                }

                toWrite.append("\n");
                output.write(toWrite.toString().getBytes());


            } catch (IOException e2) {
                e.printStackTrace();
            }

            close();
            isCrashed = true;

        }

    }

    public void close(){
        jda.shutdown();
    }

    public void restart(){
        close();
        start();
    }

    public boolean isCrashed() {
        return isCrashed;
    }

    public JDA getJDA(){
        return jda;
    }

}
