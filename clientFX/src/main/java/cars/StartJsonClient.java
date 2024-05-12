package cars;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClient extends Application {
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

//        Properties clientProps = new Properties();
//        try
//        {
//            clientProps.load(StartJsonClient.class.getResourceAsStream("/client.properties"));
//            System.out.println("Client properties set. ");
//            clientProps.list(System.out);
//        }
//        catch (IOException e)
//        {
//            System.err.println("Cannot find client.properties " + e);
//            return;
//        }
//
//        String serverIP = clientProps.getProperty("server.host", defaultServer);
//        int serverPort = defaultChatPort;
//        try
//        {
//            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
//        }
//        catch (NumberFormatException ex)
//        {
//            System.err.println("Wrong port number " + ex.getMessage());
//            System.out.println("Using default port: " + defaultChatPort);
//        }
//        System.out.println("Using server IP " + serverIP);
//        System.out.println("Using server port " + serverPort);

        primaryStage.show();
    }
}
