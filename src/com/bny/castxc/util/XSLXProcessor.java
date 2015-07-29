package com.bny.castxc.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.bny.castxc.schedule.summary.BNYSummaryXMLProcessor;
import com.bny.castxc.schedule.summary.SMRYDataCollectPROJ;
import com.bny.castxc.schedule.summary.TRANSTYPEIUD;
import com.bny.castxc.schedule.summary.main.FileChooserController;
import com.bny.castxc.schedule.summary.model.BNYSMRYDataDictionary;
//import com.bny.castxc.schedule.summary.model.BNYSMRYOTTIACTModel;
import com.bny.castxc.schedule.summary.model.BNYSMRYRModelCollection;

import  java.math.BigDecimal;
import  java.math.BigInteger;
import java.math.RoundingMode;




public class XSLXProcessor {
	private static File xlsxFile ; 
	private static Logger log = Logger.getLogger(XSLXProcessor.class);
	static  String LOG_PROPERTIES_FILE = "";
	private static String ID_RSSID = "";

	
	
	public XSLXProcessor (File xlsxFile) {
		if (xlsxFile != null) {
			XSLXProcessor.xlsxFile = xlsxFile;
		}
		if ( OSDetector.isLinux() || OSDetector.isMac()) LOG_PROPERTIES_FILE =	System.getProperty("user.home") +"/BNY/log4j.properties";
		if (OSDetector.isWindows()) LOG_PROPERTIES_FILE = System.getProperty("user.home") + "\\BNY\\log4j.properties";

		
		Properties logProperties = new Properties();
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
		log.info("Logging initialized.");
		log.debug("XSLXProcessor- Logging Intialized using the properties=" + logProperties.getProperty("log4j.appender.file"));
		
		
	}
	
	public void xlsxFileProcess() throws Exception {
		
		FileChooserController.printToConsole("Input xlsx file processing - started.. ");
		XSSFWorkbook xwb = new XSSFWorkbook( new FileInputStream (XSLXProcessor.xlsxFile) );
		FileChooserController.printToConsole ( "Number of Sheets to be Processed = " + xwb.getNumberOfSheets());
		
			
		for ( int i = 0; i< xwb.getNumberOfSheets(); i++) {
			//FileChooserController.printToConsole ( "XLOOP Started processing the Sheet with the name= " + xwb.getSheetAt(i).getSheetName());
			
			XSSFSheet xsht = xwb.getSheetAt(i);
			xslxProcessSheets(xsht);
		}
		FileChooserController.printToConsole("Finished ingesting all of the sheets.....");

		
		//BNYSummaryXMLProcessor.summaryDataCollect();
		return;
		
	}//end-xslxFileProcess()
	
