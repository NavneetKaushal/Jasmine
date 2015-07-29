//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.15 at 01:11:28 PM CST 
//


package com.bny.castxc.schedule.summary;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}SMRY_Data_Collect_ACT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_PROJ" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_VINT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_CAP_PROJ" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_OTTI_ACT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_OTTI_PROJ" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_OTTI_CUSIP" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_OTTI_MV" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMRY_Data_Collect_OPSRISK" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="DATA_ASOF_TSTMP" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="LAST_ASOF_TSTMP" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "smryDataCollectACT",
    "smryDataCollectPROJ",
    "smryDataCollectVINT",
    "smryDataCollectCAPPROJ",
    "smryDataCollectOTTIACT",
    "smryDataCollectOTTIPROJ",
    "smryDataCollectOTTICUSIP",
    "smryDataCollectOTTIMV",
    "smryDataCollectOPSRISK"
})
@XmlRootElement(name = "SMRY_Data_Collect")
public class SMRYDataCollect {

    @XmlElement(name = "SMRY_Data_Collect_ACT")
    protected List<SMRYDataCollectACT> smryDataCollectACT;
    @XmlElement(name = "SMRY_Data_Collect_PROJ")
    protected List<SMRYDataCollectPROJ> smryDataCollectPROJ;
    @XmlElement(name = "SMRY_Data_Collect_VINT")
    protected List<SMRYDataCollectVINT> smryDataCollectVINT;
    @XmlElement(name = "SMRY_Data_Collect_CAP_PROJ")
    protected List<SMRYDataCollectCAPPROJ> smryDataCollectCAPPROJ;
    @XmlElement(name = "SMRY_Data_Collect_OTTI_ACT")
    protected List<SMRYDataCollectOTTIACT> smryDataCollectOTTIACT;
    @XmlElement(name = "SMRY_Data_Collect_OTTI_PROJ")
    protected List<SMRYDataCollectOTTIPROJ> smryDataCollectOTTIPROJ;
    @XmlElement(name = "SMRY_Data_Collect_OTTI_CUSIP")
    protected List<SMRYDataCollectOTTICUSIP> smryDataCollectOTTICUSIP;
    @XmlElement(name = "SMRY_Data_Collect_OTTI_MV")
    protected List<SMRYDataCollectOTTIMV> smryDataCollectOTTIMV;
    @XmlElement(name = "SMRY_Data_Collect_OPSRISK")
    protected List<SMRYDataCollectOPSRISK> smryDataCollectOPSRISK;
    @XmlAttribute(name = "DATA_ASOF_TSTMP", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataasoftstmp;
    @XmlAttribute(name = "LAST_ASOF_TSTMP")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastasoftstmp;

    /**
     * Gets the value of the smryDataCollectACT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectACT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectACT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectACT }
     * 
     * 
     */
    public List<SMRYDataCollectACT> getSMRYDataCollectACT() {
        if (smryDataCollectACT == null) {
            smryDataCollectACT = new ArrayList<SMRYDataCollectACT>();
        }
        return this.smryDataCollectACT;
    }

    /**
     * Gets the value of the smryDataCollectPROJ property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectPROJ property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectPROJ().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectPROJ }
     * 
     * 
     */
    public List<SMRYDataCollectPROJ> getSMRYDataCollectPROJ() {
        if (smryDataCollectPROJ == null) {
            smryDataCollectPROJ = new ArrayList<SMRYDataCollectPROJ>();
        }
        return this.smryDataCollectPROJ;
    }

    /**
     * Gets the value of the smryDataCollectVINT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectVINT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectVINT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectVINT }
     * 
     * 
     */
    public List<SMRYDataCollectVINT> getSMRYDataCollectVINT() {
        if (smryDataCollectVINT == null) {
            smryDataCollectVINT = new ArrayList<SMRYDataCollectVINT>();
        }
        return this.smryDataCollectVINT;
    }

    /**
     * Gets the value of the smryDataCollectCAPPROJ property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectCAPPROJ property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectCAPPROJ().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectCAPPROJ }
     * 
     * 
     */
    public List<SMRYDataCollectCAPPROJ> getSMRYDataCollectCAPPROJ() {
        if (smryDataCollectCAPPROJ == null) {
            smryDataCollectCAPPROJ = new ArrayList<SMRYDataCollectCAPPROJ>();
        }
        return this.smryDataCollectCAPPROJ;
    }

    /**
     * Gets the value of the smryDataCollectOTTIACT property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectOTTIACT property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectOTTIACT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectOTTIACT }
     * 
     * 
     */
    public List<SMRYDataCollectOTTIACT> getSMRYDataCollectOTTIACT() {
        if (smryDataCollectOTTIACT == null) {
            smryDataCollectOTTIACT = new ArrayList<SMRYDataCollectOTTIACT>();
        }
        return this.smryDataCollectOTTIACT;
    }

    /**
     * Gets the value of the smryDataCollectOTTIPROJ property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectOTTIPROJ property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectOTTIPROJ().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectOTTIPROJ }
     * 
     * 
     */
    public List<SMRYDataCollectOTTIPROJ> getSMRYDataCollectOTTIPROJ() {
        if (smryDataCollectOTTIPROJ == null) {
            smryDataCollectOTTIPROJ = new ArrayList<SMRYDataCollectOTTIPROJ>();
        }
        return this.smryDataCollectOTTIPROJ;
    }

    /**
     * Gets the value of the smryDataCollectOTTICUSIP property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectOTTICUSIP property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectOTTICUSIP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectOTTICUSIP }
     * 
     * 
     */
    public List<SMRYDataCollectOTTICUSIP> getSMRYDataCollectOTTICUSIP() {
        if (smryDataCollectOTTICUSIP == null) {
            smryDataCollectOTTICUSIP = new ArrayList<SMRYDataCollectOTTICUSIP>();
        }
        return this.smryDataCollectOTTICUSIP;
    }

    /**
     * Gets the value of the smryDataCollectOTTIMV property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectOTTIMV property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectOTTIMV().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectOTTIMV }
     * 
     * 
     */
    public List<SMRYDataCollectOTTIMV> getSMRYDataCollectOTTIMV() {
        if (smryDataCollectOTTIMV == null) {
            smryDataCollectOTTIMV = new ArrayList<SMRYDataCollectOTTIMV>();
        }
        return this.smryDataCollectOTTIMV;
    }

    /**
     * Gets the value of the smryDataCollectOPSRISK property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smryDataCollectOPSRISK property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMRYDataCollectOPSRISK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMRYDataCollectOPSRISK }
     * 
     * 
     */
    public List<SMRYDataCollectOPSRISK> getSMRYDataCollectOPSRISK() {
        if (smryDataCollectOPSRISK == null) {
            smryDataCollectOPSRISK = new ArrayList<SMRYDataCollectOPSRISK>();
        }
        return this.smryDataCollectOPSRISK;
    }

    /**
     * Gets the value of the dataasoftstmp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAASOFTSTMP() {
        return dataasoftstmp;
    }

    /**
     * Sets the value of the dataasoftstmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAASOFTSTMP(XMLGregorianCalendar value) {
        this.dataasoftstmp = value;
    }

    /**
     * Gets the value of the lastasoftstmp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLASTASOFTSTMP() {
        return lastasoftstmp;
    }

    /**
     * Sets the value of the lastasoftstmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLASTASOFTSTMP(XMLGregorianCalendar value) {
        this.lastasoftstmp = value;
    }

}