package de.rb.visa.main;

import java.util.List;
import java.util.logging.Logger;

import javax.crypto.ShortBufferException;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.NativeLongByReference;

import jvisa.*;
import visa.VisaLibrary;
import visa.VisaLibrary.ViStatus;
import visatype.VisatypeLibrary;

public class VisaMain {

	private List<String> deviceList;
	
	private JVisa defaultRessourceManager;
	
	public static final String CLASS_NAME = VisaMain.class.getName(); 
	
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
	
	public VisaMain() {
		super();
		deviceList = new ArrayList<>();
		defaultRessourceManager = new JVisa();
		defaultRessourceManager.openDefaultResourceManager();
	}
	
	public static void main(String[] args) throws Exception{	
		
		new VisaMain().listDevices();
	
	}
	
	public void listDevices() {
		deviceList.clear();
		ByteBuffer searchExpr = defaultRessourceManager.stringToByteBuffer("?*INSTR");
		NativeLongByReference listPtr = new NativeLongByReference();
		NativeLongByReference countPtr = new NativeLongByReference();
		ShortBuffer interfaceType;
		NativeLong session;
		JVisaStatus visaStatus;
		ByteBuffer foundResString = ByteBuffer.allocate(256);
		interfaceType = ShortBuffer.allocate(16);
		interfaceType.position(0);
		foundResString.position(0);
		visaStatus = new JVisaStatus(255, "UTF-8");
		session = new NativeLong(defaultRessourceManager.getResourceManagerHandle());
		
		visaStatus.setStatus(JVisa.visaLib.viFindRsrc(session, searchExpr, listPtr, countPtr, foundResString));
		if(countPtr.getValue().intValue() > 0) {
			JVisa.visaLib.viParseRsrc(session, foundResString, interfaceType, null);
			deviceList.add(new String(foundResString.array()).replace('\n', ' ').trim());
			LOGGER.info(String.format("countPtr: %d, listPtr: %d, device: %s", 
					countPtr.getValue().intValue(), listPtr.getValue().intValue(), deviceList.get(deviceList.size() - 1)));
		}
	}
	

}
