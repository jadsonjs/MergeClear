/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.connectors;

import japa.parser.ast.CompilationUnit;

import java.util.List;

/**
 * Interface to be implemented by connector of repository, like SVN connector or GIT connector.
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface RepositoryConnector {
	
	
	/**
	 * 	'Checkout" the content of a class in revision. In other words, the content that the class had in the revision.
	 */
	public abstract CompilationUnit getClassContentInTheRevision(String classPath, String revisionNumber) throws Exception;
	
	/** 
	 * Abstract methods to be implement that return the files of a revision using the 
	 * specific repository, like SVN, GIT, etc..
	 * 
	 */
	public abstract List<String> getFilesOfRevision(String Revision) throws Exception;
	
	

	/**Get the previous revision of a file in the repository 
	 * The indicated change here is to switch SVNRevision for String, but as this method is called elsewhere, this change is not minimum
	 * public long getPreviousRevision(String path, String actualRevision) throws Exception; */
	public String getPreviousRevision(String path, String actualRevision) throws Exception;
	
	//public String getPreviousRevisionGit(String path, String actualRevision) throws Exception;
	
	/** Had to create an interface to be able to return both SVN and Git AnalisisExecutor */
	//public abstract AnalisisExecutor getRespositoryAnalizer(RepositoryConnector repositoryConnector);

	public abstract void performsRun();

	public abstract String getRepositoryPath();
	
}
