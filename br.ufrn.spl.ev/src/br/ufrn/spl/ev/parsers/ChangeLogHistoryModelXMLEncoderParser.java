/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;

/**
 * Implementation of a parser to XML files using XMLEncoder
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeLogHistoryModelXMLEncoderParser extends Object implements ChangeLogHistoryModelParser {

	/**
	 * @see br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParser#readHistoryChangeLogFile(java.io.File)
	 */
	@Override
	public ChangeLogHistory readHistoryChangeLogFile(File changeLogFile) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(changeLogFile);
			BufferedInputStream buf = new BufferedInputStream(fis);
			XMLDecoder xml = new XMLDecoder(buf);
			Object object = xml.readObject();
			xml.close();

			return (ChangeLogHistory) object;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParser#writeHistoryChangeLogFile(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory, java.io.File)
	 */
	@Override
	public void writeHistoryChangeLogFile(ChangeLogHistory changeLogHistory, File changeLogFile) {
		BufferedOutputStream buf;
		try {
			FileOutputStream fos = new FileOutputStream(changeLogFile);
			buf = new BufferedOutputStream(fos);
			XMLEncoder xml = new XMLEncoder(buf);
			xml.writeObject(changeLogHistory);
			xml.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
