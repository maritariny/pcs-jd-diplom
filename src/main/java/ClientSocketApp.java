import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketApp {

    public static void main(String[] args) {

        String host = "netology.homework";
        int port = 8989;

        try (Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("бизнес");

            String resp = in.readLine();
            System.out.println(resp);

        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
