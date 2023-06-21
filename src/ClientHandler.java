import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
   public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
   private Socket socket;
   private BufferedReader bufferedReader;
   private PrintWriter printWriter;
   private String clientUsername;

   public ClientHandler(Socket socket){
       try{
           this.socket = socket;
           this.printWriter = new PrintWriter(socket.getOutputStream(), true);
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.clientUsername = bufferedReader.readLine();
           clientHandlers.add(this);
           broadcastMessage("SERVER: " + clientUsername + " has entered the chat");
       }catch(IOException e){
           closeEverything(socket, bufferedReader, printWriter);
           e.printStackTrace();
       }

   }
    @Override
    public void run() {
String messageFromClient;
while(socket.isConnected()){
    try{
        messageFromClient = bufferedReader.readLine();
        broadcastMessage(messageFromClient);
    }catch(IOException e) {
        closeEverything(socket, bufferedReader, printWriter);
    break;
    }

}
    }
    public void broadcastMessage(String messageToSend){
       for(ClientHandler clientHandler : clientHandlers){

               if(!clientHandler.clientUsername.equals(clientHandler)){
                   clientHandler.printWriter.println(messageToSend);

               }
//test
       }
    }
    public void removeClient(){
       clientHandlers.remove(this);
       broadcastMessage("SERVER: " + clientUsername + " has left the chat.");
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter){
       removeClient();
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
}
