import serializable.Message;
import serializable.User;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

            out.write("login yal ababab\n".getBytes());
            out.flush();

            Scanner scanner = new Scanner(in);
            String s = scanner.next();
            System.out.println(s);

            out.write("getUsers\n".getBytes(StandardCharsets.UTF_8));
            out.flush();

            List<User> users;

            try {
                users = (List<User>) in.readObject();
                System.out.println(users.getFirst().getUserName());
            } catch(ClassNotFoundException e) {
                System.err.println("ERROR: ошибка получения результата getUsers");
                return;
            }

            out.write("getMessagesInChat\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.writeObject(users.getFirst().getUserID());
            out.flush();

            List<Message> messages;

            try {
                messages = (List<Message>) in.readObject();
                System.out.println(messages);
            } catch(ClassNotFoundException e) {
                System.err.println("ERROR: ошибка получения результата getMessagesInChat");
            }

            Message message = new Message("yal", "yal", "Hello!");
            out.write("sendMessage\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.writeObject(message);
            out.flush();

            String response = scanner.next();
            System.out.println(response);

            out.write("getMessagesInChat\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.writeObject(users.getFirst().getUserID());
            out.flush();

            try {
                messages = (List<Message>) in.readObject();
                System.out.println(messages);
                messages.forEach(e -> System.out.println(e.getText()));
            } catch(ClassNotFoundException e) {
                System.err.println("ERROR: ошибка получения результата getMessagesInChat");
                return;
            }

            out.write("deleteMessage\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.writeObject(users.getFirst().getUserID());
            out.writeObject(messages.getFirst().getId());
            out.flush();

            response = scanner.next();
            System.out.println(response);

            out.write("getMessagesInChat\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.writeObject(users.getFirst().getUserID());
            out.flush();

            try {
                messages = (List<Message>) in.readObject();
                System.out.println(messages);
                messages.forEach(e -> System.out.println(e.getText()));
            } catch(ClassNotFoundException e) {
                System.err.println("ERROR: ошибка получения результата getMessagesInChat");
            }

            out.write("disconnect\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
        catch(OptionalDataException e) {
            e.printStackTrace();
            System.out.println(e.length);
        }
        catch(IOException e) {
            System.out.println("Ошибка установки соединения");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
