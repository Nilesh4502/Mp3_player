package com.example.mp3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable {

     @FXML
     private Pane pane;
     @FXML
     private Label song_label;
     @FXML
     private Button playButton,pauseButton,resetButton,nextButton,preveiousButton;
     @FXML
     private ComboBox<String> SpeedBox;
     @FXML
     private Slider Volume;
     @FXML
     private ProgressBar song_progressbar;
     private Media media;
     private MediaPlayer mediaplayer;
     private File directory;
     private File[] files;
     private ArrayList<File> songs;
     private int songsNumber;
     private int speed[]={25,50,75,100,125,150,175,200};
     private Timer timer;
     private TimerTask task;
     private  Boolean running;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            songs =new ArrayList<File>();
            directory = new File("songs");
            files=directory.listFiles();

            if(files!=null){
                for(File file: files){
                    songs.add(file);
                    System.out.println(file);
                }
            }
            media=new Media(songs.get(songsNumber).toURI().toString());
            mediaplayer=new MediaPlayer(media);

            song_label.setText(songs.get(songsNumber).getName());
            for(int i=0;i<speed.length;i++){
                SpeedBox.getItems().add(Integer.toString(speed[i])+"%");

            }
            SpeedBox.setOnAction(this::speedChange);

            Volume.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mediaplayer.setVolume(Volume.getValue()*0.01);
                }
            });

            song_progressbar.setStyle("-fx-accent: #146C94 ");

    }
    public  void playmedia(){
        beginTimmer();
        speedChange(null);
       mediaplayer.play();
    }
    public  void pausemedia(){
        cancelTimmer();
       mediaplayer.pause();
    }
    public  void resetmedia(){
        song_progressbar.setProgress(0);
       mediaplayer.seek(Duration.seconds(0));
    }
    public  void nextmedia(){

        if(songsNumber<songs.size()-1){
            songsNumber++;
            mediaplayer.stop();

            if(running){
                cancelTimmer();
            }

            media=new Media(songs.get(songsNumber).toURI().toString());
            mediaplayer=new MediaPlayer(media);

            song_label.setText(songs.get(songsNumber).getName());
            playmedia();
        }
        else{
            songsNumber=0;
            mediaplayer.stop();

            if(running){
                cancelTimmer();
            }

            media=new Media(songs.get(songsNumber).toURI().toString());
            mediaplayer=new MediaPlayer(media);

            song_label.setText(songs.get(songsNumber).getName());
            playmedia();
        }
    }
    public  void previousmedia(){
        if(songsNumber>0){
            songsNumber--;
            mediaplayer.stop();

            if(running){
                cancelTimmer();
            }
            media=new Media(songs.get(songsNumber).toURI().toString());
            mediaplayer=new MediaPlayer(media);

            song_label.setText(songs.get(songsNumber).getName());
            playmedia();
        }
        else{
            songsNumber=songs.size()-1;
            mediaplayer.stop();

            if(running){
                cancelTimmer();
            }

            media=new Media(songs.get(songsNumber).toURI().toString());
            mediaplayer=new MediaPlayer(media);

            song_label.setText(songs.get(songsNumber).getName());
            playmedia();
        }

    }

    public  void speedChange(ActionEvent event){
        if(SpeedBox.getValue()==null){
            mediaplayer.setRate(1);
        }
        else
           mediaplayer.setRate(Integer.parseInt(SpeedBox.getValue().substring(0,SpeedBox.getValue().length()-1))*0.01);

    }
    public void beginTimmer(){
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                running=true;
                double current=mediaplayer.getCurrentTime().toSeconds();
                double end=media.getDuration().toSeconds();
                song_progressbar.setProgress(current/end);

                if(current/end==1){
                    cancelTimmer();
                }

            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }
    public void cancelTimmer(){
         running=false;
         timer.cancel();
    }
}