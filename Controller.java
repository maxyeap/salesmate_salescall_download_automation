package com.example.salesmate;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.net.URL;
import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
public class Controller implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField urlField;
    @FXML
    private TextField highestIndexField;
    @FXML
    private TextField clientField;
    @FXML
    private TextField folderField;
    @FXML
    private ChoiceBox<String> browserChoiceBox;
    private String[] browsers = {"Chrome", "Safari"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        browserChoiceBox.getItems().addAll(browsers);
    }

    @FXML
    protected void onStartButtonClick() throws InterruptedException {
        String browserChoice = browserChoiceBox.getValue();
        String email = emailField.getText();
        String password = passwordField.getText();
        String url = urlField.getText();
        int highestIndex = Integer.parseInt(highestIndexField.getText());
        String client = clientField.getText();
        SalesmateAutomation newSession = new SalesmateAutomation(browserChoice, client);
        newSession.login(email, password, url);
        List<HashMap> downloadDetails = newSession.getDownloadLinks(highestIndex);
        String folder = folderField.getText();
        Download.startDownload(downloadDetails, folder);
    }

    @FXML
    protected void selectFolder() {
        folderField.setDisable(true);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        File selectedDirectory = directoryChooser.showDialog(null);
        try {
            folderField.setText(selectedDirectory.getAbsolutePath());
        } catch (Exception ignored) {
        }
        folderField.setDisable(false);
    }
}