import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//      System.out.println(engine.search("бизнес"));

        int port = 8989;
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port);
                 Socket clientSocket = serverSocket.accept(); // ждем подключения
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String input = in.readLine();
                List<PageEntry> pageEntry = engine.search(input);
                out.println(listToJson(pageEntry));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String listToJson(List<PageEntry> list) {
        Type listType = new TypeToken<List<PageEntry>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return  json;
    }
}