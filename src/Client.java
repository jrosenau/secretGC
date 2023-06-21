import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
private Socket socket;
private BufferedReader bufferedReader;
private PrintWriter printWriter;
private String username;
public Client(Socket socket, String username){
    try{
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.username = username;
    }catch(IOException e ){
        closeEverything(socket, bufferedReader, printWriter);
    }
}
public void sendMessage(){

        printWriter.println(username);
        Scanner scanner = new Scanner(System.in);
        while(socket.isConnected()){
            String messageToSend = scanner.nextLine();
            printWriter.println(username + ": " + messageToSend);
        }

}
public void listenForMessage(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                String messageFromGroupChat;
                while(socket.isConnected()){
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }).start();

    //test

}
    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter){
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(printWriter!=null){
                printWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username to connect: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 6666);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
