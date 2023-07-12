import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String UserName;



    public Client(Socket socket, String userName){


        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))));
            this.bufferedReader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            this.UserName = userName;

        }catch (IOException e){
            closeEveryThing(socket, bufferedWriter, bufferedReader);
        }

    }


    public void sendMessage(){
        try {
            bufferedWriter.write(UserName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messsageToSend = scanner.nextLine();
                bufferedWriter.write(UserName + ":" + messsageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e ){
            closeEveryThing(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException e ){
                        closeEveryThing(socket, bufferedWriter, bufferedReader);
                    }

                }
            }
        }).start();
    }

    public void closeEveryThing(Socket socket , BufferedWriter bufferedWriter, BufferedReader bufferedReader ){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }

        } catch(IOException e ){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat:");
        String username =scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();




    }
}
