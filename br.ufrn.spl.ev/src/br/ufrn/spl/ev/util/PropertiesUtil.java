/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Class to manager properties files of the tool
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class PropertiesUtil {
	
	/**
	 *  Represent the two types of properties files we have in the plug-in.
	 *  
	 * @author jadson - jadson@info.ufrn.br
	 *
	 */
	public enum PluginProperties{
		CONFIG(     "platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/config.properties"), 
		CONNECTIONS("platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/connections.properties"),
		CONNECTOR(  "platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/connectors.properties");
		
		private String path;
		
		private PluginProperties(String path){
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}
	
	
	/** Return a specific property of the plug-in */
	public static String readProperty(PluginProperties pp, String propertieKey) {
		return readPropertiesObject(pp).getProperty(propertieKey);
	}
	
	
	
	/** Write a specific property to a file */
	public static void writeProperty(PluginProperties pp, String propertieName, String propertieValue) {
	    Properties propSave = readPropertiesObject(pp);
	    propSave.setProperty(propertieName, propertieValue);
	    writePropertiesObject(pp, propSave);
	}
	
	
	/** Return properties object of the plug-in */
	public static Properties readPropertiesObject(PluginProperties pp) {
		
		InputStream input = null;
		try{
			Properties properties = new Properties();
			URL urlObject = new URL(pp.getPath());
			input = urlObject.openConnection().getInputStream();
			properties.load(input);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			if(input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/** Write a full property object with all properties load */
	public static void writePropertiesObject(PluginProperties pp, Properties propSave) {
		OutputStream output = null;		
		try {
	    	
	    	URL urlObject = new URL(pp.getPath());
	    
	    	output = urlObject.openConnection().getOutputStream();
	    	
//	    	Writing to the plug-in root is usually a bad idea, since the plug-in
//	    	directory may be read-only, access-controlled, or otherwise not writeable.
//	    	Also, if the plug-in is contained within a jar file, as they often are, the
//	    	root is not a writeable location.
//
//	    	Depending on your application, perhaps a better idea would be to write to
//	    	the plug-in's metadata location, or to the Eclipse install's configuration
//	    	location? These are both intended for write access.
//
//	    	To get the plug-in's metadata location, use Plugin.getStateLocation(). To
//	    	get the Eclipse configuration location, use
//	    	Platform.getConfigurationLocation(). The Platform object also gives you a
//	    	number of other useful locations you may want to consider. 
	    	propSave.store(output, null);
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }finally{
			if(output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
