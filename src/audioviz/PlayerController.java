/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Dale Musser, modified by Connor Penrod
 */
public class PlayerController implements Initializable {
    
    @FXML
    private AnchorPane vizPane;
    
    
    @FXML
    private Slider timeSlider;
    
    private Media media;
    private MediaPlayer mediaPlayer;
    
    private Integer numBands = 140;
    private final Double updateInterval = 0.05;
    
    private Visualizer visualizer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        visualizer = new Cfpvf9ParticleVisualizer();
    }
    
    
    private void openMedia(File file) {
        
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        
        try {
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
                handleReady();
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                handleEndOfMedia();
            });
            mediaPlayer.setAudioSpectrumNumBands(numBands);
            mediaPlayer.setAudioSpectrumInterval(updateInterval);
            mediaPlayer.setAudioSpectrumListener((double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                handleUpdate(timestamp, duration, magnitudes, phases);
            });
            mediaPlayer.setAutoPlay(true);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    private void handleReady() {
        Duration duration = mediaPlayer.getTotalDuration();
        Duration ct = mediaPlayer.getCurrentTime();
        visualizer.start(numBands, vizPane);
        timeSlider.setMin(0);
        timeSlider.setMax(duration.toMillis());
    }
    
    private void handleEndOfMedia() {
        mediaPlayer.stop();
        mediaPlayer.seek(Duration.ZERO);
        timeSlider.setValue(0);
    }
    
    private void handleUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        Duration ct = mediaPlayer.getCurrentTime();
        double ms = ct.toMillis();
        timeSlider.setValue(ms);
        
        visualizer.update(timestamp, duration, magnitudes, phases);
    }
    
    @FXML
    private void handleOpen(Event event) {
        Stage primaryStage = (Stage)vizPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            openMedia(file);
        }
    }
    
    @FXML
    private void handlePlay(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
        
    }
    
    @FXML
    private void handlePause(ActionEvent event) {
        if (mediaPlayer != null) {
           mediaPlayer.pause(); 
        }
    }
    
    @FXML
    private void handleStop(ActionEvent event) {
        if (mediaPlayer != null) {
           mediaPlayer.stop(); 
        }
        
    }
    
    @FXML
    private void handleSliderMousePressed(Event event) {
        if (mediaPlayer != null) {
           mediaPlayer.pause(); 
        }  
    }
    
    @FXML
    private void handleSliderMouseReleased(Event event) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(new Duration(timeSlider.getValue()));
            visualizer.start(numBands, vizPane);
            mediaPlayer.play();
        }  
    }
}
