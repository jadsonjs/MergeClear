/**
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE –- UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA -– DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 *
 */
package br.ufrn.spl.ev.util;


/**
 * Utility class to manager the Product Line Conflict Model.
 * 
 * @author jadson - jadson@info.ufrn.br
 * @author Gleydson - gleydson@esig.com.br
 * 
 * 
 */

public class ConflictModelUtil {

//	/**
//	 * <p>
//	 * Read the ProductLineConflictModel from a XML file
//	 * </p>
//	 * 
//	 * 
//	 * @return
//	 * @throws JAXBException
//	 */
//	public static ConflictModel readConflictModelToFile(File xmlProductLineConflictModelFile) {
//
//		try {
//
//			JAXBContext contextBuild = JAXBContext.newInstance(ConflictModel.class);
//			Unmarshaller unmarshaller = contextBuild.createUnmarshaller();
//
//			ConflictModel model = (ConflictModel) unmarshaller.unmarshal(xmlProductLineConflictModelFile);
//			return model;
//			
//		} catch (JAXBException e) {
//			System.err.println("Error reading Conflict Model: "+e.getMessage());
//			return null;
//		}
//	}
//
//	/**
//	 * <p>
//	 * Write the ProductLineConflictModel to a XML file
//	 * </p>
//	 * 
//	 * 
//	 * @return
//	 * @throws JAXBException
//	 */
//	public static void writeConflictModelToFile(File xmlFile) {
//
//		try {
//			
//			JAXBContext contextBuild = JAXBContext.newInstance(ConflictModel.class);
//			Marshaller marshaller = contextBuild.createMarshaller();
//
//			//marshaller.marshal(ConflictRepository.getInstance().getModel(), xmlFile);
//			
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//	}
}
