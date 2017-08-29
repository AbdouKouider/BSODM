/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bso_dm_2017;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kadero
 */
public class FXMLResultsViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<resultat> TableResultats;

    @FXML
    private JFXButton id_Exit;
    
    //public static ArrayList<resultat> ensemebleresultat= new ArrayList();

    @FXML
    void ExitOnClick(ActionEvent event) throws IOException {
        
         Stage home = (Stage)((Node)event.getSource()).getScene().getWindow();
                   Parent root = FXMLLoader.load(getClass().getResource("first.fxml"));
                    Scene scene = new Scene(root);
               

                  home.hide(); //optional
                  home.setScene(scene);
                   home.show();

    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        addResult();
    }  
    
    public void addResult()
    {   
        
       // System.out.println("fiififrifrifrirfifr");
        this.TableResultats.getItems().clear();
        this.TableResultats.getItems().addAll(FirstController.ensemebleresultat);
        //System.out.println(FirstController.ensemebleresultat.get(0).getStrategie());
    }
    
}
