package dz.esi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    ServerSocket server;
    ArrayList<Socket> socketList=new ArrayList<>();
    ArrayList<Thread> threads=new ArrayList<>();

    public Server(int port) throws IOException{
        server=new ServerSocket(port);

    }

    public void start() throws IOException{
        while(true){

            Socket socket=server.accept();

            String dest=socket.getRemoteSocketAddress().toString().split("/")[1].split(":")[0];
            for (Socket s:socketList) {
                String des1=s.getRemoteSocketAddress().toString().split("/")[1].split(":")[0];
                if(des1.trim().equals(dest.trim())) {
                    s.close();
                    socketList.remove(s);
                }
            }
            socketList.add(socket);
            Runnable run=()->read(socket);
            Thread th=new Thread(run);
            threads.add(th);
            th.start();
        }
    }

    private void read(Socket socket) {
        loop:
        while (true) {
            System.out.println("reading .... " + socket);
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String msg = "";
                while (msg.length() < 312) {
                    char c= (char)in.read();
                    //if(c==-1)throw new Exception("socket closed");
                    msg+=c;
                }
                System.out.println(msg);
                if (msg == null) throw new NullPointerException("msg is null");
                XMLMsgHandler xml = new XMLMsgHandler(msg, socket);
                msg = xml.format();

                InetAddress a = xml.getDestination();
                Socket dest = null;
                for (Socket ss : socketList) {
                    if (InetAddress.getByName(ss.getRemoteSocketAddress().toString()
                            .split("/")[1].split(":")[0]).
                            equals(a)) {
                        dest = ss;
                        break;
                    }
                }
                if (dest == null) continue loop;
                PrintWriter out = new PrintWriter(dest.getOutputStream());
                String s = msg;

                new Thread(() -> {
                    out.println(s);
                    out.flush();
                }).start();
                System.out.println(s);
            } catch (Exception e) {
                System.out.println(socket.isClosed());
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
        }


    }
    public void shutdown(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Thread th:threads)th.stop();
        for(Socket s:socketList) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

