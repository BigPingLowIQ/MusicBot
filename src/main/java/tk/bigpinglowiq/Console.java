package tk.bigpinglowiq;

import java.util.Scanner;

public class Console extends Thread{

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);

        while(true){
            String input = scanner.nextLine();
            switch (input){
                case "1":
            }
        }
    }
}
