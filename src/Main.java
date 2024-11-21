import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static final String address = "localhost";
    public static final int port = 23456;

    ObjectOutputStream out;
    ObjectInputStream in;

    public Main() {
        try (Socket socket = new Socket(address, port)){

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Соединение успешно установлено!");

            out.write("createUser kane 123456\n".getBytes());
            out.flush();

            Scanner scanner = new Scanner(in);
            String s = scanner.next();
            System.out.println(s);
            s = scanner.next();
            System.out.println(s);

        } catch(IOException e) {
            System.out.println("Ошибка установки соединения");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
