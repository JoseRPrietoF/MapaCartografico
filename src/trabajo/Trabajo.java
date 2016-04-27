/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajo;

import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author dibosjor
 */
public class Trabajo extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("ClientArea.fxml"));
        Parent root = (Parent) cargador.load();

        FXMLDocumentController controlador = cargador.getController();
        
        UndecoratorScene undecorated = new UndecoratorScene(stage, (Region) root);
     //   Scene scene = new Scene(root);
        controlador.setUndecorator(undecorated);
     
        
        stage.setScene(undecorated);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
