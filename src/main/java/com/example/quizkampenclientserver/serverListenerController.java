package com.example.quizkampenclientserver;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class serverListenerController implements Initializable
{
    int port = 55557;
    @FXML
    private Label welcomeText;

    public void setPort(int port)
    {
        this.port = port;
    }
    public void setWelcomeText(String text)
    {
        welcomeText.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setWelcomeText("Hello");
    }
}