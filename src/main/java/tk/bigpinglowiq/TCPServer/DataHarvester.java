package tk.bigpinglowiq.TCPServer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import tk.bigpinglowiq.Application;
import tk.bigpinglowiq.Main;

import java.util.ArrayList;
import java.util.List;

class DataHarvester extends Thread{

    private boolean loop;
    private List<Guild> guilds;

    @Override
    public void run() {

        loop = true;
        guilds = new ArrayList<>();
        while(loop){
            JDA jda = Main.getJDA();
            if(jda!=null){
                guilds = jda.getGuilds();
            }



            try {Thread.sleep(30000);} catch (InterruptedException e) {e.printStackTrace();}
        }

    }

    public void shutdown(){
        loop = false;
    }

    public List<Guild> getGuilds(){
        return guilds;
    }


}
