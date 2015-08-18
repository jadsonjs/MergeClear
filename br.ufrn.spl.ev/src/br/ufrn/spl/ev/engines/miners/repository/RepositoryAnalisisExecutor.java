package br.ufrn.spl.ev.engines.miners.repository;

import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.IOException;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.miners.algorithm.StructureDifferencerFactory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;

/**
 * <p>This class  analyzes the change that happened in the Java source code assets by analyzing 
 * the actual e previous version of the file on source code repository. 
 * <br/>
 * Until this point we here information about class evolution form CMS tools (like iproject and redmine), now we will look at specifics
 * change in each class.
 * </p>
 * 
 * <p>
 * 	<strong>
 *   It is a very expensive task for each revision number access the repository. 
 *   So any optimization here creating operation in batch would be welcome.
 *   </strong> 
 * </p>
 * 
 * @author Jadson
 * @author Daniel Alencar
 * @version 1.0 - class creation
 * @version 1.2 - Jadson at 15/12/2013 - Extracting a more specific information about the change location, 
 * if was in the body, name, annotation, exceptions, etc...
 * 
 * @version 2.0 - Jadson at 28/10/2014 - Delegating the algorithm of detect change to the ChangeDistiller 
 * https://bitbucket.org/sealuzh/tools-changedistiller/wiki/Home
 *
 */
public class RepositoryAnalisisExecutor {

	/**
	 * repositoryConnector is the object to read information from the source code repository  (CVS, SVN, GIT, MERCURIAL, ETC...)
	 * 
	 * All the access is made by this class, so RepositoryAnalisisExecutor can work with several types of source code repositories.
	 * 
	 */
	private RepositoryConnector repositoryConnector;

	
	public RepositoryAnalisisExecutor(RepositoryConnector repositoryConnector){
		this.repositoryConnector = repositoryConnector;
	}
	
	
	
	/**
	 * This method is responsible for retrieving the changed elements in the repository using the revision number.
	 * 
	 * @param user
	 *            - username of the repository
	 * @param password
	 *            - password of the repository
	 * @param changeLogs
	 *            - logs of the changes
	 * @throws IOException
	 * @throws SVNException
	 * @throws ParseException
	 */
	public ChangeLogHistory retrieveChangedFromRespository(ChangeLogHistory changeLogHistory) throws IOException, SVNException, ParseException {

		// for each feature
		for (FeatureChangeLog featureChangeLog : changeLogHistory.getFeatures()) {
			
			// for each change log (task)
			for (ChangeLog changeLog : featureChangeLog.getChangelogs()) {
				
				List<ClassChangeLog> classes = changeLog.getClasses();  // identified classes

				String previousResision = "";
				
				// for each class in the change log
				for (ClassChangeLog clazz : classes) {
					
					// if the class change
					if (! clazz.getChangeType().equals("D") && ! clazz.getChangeType().equals("A")  ) {

						try{
							
						
							CompilationUnit actualCompilationUnit = repositoryConnector.getClassContentInTheRevision(clazz.getPath(), clazz.getRevision());
							
							previousResision = repositoryConnector.getPreviousRevision( clazz.getPath(), clazz.getRevision() );
							
							CompilationUnit previousCompilationUnit = repositoryConnector.getClassContentInTheRevision(clazz.getPath(), previousResision+"");
							
							StructureDifferencerFactory.getStructureDifferencer().retrieveDifferencesInClass(actualCompilationUnit, previousCompilationUnit, featureChangeLog, changeLog, clazz);

						}catch (Exception svne) {

							if (svne.getMessage() != null && svne.getMessage().contains("is not a file in revision")) {
								/*
								 * The file can have been removed from the
								 * target, or change the location in the
								 * repository In this case, the class is regard
								 * to be a new class in this location.
								 * 
								 * because we are looking the previous versions,
								 * if not exist, so is a new class. got it ?
								 */
								clazz.setChangeType(ChangeTypeRepository.ADDED);
							} else {

								if ( svne.getMessage() != null &&  svne.getMessage().contains("path not found")) {
									//if (previousResision == -1l)
									if (previousResision == "")
										System.err.println("Class :"+ clazz.getPath()+ " not found in the revision: "+ clazz.getRevision());
									else
										System.err.println("Class :"+ clazz.getPath()+ " not found in the previous revision: "+ previousResision);

									System.err.println("Version :"+ changeLog.getVersion());
									System.err.println("Feature :"+ featureChangeLog);

								} else {
									svne.printStackTrace();
								}
							}

							continue; // go to next class
						}
						
					} else {
						// clazz.getChangeType().equals("D") deleted class does not need analyze changes
						// clazz.getChangeType().equals("A") doesn't have history
					}
				}
			}
		}
		return changeLogHistory;
	}

	

}
