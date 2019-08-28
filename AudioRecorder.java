import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.sampled.*;

public class AudioRecorder extends Application
{
	private AudioFormat audioFormat;
  	private TargetDataLine targetDataLine;
  	
	@Override
    public void start(Stage primaryStage)
    {
    	Button startButton = new Button("Start");
    	Button stopButton = new Button("Stop");
    	startButton.setLayoutX(40);
    	startButton.setLayoutY(40);
    	startButton.setDisable(false);
    	stopButton.setLayoutX(100);
    	stopButton.setLayoutY(40);
    	stopButton.setDisable(true);
    	
      startButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent e) 
         {
    			startButton.setDisable(true); 
            stopButton.setDisable(false);
            
            recordAudio();  
    		}
    	}); 
    	
     stopButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent e) 
         {
    			startButton.setDisable(false);
            stopButton.setDisable(true);
            targetDataLine.stop();
            targetDataLine.close();
            //System.exit(0);
    		}
    	});  
    	
    	Pane pane = new Pane();
    	Text text = new Text(20, 20, "Microphone recorder");
    	pane.getChildren().addAll(text, startButton, stopButton);
    	
    	Scene scene = new Scene(pane, 300, 100);
    	primaryStage.setTitle("Audio Recorder");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	
    	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override
    		public void handle(WindowEvent event) {
        		Platform.exit();
    		}
		});	
    }
    
    private void recordAudio()
    {
      try
      {
         this.audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
    	   DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
         this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);   
        
         RecordingThread recording = new RecordingThread();    
         recording.start();                                    
   
      } 
      
      catch(LineUnavailableException e)
      {
         e.printStackTrace();
         System.exit(0);
      }
    }
  	
  	protected class RecordingThread extends Thread 
  	{
  		@Override
  		public void run()
  		{
    		AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
		   File f = new File("MyRecording.wav");
         
         AudioInputStream input = new AudioInputStream(targetDataLine);
         
         try
         {
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            AudioSystem.write(input, fileType, f);
            
         }
          
         catch(LineUnavailableException e)
         {
            e.printStackTrace();
         } 
         
         catch(IOException e)
         {
            e.printStackTrace();
         }
                
      }
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}