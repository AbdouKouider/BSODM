/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bso_dm_2017;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Vi Incorporated
 */
public class Bso_DM_2017 extends Application {
    
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws IOException {
           Parent root = FXMLLoader.load(getClass().getResource("first.fxml"));
        
        Scene scene = new Scene(root);
       //  String css = Test1.class.getResource("cssStyling.css").toExternalForm();
        //scene.getStylesheets().add(css);
        Bso_DM_2017.stage=stage;
        //stage.getIcons().add(new Image(this.getClass().getResource("login.png").toString()));
        stage.setScene(scene);
        stage.setTitle("BSO_DM");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
