/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.parsers;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.ufrn.spl.ev.models.conflictmodel.ConflictModel;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ConflictModelJAXBParser implements ConflictModelParser{

	@Override
	public ConflictModel readConflictModelFile(File xmlConflictModelFile) {
		
		try {

			if(! xmlConflictModelFile.exists()) return new ConflictModel();
			
			JAXBContext contextBuild = JAXBContext.newInstance(ConflictModel.class);
			Unmarshaller unmarshaller = contextBuild.createUnmarshaller();

			ConflictModel model = (ConflictModel) unmarshaller.unmarshal(xmlConflictModelFile);
			return model;
			
		} catch (JAXBException e) {
			System.err.println("Error reading Conflict Model: "+e.getMessage());
			return null;
		}
	}

	@Override
	public void writeConflictModelFile(ConflictModel conflictModel, File xmlConflictModelFile) {
		try {
			
			JAXBContext contextBuild = JAXBContext.newInstance(ConflictModel.class);
			Marshaller marshaller = contextBuild.createMarshaller();

			marshaller.marshal(conflictModel, xmlConflictModelFile);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
}
