package de.rb.visa;

import jvisa.JVisa;

public abstract class JVisaResource extends JVisa{
	
	
	
	private JVisaResource(){
		super();
	};
	
	protected JVisaResource openResource(String resourceName) {
		
		if(visaResourceManagerHandle == 0) {
			super.openDefaultResourceManager();
		}
		
		if(this.visaInstrumentHandle != null) {
			return this;
		} else {
			super.openInstrument(resourceName);
			return new JVisaResource() {};
		}
	}
	
	
	
}
