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

import br.ufrn.spl.ev.models.dependencemodel.AssetDependence;
import br.ufrn.spl.ev.models.dependencemodel.Dependence;
import br.ufrn.spl.ev.models.dependencemodel.DependenceModel;

import com.thoughtworks.xstream.XStream;

/**
 * A parser to read or write the model to a file using Xstream library
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DependenceModelXStreamParser implements DependenceModelParser {

	/**
	 * @see br.ufrn.spl.ev.parsers.DependenceModelParser#readDependenceFile(java.io.File)
	 */
	@Override
	public DependenceModel readDependenceFile(File dependenceFile) {
		BufferedReader input = null;

		try {
			XStream xStream = new XStream();

			setAlias(xStream);
		
			input = new BufferedReader(new FileReader(dependenceFile));
			DependenceModel map = (DependenceModel) xStream.fromXML(input);

			return map;

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
	 * @see br.ufrn.spl.ev.parsers.DependenceModelParser#writeDependenceFile(br.ufrn.spl.ev.models.dependencemodel.DependenceModel, java.io.File)
	 */
	@Override
	public void writeDependenceFile(DependenceModel dependenceMap, File dependenceFile) {

		if(dependenceMap == null) return;

		BufferedWriter output = null;

		try {
			XStream xStream = new XStream();
			
			setAlias(xStream);
			
			output = new BufferedWriter(new FileWriter(dependenceFile));
			xStream.toXML(dependenceMap, output);

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
		xStream.alias("dependenceModel", DependenceModel.class);
		xStream.alias("depedences", ArrayList.class);
		xStream.alias("dependence", Dependence.class);
		xStream.alias("assetsDependents", ArrayList.class);
		xStream.alias("assetDependent", AssetDependence.class);
		xStream.alias("asset", String.class);
	}

}
