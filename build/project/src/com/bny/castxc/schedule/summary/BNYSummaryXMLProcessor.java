package com.bny.castxc.schedule.summary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;















import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import com.bny.castxc.schedule.summary.main.FileChooserController;
import com.bny.castxc.schedule.summary.model.BNYSMRYDataDictionary;
//import com.bny.castxc.schedule.summary.model.BNYSMRYOTTIACTModel;
import com.bny.castxc.schedule.summary.model.BNYSMRYRModelCollection;
import com.bny.castxc.util.OSDetector;
import com.bny.castxc.util.XSLXProcessor;


public class BNYSummaryXMLProcessor {

	static ObjectFactory summaryFty = new ObjectFactory();
	static SMRYDataCollect smryDataCollect;
	static Map<String, String> ottiActAttList = new HashMap<String, String>();
	
	//Setting up Logger Boiler Plate
	private static Logger log = Logger.getLogger(BNYSummaryXMLProcessor.class);
	static String LOG_PROPERTIES_FILE = "";
	static Properties logProperties = new Properties();

	static {
		try {
			if ( OSDetector.isLinux() || OSDetector.isMac()) LOG_PROPERTIES_FILE =	System.getProperty("user.home") +"/BNY/log4j.properties";
			if (OSDetector.isWindows()) LOG_PROPERTIES_FILE = System.getProperty("user.home") + "\\BNY\\log4j.properties";

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
		log.debug("BNYSummaryXMLProcessor- Logging Intialized using the properties=" + logProperties.getProperty("log4j.appender.file"));
	}
	
	
	
	public static String summaryDataCollect()  {
		log.info ( "***** BNYSummaryXMLProcessor.summaryDataCollect - START ");
		boolean validationsPerfect = true;
		smryDataCollect = summaryFty.createSMRYDataCollect();
		
		
		//FileChooserController.printToConsole (" DATE ERROR COUNT :" +"");
		
		Map<?, ?> smry_Act_map = BNYSMRYRModelCollection.getSMRY_ACT().get(0);
		//String DATA_ASOF_TSTMP = (String)smry_Act_map.get("DATA_ASOF_TSTMP");
		String DATA_ASOF_TSTMP = (String)smry_Act_map.get("DATA_ASOF_TSTMP");//2014-09-30T00:00:00
		String LAST_ASOF_TSTMP = (String)smry_Act_map.get("LAST_ASOF_TSTMP");
		//FileChooserController.printToConsole (" DATE ERROR :" +(String)smry_Act_map.get("DATA_ASOF_TSTMP") +(String)smry_Act_map.get("LAST_ASOF_TSTMP"));
		
		try {
			smryDataCollect.setDATAASOFTSTMP(DatatypeFactory.newInstance().newXMLGregorianCalendar(DATA_ASOF_TSTMP));
			smryDataCollect.setLASTASOFTSTMP(DatatypeFactory.newInstance().newXMLGregorianCalendar(LAST_ASOF_TSTMP));
			
			
			validationsPerfect = process_SMRY_ACT_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_PROJ_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_VINT_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_CAP_PROJ_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_OTTI_ACT_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_OTTI_PROJ_xml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_OTTI_CUSIPxml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_OTTI_MVxml();
			if ( validationsPerfect) validationsPerfect = process_SMRY_OPSRISKxml();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1);
			validationsPerfect = false;
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1);
			validationsPerfect = false;
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1);
			validationsPerfect = false;
		}

		if(!validationsPerfect) {
			FileChooserController.printToConsole (" Cannot proceed with the XML Generation, as the validations are not passed through");
			return "";
		}
			
		
		JAXBContext ctx = null;
		String outPutFilePath = "";

		try {
			//File outputFile = new File(System.getProperty("user.home") + "/BNYFinalOutputFile.xml");
			String outputFileName = getOutputFileName();
			File outputFile = new File(logProperties.getProperty("com.bny.home"),outputFileName);
			
			ctx = JAXBContext.newInstance("com.bny.castxc.schedule.summary");
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.marshal (smryDataCollect, outputFile );
			marshaller.marshal (smryDataCollect, System.out );
			log.info("*** SUCCESSFULLY CREATED THE XML FILE ***");
			outPutFilePath= outputFile.getCanonicalPath();
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			return "";
		}
		return outPutFilePath;

	}
	
	private static String getOutputFileName() {
		String outputName = "";
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMDD");
		Date date = new Date();
		String dateValue = dateFormat.format(date);
		DateFormat dateYY = new SimpleDateFormat("YY");
		String dateYYvalue = dateYY.format(date);
		DateFormat dateFormat2 = new SimpleDateFormat("dd_MM_YYYY");
		String dateValue2 = dateFormat2.format(date);
		
		Map smry_Act_map = BNYSMRYRModelCollection.getSMRY_ACT().get(0);
		String DATA_ASOF_TSTMP = (String)smry_Act_map.get("D_DT");//2014-09-30T00:00:00
		String yy1 = DATA_ASOF_TSTMP.substring(0,DATA_ASOF_TSTMP.indexOf("-"));
		DATA_ASOF_TSTMP = DATA_ASOF_TSTMP.substring(DATA_ASOF_TSTMP.indexOf("-") +1 );
		String mm1 = DATA_ASOF_TSTMP.substring(0,DATA_ASOF_TSTMP.indexOf("-"));
		String dd1 = DATA_ASOF_TSTMP.substring(DATA_ASOF_TSTMP.indexOf("-")+1 , DATA_ASOF_TSTMP.indexOf("T"));
		DATA_ASOF_TSTMP = yy1 + mm1 + dd1;
		String ID_RSSD = (String)smry_Act_map.get("ID_RSSD");
		//ID_RSSD = ID_RSSD.substring(0, ID_RSSD.indexOf("."));

		outputName = "FRY" + dateYYvalue + "A_SMRY_"+DATA_ASOF_TSTMP + "_" + ID_RSSD + "_" + dateValue2 +".xml";
		
		
		return outputName;
	}
	
	/*
	 * Report 1 : SMRY_ACT
	 */
	private static boolean process_SMRY_ACT_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_ACT_xml - START ");

		List <Map> smryActmdlList =BNYSMRYRModelCollection.getSMRY_ACT();
		int recordCount = 0;
		for (Iterator<Map> iter = smryActmdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectACT smryDataCollectAct = summaryFty.createSMRYDataCollectACT();
			Map<String, String> xlOttiActRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiActRow.entrySet())
			{
			    //System.out.println("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();

			    if (BNYSMRYDataDictionary.ACT_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.ACT_AttributesList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectAct.idrssd = Integer.parseInt(value);
			    	} else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType)  && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType)  && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	} else if ("STRING_100".equalsIgnoreCase(dataType)  && value.length() >100){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	} else if ("STRING_300".equalsIgnoreCase(dataType)  && value.length() >300){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	} else if ("STRING_10000".equalsIgnoreCase(dataType)  && value.length() >10000){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	}		
			    	
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectAct.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectAct.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectAct, key, value);
			    	}
			    }			    
			}		
			//smryDataCollect.setSMRYDataCollectACT(smryDataCollectAct);
			smryDataCollect.getSMRYDataCollectACT().add(smryDataCollectAct);
			//smryDataCollect.smryDataCollectACT
			recordCount++;
			//FileChooserController.printToConsole("SMRY_ACT Processed record#="+ recordCount);
		}
		FileChooserController.printToConsole("SMRY_ACT Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_ACT_xml - END " + recordCount);

		return true;
	}	
	
	/*
	 *  Report 2 : SMRY_PROJ
	 */
	private static boolean process_SMRY_PROJ_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_PROJ_xml - START ");
		List <Map> smryProjmdlList =BNYSMRYRModelCollection.getSMRY_PROJ();
		int recordCount = 0;
		for (Iterator<Map> iter = smryProjmdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectPROJ smryProj = summaryFty.createSMRYDataCollectPROJ();
			Map<String, String> xlOttiActRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiActRow.entrySet())
			{
			    //System.out.println("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    
			    if (BNYSMRYDataDictionary.SMRY_PROJ_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.SMRY_PROJ_AttributesList.get(key);
			    	
			    	//Validations for Required Fields.
			    	
			    	
			    	
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryProj.idrssd = Integer.parseInt(value);
			    	} 			    	
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryProj, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryProj, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryProj, key, value);
			    	}		
			    	
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryProj.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryProj.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryProj, key, value);
			    	}
			    }			    
			}
		
		smryDataCollect.getSMRYDataCollectPROJ().add(smryProj);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_PROJ Processed record#="+ recordCount);

		}
		FileChooserController.printToConsole("SMRY_PROJ Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_PROJ_xml - END " + recordCount);
		return true;
	}
	
	/*
	 * Report 3 : SMRY_DataCollect_VINT
	 */
	private static boolean process_SMRY_VINT_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_VINT_xml - START ");
		List <Map> smryVintmdlList =BNYSMRYRModelCollection.getSMRY_VINT();
		int recordCount = 0;
		for (Iterator<Map> iter = smryVintmdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectVINT smryDataCollectVintAct = summaryFty.createSMRYDataCollectVINT();
			Map<String, String> xlRow = iter.next();

			for (Map.Entry<String, String> entry : xlRow.entrySet())
			{
			    //log.debug("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.SMRY_VINT_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.SMRY_VINT_AttributesList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectVintAct.idrssd = Integer.parseInt(value);
			    	} 			    	
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectVintAct, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectVintAct, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectVintAct, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectVintAct.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectVintAct.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectVintAct, key, value);
			    	}
			    }			    
			}
		
			smryDataCollect.getSMRYDataCollectVINT().add(smryDataCollectVintAct);
			recordCount++;
			//FileChooserController.printToConsole("SMRY_VINT Processed record#="+ recordCount);

		}
		FileChooserController.printToConsole("SMRY_VINT Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_VINT_xml - END " + recordCount);
		return true;

	}

	/*
	 * Report 4 : SMRY_CAP_PROJ
	 */
	private static boolean process_SMRY_CAP_PROJ_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_CAP_PROJ_xml - START ");

		List <Map> smryCapProjMdlList =BNYSMRYRModelCollection.getSMRY_CAP_PROJ();
		int recordCount = 0;
		for (Iterator<Map> iter = smryCapProjMdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectCAPPROJ smryDataCollectCAPProj = summaryFty.createSMRYDataCollectCAPPROJ();
			Map<String, String> xlOttiActRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiActRow.entrySet())
			{
			    //log.debug("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.SMRY_CAPPROJ_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.SMRY_CAPPROJ_AttributesList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectCAPProj.idrssd = Integer.parseInt(value);
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectCAPProj, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectCAPProj, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectCAPProj, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectCAPProj.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectCAPProj.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectCAPProj, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectCAPPROJ().add(smryDataCollectCAPProj);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_CAPPROJ Processed record#="+ recordCount);

		}
		FileChooserController.printToConsole("SMRY_CAPPROJ Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_CAP_PROJ_xml - END " + recordCount);

		return true;
	}

	
	
	/*
	 * Report 5 : SMRY_OTTI_ACT
	 */
	private static boolean process_SMRY_OTTI_ACT_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_ACT_xml - START ");

		List <Map> ottiActmdlList =BNYSMRYRModelCollection.getSMRY_OTTI_ACT();
		int recordCount = 0;
		for (Iterator<Map> iter = ottiActmdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectOTTIACT smryDataCollectOttiAct = summaryFty.createSMRYDataCollectOTTIACT();
			Map<String, String> xlOttiActRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiActRow.entrySet())
			{
			    //log.debug("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.OTTI_ACT_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.OTTI_ACT_AttributesList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectOttiAct.idrssd = Integer.parseInt(value);
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiAct, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiAct, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiAct, key, value);
			    	}
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectOttiAct.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
							smryDataCollectOttiAct.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiAct, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectOTTIACT().add(smryDataCollectOttiAct);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_OTTI_ACT Processed record#="+ recordCount);

		}
		FileChooserController.printToConsole("SMRY_OTTI_ACT Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_ACT_xml - END " + recordCount);

		return true;
	}
	
	
	/*
	 * Report 6 : SMRY_OTTI_PROJ
	 */
	private static boolean process_SMRY_OTTI_PROJ_xml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_PROJ_xml - START ");

		List <Map> ottiProjMdlList =BNYSMRYRModelCollection.getSMRY_OTTI_PROJ();
		int recordCount = 0;
		for (Iterator<Map> iter = ottiProjMdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectOTTIPROJ smryDataCollectOttiProj = summaryFty.createSMRYDataCollectOTTIPROJ();
			Map<String, String> xlOttiProjRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiProjRow.entrySet())
			{
			    //log.debug("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.OTTI_PROJ_AttributesList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.OTTI_PROJ_AttributesList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectOttiProj.idrssd = Integer.parseInt(value);
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiProj, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiProj, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiProj, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectOttiProj.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectOttiProj.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiProj, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectOTTIPROJ().add(smryDataCollectOttiProj);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_OTTI_PROJ Processed record#="+ recordCount);

		}
	FileChooserController.printToConsole("SMRY_OTTI_PROJ Processed total # of records="+ recordCount);
	log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_PROJ_xml - END " + recordCount);

	return true;
	}
	
	/*
	 * Report 7 : SMRY_OTTI_CUSIP
	 * 
	 */
	private static boolean process_SMRY_OTTI_CUSIPxml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_CUSIPxml - START ");

		List <Map> ottiCusipMdlList =BNYSMRYRModelCollection.getSMRY_OTTI_CUSIP();
		int recordCount = 0;
		for (Iterator<Map> iter = ottiCusipMdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectOTTICUSIP smryDataCollectOttiCusip = summaryFty.createSMRYDataCollectOTTICUSIP();
			Map<String, String> xlOttiCusipRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiCusipRow.entrySet())
			{
				//FileChooserController.printToConsole("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.OTTI_CUSIP_AttributeList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.OTTI_CUSIP_AttributeList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectOttiCusip.idrssd = Integer.parseInt(value);
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiCusip, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiCusip, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiCusip, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectOttiCusip.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectOttiCusip.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiCusip, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectOTTICUSIP().add(smryDataCollectOttiCusip);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_OTTI_CUSIP Processed record#="+ recordCount);

		}
	FileChooserController.printToConsole("SMRY_OTTI_CUSIP Processed total # of records="+ recordCount);
	log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_CUSIPxml - END " + recordCount);

	return true;
	}
	
	/*
	 * Report 8 : SMRY_Data_Collect_OTTI_MV
	 */
	private static boolean process_SMRY_OTTI_MVxml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_MVxml - START ");

		List <Map> ottiMvMdlList =BNYSMRYRModelCollection.getSMRY_OTTI_MV();
		int recordCount = 0;
		for (Iterator<Map> iter = ottiMvMdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectOTTIMV smryDataCollectOttiMV = summaryFty.createSMRYDataCollectOTTIMV();
			Map<String, String> xlOttiMvRow = iter.next();

			for (Map.Entry<String, String> entry : xlOttiMvRow.entrySet())
			{
				//FileChooserController.printToConsole("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.SMRY_OTTI_MV_AttributeList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.SMRY_OTTI_MV_AttributeList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectOttiMV.idrssd = Integer.parseInt(value); //Currently, idrssd is the only integer. In future, if there are other keys, we need to set them accordingly.
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiMV, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiMV, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiMV, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectOttiMV.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectOttiMV.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOttiMV, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectOTTIMV().add(smryDataCollectOttiMV);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_OTTI_MV Processed record#="+ recordCount);

		}
		FileChooserController.printToConsole("SMRY_OTTI_MV Processed total # of records="+ recordCount);
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OTTI_MVxml - END " + recordCount);

	return true;
	}
	
	/*
	 * Report 9 : SMRY_Data_Collect_OPSRISK
	 */
	private static boolean process_SMRY_OPSRISKxml() throws DatatypeConfigurationException, IllegalAccessException, InvocationTargetException{
		log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OPSRISKxml - START ");

		List <Map> opsRiskMdlList =BNYSMRYRModelCollection.getSMRY_OPSRISK();
		int recordCount = 0;
		for (Iterator<Map> iter = opsRiskMdlList.iterator(); iter.hasNext(); ) {
			SMRYDataCollectOPSRISK smryDataCollectOpsRisk = summaryFty.createSMRYDataCollectOPSRISK();
			Map<String, String> xlOpsRiskRow = iter.next();

			for (Map.Entry<String, String> entry : xlOpsRiskRow.entrySet())
			{
			    //log.debug("** Key=" + entry.getKey() + "/" + entry.getValue());
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if (BNYSMRYDataDictionary.SMRY_OPSRISK_AttributeList.containsKey(key)){
			    	String dataType = BNYSMRYDataDictionary.SMRY_OPSRISK_AttributeList.get(key);
			    	if ("INTEGER".equalsIgnoreCase(dataType)){
			    		//value = value.substring(0, value.indexOf("."));
			    		smryDataCollectOpsRisk.idrssd = Integer.parseInt(value);
			    	} 
			    	else if ("STRING_25".equalsIgnoreCase(dataType) && value.length() >25){
			    		value = value.substring(0,25);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOpsRisk, key, value);
			    	} else if ("STRING_50".equalsIgnoreCase(dataType) && value.length() >50){
			    		value = value.substring(0,50);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOpsRisk, key, value);
			    	} else if ("STRING_250".equalsIgnoreCase(dataType) && value.length() >250){
			    		value = value.substring(0,250);
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOpsRisk, key, value);
			    	}		
			    	else if ("TRANSTYPE_IUD".equalsIgnoreCase(dataType)) {
			    		smryDataCollectOpsRisk.transtype = 	TRANSTYPEIUD.fromValue(value);
			    	} else if ("DATE_TIME".equalsIgnoreCase(dataType)) {
			    		try {
			    			smryDataCollectOpsRisk.ddt = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							FileChooserController.printToConsole("Please ensure it is in the correct format : YYYY-MM-DDThh:mm:ss");
							e.printStackTrace();
							log.error(e);
							return false;
						}
			    	} else {
			    		com.bny.castxc.util.ReflectUtils.callSetter(smryDataCollectOpsRisk, key, value);
			    	}
			    }
			}
		
		smryDataCollect.getSMRYDataCollectOPSRISK().add(smryDataCollectOpsRisk);
		recordCount++;
		//FileChooserController.printToConsole("SMRY_OPSRisk Processed record#="+ recordCount);

		}
	FileChooserController.printToConsole("SMRY_SMRY_OPSRisk Processed total # of records="+ recordCount);
	log.info ( "***** BNYSummaryXMLProcessor.process_SMRY_OPSRISKxml - END " + recordCount);

	return true;
	}
	
	
	public static int strToInt( String str ){
		  int i = 0;
		  int num = 0;
		  boolean isNeg = false;

		  //check for negative sign; if it's there, set the isNeg flag
		  if( str.charAt(0) == '-') {
		    isNeg = true;
		    i = 1;
		  }

		  //process each char of the string; 
		  while( i < str.length()) {
		    num *= 10;
		    num += str.charAt(i++) - '0'; //minus the ASCII code of '0' to get the value of the charAt(i++)
		  }

		  if (isNeg)
		    num = -num;
		  return num;
		}
	
	
}
