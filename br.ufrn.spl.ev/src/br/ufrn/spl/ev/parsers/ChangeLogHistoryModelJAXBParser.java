/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
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
