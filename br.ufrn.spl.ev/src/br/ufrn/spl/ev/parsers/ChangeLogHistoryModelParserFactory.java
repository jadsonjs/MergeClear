/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

/**
 * Creates the parser used to store the ChangeLog History Model
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeLogHistoryModelParserFactory {

	/**
	 * Configure here the type of Model parser you would like to use.
	 * 
	 * All parsers to the same thing, but using different libraries
	 * 
	 * @return
	 */
	public static ChangeLogHistoryModelParser getChangeLogHistoryModelParser(){
		return new ChangeLogHistoryModelXStreamParser();
		
		/* not implemented */
		//return new ChangeLogHistoryModelJAXBParser();
		
		/* it is not working correctly because it use the setters e getters methods of the object
		 * and the getters and setters of the change log do not return exactly the value of the variable
		 * @see MethodChagenLog#getSignature()
		 */
		// return new ChangeLogHistoryModelXMLEncoderParser(); 
	}
	
}
