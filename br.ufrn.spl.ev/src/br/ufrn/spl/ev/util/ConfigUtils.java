/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.util.Properties;

import br.ufrn.spl.ev.util.PropertiesUtil.PluginProperties;

/**
 * <p>
 * Util classe for the puglin configuration.
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 11/10/2013
 * 
 */
public class ConfigUtils {

	private static Properties propConfig = null;
	private static Properties propConnector = null;
	
	/** Return a configuration from the configuration file */
	public static String getConfiguration(String name) {

		if (propConfig == null) {
			propConfig = PropertiesUtil.readPropertiesObject(PluginProperties.CONFIG);
		}
		return propConfig.getProperty(name);
	}
	
	/** Return a configuration from the connection file */
	public static String getConnection(String name) {

		if (propConnector == null) {
			propConnector = PropertiesUtil.readPropertiesObject(PluginProperties.CONNECTIONS);
		}
		return propConnector.getProperty(name);
	}

//	public static ConflictAnalysisLevel getConflictAnalysisLevel() {
//
//		String level = getConfiguration(PluginConstants.CONFLICT_ANALYSIS_LEVEL);
//		
//		if ( level.equals("ALL")) {
//			return ConflictAnalysisLevel.ALL;
//		} else if ( level.equals("BODY") ) {
//			return ConflictAnalysisLevel.BODY;
//		} else {
//			throw new RuntimeException("Configuração de conflito não reconhecida: " + level );
//		}
//		
//	}

}