package com.bny.castxc.schedule.summary.main;
	
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("BNYCastXCUI.fxml"));
			final FileChooser fileChooser = new FileChooser();
			Scene scene = new Scene(root,1000,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			this.primaryStage = primaryStage;
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static Stage getStage(){
		return primaryStage;
	}
	
	/*
	public void start222( Stage primaryStage) {
		try {
			//VBox root = (VBox)FXMLLoader.load(getClass().getResource("filechooser.fxml"));
			
			primaryStage.setTitle("File Chooser Sample");
			final FileChooser fileChooser = new FileChooser();

		    final Button openButton = new Button("Open a XML File...");
		    final Button generateButton = new Button("Generate...");
		    
		    openButton.setOnAction(
		            new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                    File file = fileChooser.showOpenDialog(primaryStage);
		                    if (file != null) {
		                        //openFile(file);
		                        //OSDetector.open(file);
		                    	System.out.println( "About to process the file=" + file.getName());
		                    	XSLXProcessor xslxProcessor = new XSLXProcessor (file);
		                    	try {
									xslxProcessor.xlsxFileProcess();
								} catch (IOException | DatatypeConfigurationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (InvocationTargetException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		                    }
		                }
		            });
	
		    generateButton.setOnAction(
		            new EventHandler<ActionEvent>() {
		                @Override
		                public void handle(final ActionEvent e) {
		                	System.out.println(" ***** I will start executing *****"); 
		                }
		            });
		    
		    final GridPane inputGridPane = new GridPane();

		    GridPane.setConstraints(openButton, 0, 0);
		    //.setConstraints(openMultipleButton, 1, 0);
		    inputGridPane.setHgap(6);
		    inputGridPane.setVgap(6);
		    inputGridPane.getChildren().addAll(openButton);

		    final Pane rootGroup = new VBox(12);
		    rootGroup.getChildren().addAll(inputGridPane);
		    rootGroup.setPadding(new Insets(12, 12, 12, 12));
		    
		    Scene scene = new Scene(rootGroup,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			 
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public static void main(String[] args) {
		Application.launch(args);
	}
	
}