	private void xslxProcessSheets(XSSFSheet xsht1) throws Exception{
		if (xsht1 == null)
			return;
		FileChooserController.printToConsole ( "Started processing the Sheet with the name= " + xsht1.getSheetName());
		
		String xsheetName = xsht1.getSheetName();
		
		int rowNum = xsht1.getLastRowNum() + 1;
        int colNum = xsht1.getRow(0).getLastCellNum();
        String [][] data = new String [rowNum] [colNum];
        BNYSMRYRModelCollection smryMdlCollection = new BNYSMRYRModelCollection() ; 
        List<Map> xslxRowList = new ArrayList<Map>();
        
        XSSFRow keysRow = xsht1.getRow(0);
        Map< String, String> keys = new HashMap< String,String>() ;
        
        for (int i =0 ; i< colNum; i++) {
        	XSSFCell cell = keysRow.getCell(i);
            String value = "";
            if (cell!= null) {
            	cell.setCellType(Cell.CELL_TYPE_STRING);
            	value = cell.toString().trim();            	
            }
            keys.put(Integer.toString(i), value);
     		//System.out.println("Validation key=" + keys.get(Integer.toString(i)) + " value=" + cell.toString());
            
        }
        

        for(int i = 1; i <rowNum; i++){
        	Map< String, String> ottiACTrow = new HashMap <String, String>();
            XSSFRow row = xsht1.getRow(i);
            if(row != null){
                //System.out.println("JEEVAN Validation ROWNUM =" + i + " TOTAL rowNum = " + rowNum);
                int rowNmbr = row.getRowNum();
                for (int j = 0; j < colNum; j++){
                	XSSFCell cell = row.getCell(j);
                	String value = "";
                
                	String value2 = "";
                	String value3 = "";
                	String value4 = "";
                	String value5 = "";
                	
                	if(i == 210){
                		if (cell!= null){
                			//System.out.println("JEEVAN COL VAL CELLCOLNUM populated=" + j);
                		}else{
                			//System.out.println("JEEVAN COL VAL CELLCOLNUM NOT populated=" + j);
                            
                		}
                	}
                	
                	/*
                	if (cell!= null) {
                	if(cell.getCellType() == cell.CELL_TYPE_NUMERIC) { 
                		DataFormatter df = new DataFormatter();
                		
                		//value2 = cell.getNumericCellValue());
                		value3 =  cell.getCellStyle().getDataFormatString();
                		   
                		int xio = (int)cell.getNumericCellValue(); 
                		double xi = (double)cell.getNumericCellValue(); 
                		if(xio == xi){
                			value2 = String.valueOf(xio);
                		}else{
                			value2 = String.valueOf(xi);
                		}
                		
                		//value2 = String.valueOf(xi); 
                		//value3 = cell.toString(); 
                		}
                	}*/
                	
                	if (cell!= null) {
                		
                		if(cell.getCellType() == cell.CELL_TYPE_NUMERIC) { 
                    		int cellIntValue = (int)cell.getNumericCellValue(); 
                    		double cellDoubleValue = (double)cell.getNumericCellValue(); 
                    		String cellStringValue = cell.toString(); 
                    		BigDecimal cellBigDecValue = new BigDecimal(cell.getNumericCellValue());
                    		BigDecimal cellBigDecValue2 = new BigDecimal(cell.getNumericCellValue());
                    		//cellBigDecValue2 = cellBigDecValue2.setScale(6, RoundingMode.CEILING);
                    		//cellBigDecValue2 = cellBigDecValue2(10, RoundingMode.CEILING);
                    		//int len = cell.
                    		
                    		BigDecimal cellBigDecValue3 = cellBigDecValue2;//new BigDecimal(0.78998765462);
                    		cellBigDecValue3 = cellBigDecValue3.setScale(6, RoundingMode.CEILING);
                    		
                    		
                    		if("CPSCN234".equalsIgnoreCase(keys.get(Integer.toString(j)))){
                    			BigInteger bigInt =  cellBigDecValue3.toBigInteger();
                    			String myscalenumber =  cellBigDecValue3.toPlainString();
                    			myscalenumber = "0000000000000000" + myscalenumber.replace(".", "");
                    			myscalenumber = myscalenumber.substring(myscalenumber.length()-16, myscalenumber.length());
                    			//System.out.println("cellBigDecValue3=" + cellBigDecValue3 + "  myscalenumber = " + myscalenumber);
                    			
                       	}
                    		
                    		double test = cellBigDecValue.doubleValue();
                    		String test2 = cellBigDecValue.toString();
                    		
                    		DataFormatter objDefaultFormat = new DataFormatter();
                    		//FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((XSSFWorkbook) xsht1.getWorkbook());

                    		String cellValueStr = objDefaultFormat.formatCellValue(cell);

                    		
                    		//if("CPSCN234".equalsIgnoreCase(keys.get(Integer.toString(j))))
                    		//System.out.println("cellValueStr=" + cellValueStr + " cellDoubleValue=" + cellDoubleValue + " cellStringValue=" + cellStringValue + " cellBigDecValue=" + cellBigDecValue + " BIG_TO_DOUBLE="+test + " STRING="+test2);
                    		
                    		//if("CPSCN234".equalsIgnoreCase(keys.get(Integer.toString(j))))
                    		//System.out.println("cellValueStr=" + BigDecimal.valueOf(cellDoubleValue).toPlainString() + " cellDoubleValue=" + cellDoubleValue + " cellStringValue=" + String.valueOf(cellDoubleValue) + " cellBigDecValue=" + cellBigDecValue + " cellBigDecValue2="+String.valueOf(cellBigDecValue2) );
                    		
                    		if(cellIntValue == cellDoubleValue){
                    			value = String.valueOf(cellIntValue);
                    		}else{
                    			value = BigDecimal.valueOf(cellDoubleValue).toPlainString();//String.valueOf(cellDoubleValue);
                    		}
                    		
                    		if(value.contains("E")){
                    			System.out.println("E_FIRED");
                    			value = cellBigDecValue.toString();
                    			//value = value.substring(0, 22);
                    			//value = cellValueStr;
                    		}
                    		
                    	}else{
                        	cell.setCellType(Cell.CELL_TYPE_STRING);
                    		value = cell.toString();
                    	}
                		
                		//value2 = cell.getNumericCellValue();
                 	}

            		if ( validateCell(keys.get(Integer.toString(j)), value,xsheetName, rowNmbr)){
                		ottiACTrow.put(keys.get(Integer.toString(j)), value);
                		//System.out.println("Validation perfect  key=" + keys.get(Integer.toString(j)) + " value=" + value);
                	}

            		//System.out.println("Validation perfect  key=" + keys.get(Integer.toString(j)) + " value=" + value);
                	//if("CPSNQ024".equalsIgnoreCase(keys.get(Integer.toString(j))))
            		//System.out.println("Validation int  value2=" + value2 + " value=" + value + " value3=" + value3);
                	
                	//if("CPSNQ016".equalsIgnoreCase(keys.get(Integer.toString(j))))
                		//System.out.println("Validation dec  value2=" + value2 + " value=" + value + " value3=" + value3);
            		                
            	}//end-for j   
                xslxRowList.add(ottiACTrow);
            }else{
            	//System.out.println("JEEVAN Validation NULL ROW =" + i + " TOTAL rowNum = " + rowNum);
            }
        } //end-for i
       FileChooserController.printToConsole ( "Total Number of rows processed = " + xslxRowList.size()  + " ...");
      
       if ("SMRY_ACT".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_ACT(xslxRowList);
       } else if ("SMRY_PROJ".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_PROJ(xslxRowList);
       } else if ("SMRY_VINT".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_VINT(xslxRowList);
       } else if ("SMRY_CAP_PROJ".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_CAP_PROJ(xslxRowList);
       } else if ("SMRY_OTTI_ACT".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_OTTI_ACT(xslxRowList);
       } else if ("SMRY_OTTI_PROJ".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_OTTI_PROJ(xslxRowList);
       } else if ("SMRY_OTTI_CUSIP".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_OTTI_CUSIP(xslxRowList);;
       } else if ("SMRY_OTTI_MV".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_OTTI_MV(xslxRowList);
       } else if ("SMRY_OPSRISK".equalsIgnoreCase(xsheetName)) {
    	   BNYSMRYRModelCollection.setSMRY_OPSRISK(xslxRowList);
       }
//       smryMdlCollection.setOttiActModelList(xslxRowList);
        
        //BNYSummaryXMLProcessor.summaryDataCollect();
	   
       
		
		FileChooserController.printToConsole ( "Finished ingesting the sheet with the name= " + xsht1.getSheetName());
		//validations();
		
	}
	
