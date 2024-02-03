package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox listMovie;
    @FXML
    private ListView<User> lvUsers;
    @FXML
    private ListView<Movie> lvTopForUser;
    @FXML
    private ListView<Movie> lvTopAvgNotSeen;
    @FXML
    private ListView<UserSimilarity> lvTopSimilarUsers;
    @FXML
    private ListView<TopMovie> lvTopFromSimilar;

    private int currentIndex = 0;
    private static final int ITEMS_PER_LOAD = 15;


    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";

    private void startTimer(String message){
        timerStartMillis = System.currentTimeMillis();
        timerMsg = message;
    }

    private void stopTimer(){
        System.out.println(timerMsg + " took : " + (System.currentTimeMillis() - timerStartMillis) + "ms");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPaneListener();
    }

    public void setModel(AppModel model) {
        this.model = model;
        lvUsers.setItems(model.getObsUsers());
        lvTopForUser.setItems(model.getObsTopMovieSeen());
        lvTopAvgNotSeen.setItems(model.getObsTopMovieNotSeen());
        lvTopSimilarUsers.setItems(model.getObsSimilarUsers());
        lvTopFromSimilar.setItems(model.getObsTopMoviesSimilarUsers());


        startTimer("Load users");
        model.loadUsers();
        stopTimer();

        lvUsers.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldUser, selectedUser) -> {
                    startTimer("Loading all data for user: " + selectedUser);
                    model.loadData(selectedUser);
                    populateMovieNotSeen(model);
                });

        // Select the logged-in user in the listview, automagically trigger the listener above
        lvUsers.getSelectionModel().select(model.getObsLoggedInUser());
    }

//    public void populateMovieNotSeen(AppModel model) {
//        List<Movie> movies = model.getObsTopMovieNotSeen();
//        System.out.println("Number of movies: " + movies.size());
//        for (Movie movie : movies) {
//            System.out.println("Movie title: " + movie.getTitle());
//            Label label = new Label(movie.getTitle());
//            label.setMaxWidth(Double.MAX_VALUE);
//            VBox movieBox = new VBox(label);
//            movieBox.setPrefHeight(100);
//            movieBox.setPrefWidth(100);
//            movieBox.setPadding(new Insets(10));
//            listMovie.getChildren().add(movieBox);
//        }
//    }

    public void populateMovieNotSeen(AppModel model) {
        List<Movie> movies = model.getObsTopMovieNotSeen();
        System.out.println("Number of movies: " + movies.size());
        loadMoreItems(movies);
    }

    private void loadMoreItems(List<Movie> movies) {
        int endIndex = Math.min(currentIndex + ITEMS_PER_LOAD, movies.size());
        for (int i = currentIndex; i < endIndex; i++) {
            Movie movie = movies.get(i);
            System.out.println("Movie title: " + movie.getTitle());
            Label label = new Label(movie.getTitle());
            label.setMaxWidth(Double.MAX_VALUE);
            VBox movieBox = new VBox(label);
            movieBox.setPrefHeight(100);
            movieBox.setPrefWidth(100);
            movieBox.setPadding(new Insets(10));
            listMovie.getChildren().add(movieBox);
        }
        currentIndex = endIndex;
    }

    private void scrollPaneListener(){
        scrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == scrollPane.getHmax()) {
                loadMoreItems(model.getObsTopMovieNotSeen());
            }
        });
    }

}