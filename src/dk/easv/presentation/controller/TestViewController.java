package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TestViewController implements Initializable {


    @FXML
    private ScrollPane scrollTopFromSimUsers;
    //Vboxes and Hboxes//
    @FXML
    private VBox MovieListVBox;
    @FXML
    private VBox Row1VBox;
    @FXML
    private HBox Row1HBox;

    //animated objects//
    @FXML
    private Circle tomatTop;
    @FXML
    private Circle tomatBottom;

    @FXML
    private AppModel appModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //animation for top "tomato" (a.k.a. circle)
        Path path = new Path();
        path.getElements().add(new MoveTo(tomatTop.getCenterX(),tomatTop.getCenterY()));
        path.getElements().add(new CubicCurveTo(tomatTop.getCenterX(), tomatTop.getCenterY(),tomatTop.getCenterX()-10,tomatTop.getCenterY()+12,tomatTop.getCenterX()-7,tomatTop.getCenterY()+14));
        PathTransition pathTrans = new PathTransition();
        pathTrans.setDuration(Duration.millis(5200));
        pathTrans.setPath(path);
        pathTrans.setNode(tomatTop);
        pathTrans.setCycleCount(Animation.INDEFINITE);
        pathTrans.setAutoReverse(true);
        pathTrans.play();

        //animation for bottom "tomato" (a.k.a. circle)
        Path path2 = new Path();
        path2.getElements().add(new MoveTo(tomatTop.getCenterX(),tomatTop.getCenterY()));
        path2.getElements().add(new CubicCurveTo(tomatTop.getCenterX(), tomatTop.getCenterY(),tomatTop.getCenterX()-12,tomatTop.getCenterY()-5,tomatTop.getCenterX()-7,tomatTop.getCenterY()-7));
        PathTransition pathTrans2 = new PathTransition();
        pathTrans2.setDuration(Duration.millis(3500));
        pathTrans2.setPath(path2);
        pathTrans2.setNode(tomatBottom);
        pathTrans2.setCycleCount(Animation.INDEFINITE);
        pathTrans2.setAutoReverse(true);
        pathTrans2.play();





    }

    public void setModel(AppModel model){
        this.appModel = model;
        //scrollTopFromSimUsers.setContent();
    }

    //old useless code that I might be able to reuse in the future
    /*
    public void setWidthProperty(){ //set it so the VBox to the side (where the movies are displayed) scales with the scene
        double widthPercentage = MovieListVBox.getWidth() / MovieListVBox.getScene().getWidth() ;
        System.out.println(widthPercentage);
        double spacingPercentage = Row1HBox.getSpacing() / MovieListVBox.getWidth() ;
        System.out.println("Spacing Percentage: " + spacingPercentage);
        MovieListVBox.getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MovieListVBox.setPrefWidth((double)newValue);

                //Row1VBox.setPrefWidth((double) newValue);
                //Row1HBox.setSpacing((double) newValue * (spacingPercentage));

            }
        });

    }

     */

}
