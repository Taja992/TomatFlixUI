package dk.easv.presentation.model;

import dk.easv.entities.*;
import dk.easv.logic.LogicManager;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AppModel {

    LogicManager logic = new LogicManager();
    // Models of the data in the view
    private final ObservableList<User>  obsUsers = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity>  obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();

    public void loadUsers(){
        obsUsers.clear();
        obsUsers.addAll(logic.getAllUsers());
    }

    public void loadData(User user) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<Movie> topMoviesSeen = FXCollections.observableArrayList(logic.getTopAverageRatedMovies(user));
                ObservableList<Movie> topMoviesNotSeen = FXCollections.observableArrayList(logic.getTopAverageRatedMoviesUserDidNotSee(user));
                ObservableList<UserSimilarity> similarUsers = FXCollections.observableArrayList(logic.getTopSimilarUsers(user));
                ObservableList<TopMovie> topMoviesSimilarUsers = FXCollections.observableArrayList(logic.getTopMoviesFromSimilarPeople(user));

                Platform.runLater(() -> {
                    obsTopMovieSeen.clear();
                    obsTopMovieSeen.addAll(topMoviesSeen);

                    obsTopMovieNotSeen.clear();
                    obsTopMovieNotSeen.addAll(topMoviesNotSeen);

                    obsSimilarUsers.clear();
                    obsSimilarUsers.addAll(similarUsers);

                    obsTopMoviesSimilarUsers.clear();
                    obsTopMoviesSimilarUsers.addAll(topMoviesSimilarUsers);
                });

                return null;
            }
        };
        new Thread(task).start();
    }

    public ObservableList<User> getObsUsers() {
        return obsUsers;
    }

    public ObservableList<Movie> getObsTopMovieSeen() {
        return obsTopMovieSeen;
    }

    public ObservableList<Movie> getObsTopMovieNotSeen() {
        return obsTopMovieNotSeen;
    }

    public ObservableList<UserSimilarity> getObsSimilarUsers() {
        return obsSimilarUsers;
    }

    public ObservableList<TopMovie> getObsTopMoviesSimilarUsers() {
        return obsTopMoviesSimilarUsers;
    }

    public User getObsLoggedInUser() {
        return obsLoggedInUser.get();
    }

    public SimpleObjectProperty<User> obsLoggedInUserProperty() {
        return obsLoggedInUser;
    }

    public void setObsLoggedInUser(User obsLoggedInUser) {
        this.obsLoggedInUser.set(obsLoggedInUser);
    }

    public boolean loginUserFromUsername(String userName) {
        User u = logic.getUser(userName);
        obsLoggedInUser.set(u);
        if (u==null)
            return false;
        else
            return true;
    }
}
