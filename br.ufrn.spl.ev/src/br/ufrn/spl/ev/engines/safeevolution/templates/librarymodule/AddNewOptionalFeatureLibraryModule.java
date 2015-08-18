/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORM�TICA E MATEM�TICA APLICADA - DIMAP
 * Programa de P�s-Gradua��o em Sistemas e Computa��o - PPGSC
 */
package br.ufrn.spl.ev.engines.safeevolution.templates.librarymodule;

import japa.parser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.safeevolution.templates.AddNewOptionalFeature;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.util.GUIUtils;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * <p>This class try to detect the <strong>AddNewOptionalFeature template </strong> for the UFRN LPS</p>
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class AddNewOptionalFeatureLibraryModule extends AddNewOptionalFeature{

	
	public AddNewOptionalFeatureLibraryModule(RepositoryConnector repositoryConnector,  String workDirectory){
		super(repositoryConnector, workDirectory);
	}
	
	/**
	 * @see br.ufrn.spl.ev.engines.safeevolution.TemplateAnalyzerChain#doAnalysis()
	 */
	@Override
	protected void doAnalysis(ChangeLogHistory changeLogHistory) {
		
		final String CK_CLASS = "ParametrosBiblioteca.java";
		
		Map<String, Map<String, Set<String> >>  informationCollected = new HashMap<String, Map<String, Set<String> >>();
		
		/*
		 * The Fields change on ParametosBibliotecas.java class
		 * 
		 * With these fields we check if some asset are include just if this parameter is selected.
		 */
		List<FieldChangeLog> libraryParameterFields = new ArrayList<FieldChangeLog>();
		
		try {
			
			boolean useConditionalExecution = false;
			
			for (FeatureChangeLog feature : changeLogHistory.getFeatures()) {
				
				//int actualChangeLog = 1;
				//int qtdChangeLogs = feature.getChangelogs().size();
				for (ChangeLog changeLog : feature.getChangelogs()) {
					
					//System.out.println("\t"+changeLog.getIdentify()+"-"+changeLog.getDescription()+" ("+actualChangeLog+" of "+qtdChangeLogs+")");
					
					forClass:
					for (ClassChangeLog _class : changeLog.getClasses()) {
						if(_class.getName().equals(CK_CLASS) && ( _class.isAdded() || _class.isUpdated() ) ){
							useConditionalExecution = true;
							
							
							libraryParameterFields.addAll( _class.getFields() ) ;
							
							break forClass;
						}
					}
					
					/*
					 * If the change log is using the ParametrosBiblioteca.java (the file that represent our CK )
					 * 
					 * Try to make sure the all asset are call just if the feature is selected
					 */
					if( useConditionalExecution ){
						
						//int actualClass = 1;
						//int qtdClass = changeLog.getClasses().size();
						// FOR ALL classes 
						for (ClassChangeLog _class : changeLog.getClasses()) {
							//System.out.println("\t\t"+_class.getPath()+" ("+actualClass+" of "+qtdClass+")");
							
							String classSouceCode = getClassSoruceCode(_class);
							
							// FOR ALL changed parameters
							for (FieldChangeLog conditionalExecutionParameter : libraryParameterFields ) {
							
								if(identifyConditionalExecution(classSouceCode, conditionalExecutionParameter.getName()) ){
									
									addInformationCollected(informationCollected, feature.getName(), changeLog.getIdentify()+" - " + changeLog.getDescription(), _class.getPath());
									
								}
							}
							//actualClass++;
						}
						
						
					}
					
					useConditionalExecution = false;
					
					//actualChangeLog++;
				}
				
				
			
			}
			
			saveTemplateStatistics(template, informationCollected);
			
		} catch (Exception e) {
			e.printStackTrace();
			GUIUtils.addErrorMessage("Error processing Add new Optin Feature ");
		}
		

	}


	/**
	 * Here is the intelligence of this template 
	 * 
	 * Try to identify patters where code is execute just if the parameter if true, in other words, if the feature is selected. 
	 * 
	 * @param classSouceCode
	 * @param conditionalExecutionParameter
	 * @return
	 */
	private boolean identifyConditionalExecution(String classSouceCode, String conditionalExecutionParameter) {
		
		if(StringUtils.isEmpty(classSouceCode) || StringUtils.isEmpty(conditionalExecutionParameter)  ) return false;
		
		if(  ! classSouceCode.contains(conditionalExecutionParameter)  ) return false;
		
		// when we have straightforward if (parameter)
		if( identifyDirectConditionalExecution(classSouceCode, conditionalExecutionParameter) ){
			return true;
		}
		
		// when we have a indirect conditional execution
		// variable = parameter
		// if (variable)
		if(  identifyConditionalExecutionAssignedToVariable(classSouceCode, conditionalExecutionParameter) ){
			
			String variable = getConditionalExecutionVariable(classSouceCode, conditionalExecutionParameter);  // EX.: Class CadastrousuarioBibliotecaMBean.java
			
			if( identifyDirectConditionalExecution(classSouceCode, variable) ){
				return true;
			}
		}
		
		/* when we have a indirect conditional execution
		 * if (isParameter())
		 * 
		 * boolean isParameter(){ return parameter;}
		 */
		
		if(  identifyConditionalExecutionInsideMethod(classSouceCode, conditionalExecutionParameter) ){
			
			String method = getConditionalExecutionMethod(classSouceCode, conditionalExecutionParameter);  // EX.: Class ProcessadorSenhaUsuarioBiblioteca.java
			
			if( identifyDirectConditionalExecution(classSouceCode, method) ){
				return true;
			}
			
		}
		
		return false;
	}

    
	/**
	 * Identify if we have a conditional if that test the parameter if true.
	 * 
	 * The code of the if will be execute just if the parameter is true (a new feature optional)
	 * 
	 * @param classSouceCode
	 * @param conditionalExecutionParameter
	 * @return
	 */
	public boolean identifyDirectConditionalExecution(String classSouceCode, String conditionalExecutionParameter) {
		
		// any character + 'if' + any white space+ '(' + any character + parameter + any character + ')' + any character //
		String pattern = "(.*)(if)(\\s*)[\\(](.*)("+conditionalExecutionParameter+")(.*)[\\)](.*)";
		
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE );
		Matcher matcher = p.matcher(classSouceCode);
		if( matcher.find() ){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Identify if we have a parameter assigned to a variable
	 * @param classSouceCode
	 * @param conditionalExecutionParameter
	 * @return
	 */
	public boolean identifyConditionalExecutionAssignedToVariable(String classSouceCode, String conditionalExecutionParameter) {
		
		// any character + '=' + any character + parameter + any character + ';' //
		String pattern = "(.*)(\\s*)(=)(.*)("+conditionalExecutionParameter+")";
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL  );
		Matcher matcher = p.matcher(classSouceCode);
		if( matcher.find() ){
			return true;
		}else{
			return false;
		}
	}
	

	/**
	 * Return the name of the variable to which the parameter was assigned.
	 */
	public String getConditionalExecutionVariable(String classSouceCode, String conditionalExecutionParameter) {
		// any character + '=' + any character + parameter + any character + ';' //
		String pattern = "(.*)(\\s*)(=)(.*)("+conditionalExecutionParameter+")";
		
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL  );
		Matcher matcher = p.matcher(classSouceCode);
		
		while (matcher.find()) {
		    String temp = matcher.group();
		    int indexSearch = temp.lastIndexOf("=");
		    boolean started = false;
		    StringBuilder variable = new StringBuilder();
		    
		    for (int index = indexSearch-1; index > 0; index--) {
		    	if( temp.charAt(index) != ' '){
		    		variable.append( temp.charAt(index) );
		    		started = true;
		    	}
		    	if( temp.charAt(index) == ' ' && started)
		    		break; // end the variable
				
			}
		    return variable.reverse().toString();
		}
		
		return "";
	}
	
	
	
	/**
	 * Identify a use of parameter inside a method, like the example:
	 * 
	 * <pre>
	 * private boolean isFeatureOptionalActive(){
	 *     return parameterHelper.getParameter(FeatureOptional) == true;
	 * }
	 * </pre>
	 * 
	 * @param classSouceCode
	 * @param conditionalExecutionParameter
	 * @return
	 */
	public boolean identifyConditionalExecutionInsideMethod(String classSouceCode, String conditionalExecutionParameter) {
		// any character + 'boolean' + any white space+ any no white space + '(' + any character + ')' + '{' + any character + parameter + any character + '}'  //
		String pattern = "(.*)(boolean)(\\s*)(\\w{1,40})[\\(](.*)[\\)](.*)[\\{](.*)("+conditionalExecutionParameter+")(.*)[\\}]";
		
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
		Matcher matcher = p.matcher(classSouceCode);
		if( matcher.find() ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Return the name of the method that test the parameter.
	 */
	public String getConditionalExecutionMethod(String classSouceCode, String conditionalExecutionParameter) {
		// any character + 'boolean' + any white space+ any no white space + '(' + any character + ')' + '{' + any character + parameter + any character + '}'  //
		String pattern = "(.*)(boolean)(\\s*)(\\w{1,40})[\\(](.*)[\\)](.*)[\\{](.*)("+conditionalExecutionParameter+")(.*)[\\}]";
		
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL  );
		Matcher matcher = p.matcher(classSouceCode);
		
		while (matcher.find()) {
		    String temp = matcher.group();
		    
		    int indexSearch = temp.lastIndexOf("{");
		    boolean started = false;
		    boolean passedByBracket = false;
		    StringBuilder variable = new StringBuilder();
		    
		    for (int index = indexSearch-1; index > 0; index--) {
		    	if( temp.charAt(index) == '(')
		    		passedByBracket = true;
		    	if( temp.charAt(index) != ' '){
		    		variable.append( temp.charAt(index) );
		    		started = true;
		    	}
		    	if( temp.charAt(index) == ' ' && started && passedByBracket)
		    		break; // end the variable
				
			}
		    return variable.reverse().toString().replaceAll("[\\(](.*)[\\)]", "").trim();
		}
		
		return "";
	}
	

	
	/**
	 * Return the source code of the class in the revision on the repository
	 * 
	 * @param _class
	 * @return
	 * @throws Exception
	 */
	private String getClassSoruceCode(ClassChangeLog _class) {
		try {
			//Mudei aqui Long String
			CompilationUnit c = repositoryConnector.getClassContentInTheRevision(_class.getPath(), _class.getRevision());
			return c.toString();
		} catch (Exception e) {
			// do nothing
		}
		return "";
	}

}
