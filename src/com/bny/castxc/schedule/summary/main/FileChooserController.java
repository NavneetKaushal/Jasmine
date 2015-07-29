package com.bny.castxc.schedule.summary.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bny.castxc.schedule.summary.BNYSummaryXMLProcessor;
import com.bny.castxc.schedule.summary.model.BNYSMRYRModelCollection;
import com.bny.castxc.util.OSDetector;
import com.bny.castxc.util.XSLXProcessor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class FileChooserController {
	@FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    Text fileName;

    @FXML
    Button btnValidate;
    
    @FXML
    Button btnGenerate;
    
    @FXML
    static TextArea errMsg;
    
    public static String screenMessage = "System Messages will appear here \n";    
    
    //Setting up Logger Boiler Plate
	private static Logger log = Logger.getLogger(XSLXProcessor.class);
	static String LOG_PROPERTIES_FILE  ;
	static Properties logProperties = new Properties();

	
	static {
		if ( OSDetector.isLinux() || OSDetector.isMac()) LOG_PROPERTIES_FILE =	System.getProperty("user.home") +"/BNY/log4j.properties";
		if (OSDetector.isWindows()) LOG_PROPERTIES_FILE = System.getProperty("user.home") + "\\BNY\\log4j.properties";

		try {
			logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
		PropertyConfigurator.configure(logProperties);
		log.debug("FileChooserController- Logging Intialized using the properties=" + logProperties.getProperty("log4j.appender.file"));

	}
	
    public void btnValidateOnClick(ActionEvent event) {
    	//fileName.setText(" You just clicked on the File");
    	
    	clearConsole();
		FileChooserController.printToConsole(" Logs and your XML file will be written to your  =" + logProperties.getProperty("com.bny.home") + " folder");
		final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(com.bny.castxc.schedule.summary.main.Main.getStage());
        if (file != null) {
            //openFile(file);
            //OSDetector.open(file);
        	fileName.setText(file.getName());
        	printToConsole( "About to ingest the file= " + file.getName());
        	log.error("New XML Creation Job Started");
        	XSLXProcessor xslxProcessor = new XSLXProcessor (file);
        	try {
				xslxProcessor.xlsxFileProcess();
		        btnGenerate.setDisable(false);
			} catch (IOException | DatatypeConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error(e1);
		        btnGenerate.setDisable(true);
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error(e1);
		        btnGenerate.setDisable(true);
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error(e1);
		        btnGenerate.setDisable(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
		        btnGenerate.setDisable(true);
			}
        }
    	
    	
    	return;
    }
    
    
    public void goGenerate(ActionEvent event) {
    	clearConsole ();
        btnGenerate.setDisable(true);
    	printToConsole("XML Generation process started...");
    	log.info("XML Generation process started...");
    	String outputPath = "";
    	try {
    		outputPath = BNYSummaryXMLProcessor.summaryDataCollect();
			printToConsole("XML Generation process Ended Successfully...");
			log.info("XML Generation process Ended Successfully...");
	       // btnGenerate.setDisable(false);
	        printToConsole("XML File can be found here ==> " + outputPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    	printToConsole("XML Generation process errored out at FileChooserController.goGenerate...");
	    	log.error("XML Generation process errored out at FileChooserController.goGenerate", e);
		}
    	return;
    }
    
    public static void printToConsole(String message) {
    	//screenMessage = message + "\n" + screenMessage;
    	screenMessage = screenMessage + "\n" + message + "\n";
    	errMsg.setText(screenMessage);
    	//log.info(screenMessage);
    	return;
    }
    public static void clearConsole (){
    	errMsg.setText("");
    	screenMessage = "";
    }
	
    private static void resetApplication(){
    	 if( ! BNYSMRYRModelCollection.getSMRY_ACT().isEmpty()) BNYSMRYRModelCollection.getSMRY_ACT().clear();
    	 if (!BNYSMRYRModelCollection.getSMRY_CAP_PROJ().isEmpty()) BNYSMRYRModelCollection.getSMRY_CAP_PROJ().clear();
    	 if (!BNYSMRYRModelCollection.getSMRY_OPSRISK().isEmpty()) BNYSMRYRModelCollection.getSMRY_OPSRISK().clear();
    	 if (!BNYSMRYRModelCollection.getSMRY_OTTI_ACT().isEmpty()) BNYSMRYRModelCollection.getSMRY_OTTI_ACT().clear();
	     if (!BNYSMRYRModelCollection.getSMRY_OTTI_CUSIP().isEmpty()) BNYSMRYRModelCollection.getSMRY_OTTI_CUSIP().clear();
	     if (!BNYSMRYRModelCollection.getSMRY_OTTI_MV().isEmpty()) BNYSMRYRModelCollection.getSMRY_OTTI_MV().clear();
	     if (!BNYSMRYRModelCollection.getSMRY_OTTI_PROJ().isEmpty())  BNYSMRYRModelCollection.getSMRY_OTTI_PROJ().clear();
	     if (!BNYSMRYRModelCollection.getSMRY_PROJ().isEmpty()) BNYSMRYRModelCollection.getSMRY_PROJ().clear();
	     if (!BNYSMRYRModelCollection.getSMRY_VINT().isEmpty()) BNYSMRYRModelCollection.getSMRY_VINT().clear();
	     
	     clearConsole();

    }
    
}
