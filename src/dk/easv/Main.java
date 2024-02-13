package dk.easv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("presentation/view/LogIn.fxml"));
        primaryStage.setTitle("Movie Recommendation System 0.01 Beta");
        // primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root));
       //  --- login is not resizable
        primaryStage.setResizable(false);
       // --- removes maximize and minimize except close use
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
