package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    @FXML
    private ScrollPane spUsers;
    @FXML
    private HBox listUsers;
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
        scrollPaneListenerTopAvgNotSeen();
        scrollPaneListenerTopForUser();
        scrollPaneListenerTopFromSimilar();
        scrollPaneListenerUsers();
    }

    public void setModel(AppModel model) {
        this.model = model;
        lvTopSimilarUsers.setItems(model.getObsSimilarUsers());

        startTimer("Load users");
        model.loadUsers();
        stopTimer();

        // Populate users directly
        populateUsers(model);

        // Select the logged-in user in the listview, automagically trigger the listener above
        User loggedInUser = model.getObsLoggedInUser();
        for (Node node : listUsers.getChildren()) {
            VBox userBox = (VBox) node;
            Label label = (Label) userBox.getChildren().get(0);
            if (label.getText().equals(loggedInUser.getName())) {
                userBox.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true,
                        true, true, true, true, true, true, true, true, null));
                break;
            }
        }
    }

    public void populateMovieNotSeen(AppModel model) {
        List<Movie> movies = model.getObsTopMovieNotSeen();
        listTopAvgNotSeen.getChildren().clear();
        currentIndex = 0;
        loadMovieNotSeen(movies);
        spTopAvgNotSeen.setHvalue(0);
    }

    public void populateTopForUser(AppModel model) {
        List<Movie> movies = model.getObsTopMovieSeen();
        listTopForUser.getChildren().clear();
        currentIndex = 0;
        loadListTopForUser(movies);
        spTopForUser.setHvalue(0);
    }

    public void populateTopFromSimilar(AppModel model) {
        List<TopMovie> movies = model.getObsTopMoviesSimilarUsers();
        listTopFromSimilar.getChildren().clear();
        currentIndex = 0;
        loadTopFromSimilar(movies);
        spTopFromSimilar.setHvalue(0);
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

    private void scrollPaneListenerTopAvgNotSeen(){
        spTopAvgNotSeen.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == spTopAvgNotSeen.getHmax()) {
                loadMovieNotSeen(model.getObsTopMovieNotSeen());
            }
        });
    }

    private void scrollPaneListenerTopForUser(){
        spTopForUser.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == spTopForUser.getHmax()) {
                loadListTopForUser(model.getObsTopMovieSeen());
            }
        });
    }

    private void scrollPaneListenerTopFromSimilar(){
        spTopFromSimilar.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == spTopFromSimilar.getHmax()) {
                loadTopFromSimilar(model.getObsTopMoviesSimilarUsers());
            }
        });
    }

    /////////////////////////users///////////////////

    public void populateUsers(AppModel model) {
        List<User> users = model.getObsUsers();
        listUsers.getChildren().clear();
        currentIndex = 0;
        loadUsers(users);
        spUsers.setHvalue(0); // Reset the scroll pane to the start
    }

    private void loadUsers(List<User> users) {
        int endIndex = Math.min(currentIndex + howManyLoaded, users.size());
        for (int i = currentIndex; i < endIndex; i++) {
            User user = users.get(i);
            Label label = new Label(user.getName());
            VBox userBox = new VBox(label);
            userBox.setPadding(new Insets(10));
            userBox.setOnMouseClicked(event -> {
                highlightUser(userBox);
                model.loadData(user);
                populateMovieNotSeen(model);
                populateTopForUser(model);
                populateTopFromSimilar(model);

            });
            listUsers.getChildren().add(userBox);
        }
        currentIndex = endIndex;
    }

    private void highlightUser(VBox userBox) {
        // Reset the style of all user boxes
        for (Node node : listUsers.getChildren()) {
            node.setStyle("");
        }
        // Highlight the selected user box
        userBox.setStyle("-fx-background-color: #4b4646;");
    }

    private void scrollPaneListenerUsers() {
        spUsers.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == spUsers.getHmax()) {
                loadUsers(model.getObsUsers());
            }
        });
    }

}