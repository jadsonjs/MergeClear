/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.ufrn.spl.ev.models.changeloghistorymodel.AnnotationChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.CodePieceChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ExceptionChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ExtensionChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ImplementationsChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.variability.Variability;

import com.thoughtworks.xstream.XStream;

/**
 * Implementation of a parser to XML files using XStream
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeLogHistoryModelXStreamParser implements ChangeLogHistoryModelParser {

	/**
	 * @see br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParser#readHistoryChangeLogFile(java.io.File)
	 */
	@Override
	public ChangeLogHistory readHistoryChangeLogFile(File changeLogFile) {
		BufferedReader input = null;

		try {
			XStream xStream = new XStream();

			xStream.alias("changeloghistory", ChangeLogHistory.class);
			xStream.alias("features", ArrayList.class);
			xStream.alias("feature", FeatureChangeLog.class);
			xStream.alias("changelogs", ArrayList.class);
			xStream.alias("changelog", ChangeLog.class);
			xStream.alias("classes", ArrayList.class);
			xStream.alias("class", ClassChangeLog.class);
			xStream.alias("methods", ArrayList.class);
			xStream.alias("method", MethodChangeLog.class);
			xStream.alias("codepieces", ArrayList.class);
			xStream.alias("codepiece", CodePieceChangeLog.class);
			xStream.alias("exceptions", ArrayList.class);
			xStream.alias("exception", ExceptionChangeLog.class);
			xStream.alias("fields", ArrayList.class);
			xStream.alias("field", FieldChangeLog.class);
			xStream.alias("variationPoint", Variability.class);
			xStream.alias("annotations", ArrayList.class);
			xStream.alias("annotation", AnnotationChangeLog.class);
			xStream.alias("implementations", ArrayList.class);
			xStream.alias("implementation", ImplementationsChangeLog.class);
			xStream.alias("extension", ExtensionChangeLog.class);

			input = new BufferedReader(new FileReader(changeLogFile));
			ChangeLogHistory history = (ChangeLogHistory) xStream.fromXML(input);

			return history;

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return null;
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see br.ufrn.spl.ev.parsers.ChangeLogHistoryModelParser#writeHistoryChangeLogFile(br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory, java.io.File)
	 */
	@Override
	public void writeHistoryChangeLogFile(ChangeLogHistory changeLogHistory, File changeLogFile) {

		if(changeLogHistory == null) return;
		
		changeLogHistory.clearTransientsValues(); // clear the transients values before save it

		BufferedWriter output = null;

		try {
			XStream xStream = new XStream();

			xStream.alias("changeloghistory", ChangeLogHistory.class);
			xStream.alias("features", ArrayList.class);
			xStream.alias("feature", FeatureChangeLog.class);
			xStream.alias("changelogs", ArrayList.class);
			xStream.alias("changelog", ChangeLog.class);
			xStream.alias("classes", ArrayList.class);
			xStream.alias("class", ClassChangeLog.class);
			xStream.alias("methods", ArrayList.class);
			xStream.alias("method", MethodChangeLog.class);
			xStream.alias("codepieces", ArrayList.class);
			xStream.alias("codepiece", CodePieceChangeLog.class);
			xStream.alias("exceptions", ArrayList.class);
			xStream.alias("exception", ExceptionChangeLog.class);
			xStream.alias("fields", ArrayList.class);
			xStream.alias("field", FieldChangeLog.class);
			xStream.alias("variationPoint", Variability.class);
			xStream.alias("annotations", ArrayList.class);
			xStream.alias("annotation", AnnotationChangeLog.class);
			xStream.alias("implementations", ArrayList.class);
			xStream.alias("implementation", ImplementationsChangeLog.class);
			xStream.alias("extension", ExtensionChangeLog.class);

			output = new BufferedWriter(new FileWriter(changeLogFile));
			xStream.toXML(changeLogHistory, output);

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
