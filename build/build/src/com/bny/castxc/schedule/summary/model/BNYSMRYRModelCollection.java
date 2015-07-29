package com.bny.castxc.schedule.summary.model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bny.castxc.util.XSLXProcessor;

public class BNYSMRYRModelCollection {
	
	//private static List<BNYSMRYOTTIACTModel> ottiActModelList;
	private static List<Map> SMRY_ACT;
	private static List<Map> SMRY_PROJ;
	private static List<Map> SMRY_VINT;
	private static List<Map> SMRY_CAP_PROJ;
	private static List<Map> SMRY_OTTI_ACT;
	private static List<Map> SMRY_OTTI_PROJ;
	private static List<Map> SMRY_OTTI_CUSIP;
	private static List<Map> SMRY_OTTI_MV;
	private static List<Map> SMRY_OPSRISK;
	

	/*public static List<Map> getOttiActModelList() {
		return ottiActModelList;
	}

	public void setOttiActModelList(List<Map> ottiActModelList) {
		this.ottiActModelList = ottiActModelList;
	}*/

	public static List<Map> getSMRY_ACT() {
		return SMRY_ACT;
	}

	public static void setSMRY_ACT(List<Map> sMRY_ACT) {
		SMRY_ACT = sMRY_ACT;
	}

	public static List<Map> getSMRY_PROJ() {
		return SMRY_PROJ;
	}

	public static void setSMRY_PROJ(List<Map> sMRY_PROJ) {
		SMRY_PROJ = sMRY_PROJ;
	}

	public static List<Map> getSMRY_VINT() {
		return SMRY_VINT;
	}

	public static void setSMRY_VINT(List<Map> sMRY_VINT) {
		SMRY_VINT = sMRY_VINT;
	}

	public static List<Map> getSMRY_CAP_PROJ() {
		return SMRY_CAP_PROJ;
	}

	public static void setSMRY_CAP_PROJ(List<Map> sMRY_CAP_PROJ) {
		SMRY_CAP_PROJ = sMRY_CAP_PROJ;
	}

	public static List<Map> getSMRY_OTTI_ACT() {
		return SMRY_OTTI_ACT;
	}

	public static void setSMRY_OTTI_ACT(List<Map> sMRY_OTTI_ACT) {
		SMRY_OTTI_ACT = sMRY_OTTI_ACT;
	}

	public static List<Map> getSMRY_OTTI_PROJ() {
		return SMRY_OTTI_PROJ;
	}

	public static void setSMRY_OTTI_PROJ(List<Map> sMRY_OTTI_PROJ) {
		SMRY_OTTI_PROJ = sMRY_OTTI_PROJ;
	}

	public static List<Map> getSMRY_OTTI_CUSIP() {
		return SMRY_OTTI_CUSIP;
	}

	public static void setSMRY_OTTI_CUSIP(List<Map> sMRY_OTTI_CUSIP) {
		SMRY_OTTI_CUSIP = sMRY_OTTI_CUSIP;
	}

	public static List<Map> getSMRY_OTTI_MV() {
		return SMRY_OTTI_MV;
	}

	public static void setSMRY_OTTI_MV(List<Map> sMRY_OTTI_MV) {
		SMRY_OTTI_MV = sMRY_OTTI_MV;
	}

	public static List<Map> getSMRY_OPSRISK() {
		return SMRY_OPSRISK;
	}

	public static void setSMRY_OPSRISK(List<Map> sMRY_OPSRISK) {
		SMRY_OPSRISK = sMRY_OPSRISK;
	}
	
}
