/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.File;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * Implementation of a parser to XML files using JABX
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeLogHistoryModelJAXBParser implements ChangeLogHistoryModelParser{

	@Override
	public ChangeLogHistory readHistoryChangeLogFile(File changeLogFile) {
		
		return null;
	}

	@Override
	public void writeHistoryChangeLogFile(ChangeLogHistory changeLogHistory, File changeLogFile) {
	
	}

}
