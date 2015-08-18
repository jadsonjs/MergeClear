/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.sigproject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.util.StringUtils;

/**
 * Class to deal with IPROJECT Logs
 *
 * @author daniel
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class criation.
 * @since 09/08/2013
 *
 */
public class SIGProjectLogUtils {

	
	/***
	 * <p>Try to extract the classes had changed from  of IPROJECT UPDATE LOGS  pattern:</p> 
	 * 
	 * <pre>
	 * Revisão 149778 por fulano de tal
	 *  
	 * A trunk/Sistema/src/br/ufrn/sistema/DAO.java 
	 * U trunk/Sistema/src/br/ufrn/sistema/MBean.java 
	 * D trunk/Sistema/src/br/ufrn/sistema/Processador.java
	 * </pre>
	 * 
	 * <p> Created at:  09/08/2013  </p>
	 *
	 * @param log
	 * @return
	 */
	public static List<ClassChangeLog> extractClassesFromLog(String log, String projectName){
		
		List<ClassChangeLog> classesChangeLogs = new ArrayList<ClassChangeLog>();
		
		if(StringUtils.isEmpty(log)) return classesChangeLogs;
		
		String re1 = "([AUD])"; // Single Character 1
		String re2 = "(\\s+)"; // White Space 1
		String re3 = "(trunk)"; // Word 1
		String re4 = "((?:\\/[\\w\\.\\-]+)+)"; // Unix Path 1

		String re5 = "(\\s)";
		String re6 = "(\\d+)";
		String re7 = "(:)";
		String re8 = "(branches)";
		String re9 = "(trunk2)";

		// ([AUD])(\s+)(trunk)((?:\/[\w\.\-]+)+)
		Pattern p1 = Pattern.compile(re1 + re2 + re3 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		// (\\s)(\\d+)(\\s)
		Pattern p2 = Pattern.compile(re5 + re6 + re5, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		// (:)(\\d+)(\\s)
		Pattern p3 = Pattern.compile(re7 + re6 + re5);
		// ([AUD])(\s+)(branches)((?:\/[\w\.\-]+)+)
		Pattern p4 = Pattern.compile(re1 + re2 + re8 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		// ([AUD])(\s+)(trunk2)((?:\/[\w\.\-]+)+)
		Pattern p5 = Pattern.compile(re1 + re2 + re9 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		// (\\s)(\\d+)
		Pattern p6 = Pattern.compile(re5 + re6);

		long revision = 0;
		
		String[] logParts = log.split("Revisao");
		// getting the revisions

		for (int i = 0; i < logParts.length; i++)
		{
			Matcher revMatcher = p2.matcher(logParts[i]);

			// (begin) get the revision
			if (revMatcher.find())
			{
				revision = Long.valueOf(revMatcher.group(2));
			} else
			{
				revMatcher = p3.matcher(logParts[i]);
				if (revMatcher.find())
				{
					revision = Long.valueOf(revMatcher.group(2));
				} else
				{
					revMatcher = p6.matcher(logParts[i]);
					if (revMatcher.find())
					{
						revision = Long.valueOf(revMatcher.group(2));
					}
				}
			}
			// (end)

			// (begin) get the changed paths/files
			boolean found = false;
			Matcher assetMatcher = p1.matcher(logParts[i]);
			found = retrievesClassFromLog(assetMatcher, classesChangeLogs, revision, projectName);

			if (!found)
			{
				assetMatcher = p4.matcher(logParts[i]);
				found = retrievesClassFromLog(assetMatcher, classesChangeLogs, revision, projectName);
				if (!found)
				{
					assetMatcher = p5.matcher(logParts[i]);
					retrievesClassFromLog(assetMatcher, classesChangeLogs, revision, projectName);
				}
			}
			// (end)
		}
		
		return classesChangeLogs;
	}
	
	/**
	 *  
	 * <p> Created at:  09/08/2013  </p>
	 *
	 * @param matcher
	 * @param changes
	 * @param revision
	 * @return
	 */
	private static boolean retrievesClassFromLog(Matcher matcher, List<ClassChangeLog> changes, long revision, String projectName){
		
		boolean found = false;
		
		while (matcher.find()){
			found = true;
			String c1 = matcher.group(1);
			String trunk = matcher.group(3);
			String path = matcher.group(4);
			if (path.contains(".java")){
				String className = path.substring(path.lastIndexOf("/")+1, path.length()); // take just the class name: "XXXX.java"
				
				String signature = StringUtils.createClasseSignature(path, projectName);
				
				changes.add( new ClassChangeLog(className, trunk + path, signature, c1, String.valueOf(revision) ));
			} else{
				continue;
			}
		}
		return found;
	}
	
}
