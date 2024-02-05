package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AppController implements Initializable {

    @FXML
    private ScrollPane spTopForUser;
    @FXML
    private HBox listTopForUser;
    @FXML
    private ScrollPane spTopFromSimilar;
    @FXML
    private HBox listTopFromSimilar;
    @FXML
    private ScrollPane spTopAvgNotSeen;
    @FXML
    private HBox listTopAvgNotSeen;
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
    private static final int howManyLoaded = 25;


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

        model.getObsTopMovieSeen().addListener((ListChangeListener.Change<? extends Movie> c) -> {
            populateTopForUser(model);
        });

        startTimer("Load users");
        model.loadUsers();
        stopTimer();

        lvUsers.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldUser, selectedUser) -> {
                    startTimer("Loading all data for user: " + selectedUser);
                    model.loadData(selectedUser);
                    populateMovieNotSeen(model);
                    populateTopForUser(model);
                    populateTopFromSimilar(model);
                });

        // Select the logged-in user in the listview, automagically trigger the listener above
        lvUsers.getSelectionModel().select(model.getObsLoggedInUser());
    }

    public void populateMovieNotSeen(AppModel model) {
        List<Movie> movies = model.getObsTopMovieNotSeen();
        listTopAvgNotSeen.getChildren().clear();
        currentIndex = 0;
        lvTopAvgNotSeen.setItems(FXCollections.observableArrayList(movies));
        loadMovieNotSeen(movies);
    }

    public void populateTopForUser(AppModel model) {
        List<Movie> movies = model.getObsTopMovieSeen();
        listTopForUser.getChildren().clear();
        currentIndex = 0;
        lvTopForUser.setItems(FXCollections.observableArrayList(movies));
        loadListTopForUser(movies);
    }

    public void populateTopFromSimilar(AppModel model) {
        List<TopMovie> movies = model.getObsTopMoviesSimilarUsers();
        listTopFromSimilar.getChildren().clear();
        currentIndex = 0;
        loadTopFromSimilar(movies);
    }

    private void loadMovieNotSeen(List<Movie> movies) {
        int endIndex = Math.min(currentIndex + howManyLoaded, movies.size());
        for (int i = currentIndex; i < endIndex; i++) {
            Movie movie = movies.get(i);
            Label label = new Label(movie.getTitle());
            VBox movieBox = new VBox(label);
            movieBox.setPrefHeight(100);
            movieBox.setPrefWidth(100);
            movieBox.setPadding(new Insets(10));
            listTopAvgNotSeen.getChildren().add(movieBox);
        }
        currentIndex = endIndex;
    }

    private void loadListTopForUser(List<Movie> movies) {
        int endIndex = Math.min(currentIndex + howManyLoaded, movies.size());
        for (int i = currentIndex; i < endIndex; i++) {
            Movie movie = movies.get(i);
            Label label = new Label(movie.getTitle());
            VBox movieBox = new VBox(label);
            movieBox.setPrefHeight(100);
            movieBox.setPrefWidth(100);
            movieBox.setPadding(new Insets(10));
            listTopForUser.getChildren().add(movieBox);
        }
        currentIndex = endIndex;
    }

    private void loadTopFromSimilar(List<TopMovie> movies) {
        int endIndex = Math.min(currentIndex + howManyLoaded, movies.size());
        for (int i = currentIndex; i < endIndex; i++) {
            Movie movie = movies.get(i).getMovie();
            Label label = new Label(movie.getTitle());
            VBox movieBox = new VBox(label);
            movieBox.setPrefHeight(100);
            movieBox.setPrefWidth(100);
            movieBox.setPadding(new Insets(10));
            listTopFromSimilar.getChildren().add(movieBox);
        }
        currentIndex = endIndex;
    }

    private void scrollPaneListener(){
        spTopAvgNotSeen.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == spTopAvgNotSeen.getHmax()) {
                loadMovieNotSeen(model.getObsTopMovieNotSeen());
            }
        });

    }

}