	private boolean validateCell(String key, String value, String xsheetName, int rowNum) throws Exception  {
		rowNum++;
		boolean throwExcption = false;
		boolean everythingisOK = true;
		String message = "**** DATA ERROR : Worksheet - " + xsheetName + " -  Row Number : "  + rowNum + " - " ;
		
		if ( BNYSMRYDataDictionary.REQUIRED_AttributeList.contains(key) && "".equalsIgnoreCase(value)){
			message = message + "The required " + key   + " column is missing!!!";
			everythingisOK=false;
		}
		
		if("DATA_ASOF_TSTMP".equalsIgnoreCase(key) || "D_DT".equalsIgnoreCase(key) || "LAST_ASOF_TSTMP".equalsIgnoreCase(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYYY-MM-DD'T'hh:mm:ss");
			try {
				sdf.parse(value);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message = message + "The required Date Format for the field  " + key   + " is NOT in the YYYYYY-MM-DDThh:mm:ss  format!!!";
				everythingisOK = false;
			}
		}
		
		if("TRANSTYPE".equalsIgnoreCase(key)){
			if (!"I".equalsIgnoreCase(value) && !"U".equalsIgnoreCase(value) && !"D".equalsIgnoreCase(value)){
				message = message + "The required TRANSTYPE is NOT correct. Please ensure it is eiether I (for Insert)  OR U (for Update)  OR D (for Delete)";
				everythingisOK=false;
			}
		}
		
		/*
		if ( ("ID_RSSD".equalsIgnoreCase(key) || "D_DT".equalsIgnoreCase(key)) && "".equalsIgnoreCase(value)) {
			  message = message + "The required columns ID_RSSD or D_DT is missing in your spreadsheet. Please correct and resubmit";	
			  throwExcption = true;
		}
		if ("SMRY_PROJ".equalsIgnoreCase(xsheetName)) {
			if ("CCARP006".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CCARP006 column is missing";throwExcption=true;}
			if ("CCARP009".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CCARP009 column is missing";throwExcption=true;}
	    }
		if ("SMRY_VINT".equalsIgnoreCase(xsheetName)) {
			if ("CCARP006".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CCARP006 column is missing";throwExcption=true;}
			if ("CPSVN998".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CPSVN998 column is missing";throwExcption=true;}
	    }
		if ("SMRY_VINT".equalsIgnoreCase(xsheetName)) {
			if ("CCARP006".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CCARP006 column is missing";throwExcption=true;}
			if ("CPSVN998".equalsIgnoreCase(key) && "".equalsIgnoreCase(value)) { message = message + "The required CPSVN998 column is missing";throwExcption=true;}
	    }
	    */
		
		if(!everythingisOK){
			FileChooserController.printToConsole(message);
			throw new Exception (message);
		}
		return  everythingisOK;

	}
}
