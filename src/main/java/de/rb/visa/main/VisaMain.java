package de.rb.visa.main;

import java.util.List;
import java.nio.ByteBuffer;
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

	public static void main(String[] args) throws Exception{	
		JVisaInstrument dev = new JVisaInstrument();
		JVisaInstrument dev2 = new JVisaInstrument();
		long visaStatus;
		JVisaStatus jvisaStatus = new JVisaStatus(255, "UTF-8");
		JVisaReturnString response = new JVisaReturnString();
		JVisaReturnNumber visaNumber = new JVisaReturnNumber(0);
		NativeLongByReference listPtr = new NativeLongByReference();
		NativeLongByReference count = new NativeLongByReference();
		String search = "?*INSTR";
		ByteBuffer expr = dev.stringToByteBuffer(search);
		List<String> devList = new ArrayList<>();
		ByteBuffer found = ByteBuffer.allocate(256);
		found.position(0);
		
		dev.openDefaultResourceManager();
//		
//		dev.openInstrument("GPIB0::14::INSTR");
//		
//		visaStatus = dev.write("DC;>\n");
//		if(visaStatus != VisatypeLibrary.VI_SUCCESS) {
//			dev.closeInstrument();
//			dev.closeResourceManager();
//			return;
//		}
//		
//		//dev.setAttribute(VisaLibrary.VI_ATTR_TMO_VALUE, 2500);
//		//Thread.sleep(2500);
//		
//		dev.read(response);
//		System.out.println(response.returnString);
//		
//		
		// SessionID, Search Expr, List, 
		NativeLong session = new NativeLong(dev.getResourceManagerHandle());
		
		jvisaStatus.setStatus(dev.visaLib.viFindRsrc(session, expr, listPtr, count, found));
		//devList.add(new String(found.array(), Charset.forName("UTF8")));
		found.position(0);
		while(jvisaStatus.visaStatusLong == VisatypeLibrary.VI_SUCCESS) {
			jvisaStatus.setStatus(dev.visaLib.viFindNext(listPtr.getValue(), found));
			if(jvisaStatus.visaStatusLong == VisatypeLibrary.VI_SUCCESS) {
				devList.add(new String(found.array(), Charset.forName("UTF8")));
			}
			
			found.position(0);
		}
		for(String s : devList) {
			if(s.contains("GPIB")) {
				dev.openInstrument(s);
				dev.setAttribute( VisaLibrary.VI_ATTR_TERMCHAR, (int)'\n', dev.getInstrumentHandle());
				dev.setAttribute(VisaLibrary.VI_ATTR_TERMCHAR_EN, VisatypeLibrary.VI_TRUE, dev.getInstrumentHandle());
				
				dev.readId(response);
				System.out.println(s + "(" + dev.getId().replace('\n',' ') + ")");
				dev.closeInstrument();
			}

		}
		//dev.closeInstrument();
		//dev.closeResourceManager();
	
	}
	

}
