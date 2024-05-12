package cars.gui;

import cars.IService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private IService service;

    private UserController userController;

    @FXML
    private TextField textField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label incorrectLabel;

    public void initController(IService service)
    {
        this.service = service;
        incorrectLabel.setVisible(false);
    }
}
