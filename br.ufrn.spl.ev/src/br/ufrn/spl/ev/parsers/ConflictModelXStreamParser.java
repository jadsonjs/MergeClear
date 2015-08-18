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

import br.ufrn.spl.ev.models.conflictmodel.AssetConflict;
import br.ufrn.spl.ev.models.conflictmodel.Conflict;
import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

import com.thoughtworks.xstream.XStream;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ConflictModelXStreamParser implements ConflictModelParser{

	@Override
	public ConflictModel readConflictModelFile(File conflictModelFile) {
		
		BufferedReader input = null;

		try {
			XStream xStream = new XStream();

			setAlias(xStream);
		
			input = new BufferedReader(new FileReader(conflictModelFile));
			ConflictModel model = (ConflictModel) xStream.fromXML(input);

			return model;

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

	
	
	@Override
	public void writeConflictModelFile(ConflictModel conflictModel, File dependenceFile) {
		
		if(conflictModel == null) return;

		BufferedWriter output = null;

		try {
			XStream xStream = new XStream();
			
			setAlias(xStream);
			
			output = new BufferedWriter(new FileWriter(dependenceFile));
			xStream.toXML(conflictModel, output);

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
	
	
	private void setAlias(XStream xStream) {
		xStream.alias("conflictModel", ConflictModel.class);
		xStream.alias("conflicts", ArrayList.class);
		xStream.alias("conflict", Conflict.class);
		xStream.alias("assetConflict", AssetConflict.class);
		xStream.alias("targetConflictsRelated", ArrayList.class);
	}

}
