package cars;

import cars.gui.LoginController;
import cars.gui.UserController;
import cars.jsonProtocol.ServiceProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class StartJsonClient extends Application {
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        Properties clientProps = new Properties();
        try
        {
            clientProps.load(StartJsonClient.class.getResourceAsStream("/client.props"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        }
        catch (IOException e)
        {
            System.err.println("Cannot find transportclient.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("transport.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try
        {
            serverPort = Integer.parseInt(clientProps.getProperty("transport.server.port"));
        }
        catch (NumberFormatException ex)
        {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        Socket socket = new Socket(serverIP, serverPort);
        var output = new PrintWriter((socket).getOutputStream());
        var input = new BufferedReader(new InputStreamReader((socket).getInputStream()));
        IService server = new ServiceProxy(socket, input, output);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        LoginController loginController = fxmlLoader.getController();
        loginController.initController(server);

        FXMLLoader fxmlLoaderUser = new FXMLLoader(getClass().getClassLoader().getResource("user-view.fxml"));
        UserController userController = fxmlLoaderUser.getController();
        loginController.setUserController(userController);

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
