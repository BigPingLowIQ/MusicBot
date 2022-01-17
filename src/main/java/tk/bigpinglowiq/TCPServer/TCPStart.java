package tk.bigpinglowiq.TCPServer;

public class TCPStart {
    private static DataHarvester dh;


    public static void init(){
        dh = new DataHarvester();
        new Thread(dh).start();
        new Thread(new Server()).start();

    }

    public static DataHarvester getDH(){
        return dh;
    }

    public static void shutdown(){
        dh.shutdown();
    }
}
