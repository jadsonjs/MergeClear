package br.ufrn.spl.ev.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;

/**
 * Utility class to manipulate string
 *
 * @author jadson
 * @author Gleydson
 *
 * @since 10/12/2012
 * @version 1.0 - Class Creation
 */
public class StringUtils {
	
	private static final String UNICODE =
		      "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"             // grave
		    + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD" // acute
		    + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" // circumflex
		    + "\u00C3\u00E3EeIi’ıUuYy—Ò" // tilde
		    + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF" // umlaut
		    + "\u00C5\u00E5"                                                             // ring
		    + "\u00C7\u00E7"                                                             // cedilla
		    ;
	
	private static final String PLAIN_ASCII =  
		      "AaEeIiOoUu"    // grave
		    + "AaEeIiOoUuYy"  // acute
		    + "AaEeIiOoUuYy"  // circumflex
		    + "AaEeIiOoUuYyNn"  // tilde
		    + "AaEeIiOoUuYy"  // umlaut
		    + "Aa"            // ring
		    + "Cc"            // cedilla
		    ;

	/** compare to strings removing the write spaces and the extensions of the file names */
	public static boolean equalsWithOutSpacesAndExtensions(String s1, String s2){
		s1 = s1.contains(".") ? s1.split("\\.")[0] : s1;
		s2 = s2.contains(".") ? s2.split("\\.")[0] : s2;
		return equalsWithOutSpaces(s1, s2);
	}
	
	/** compare to strings removing the write spaces */
	public static boolean equalsWithOutSpaces(String s1, String s2){
		if(s1 == null && s2 != null ) return false;
		if(s1 != null && s2 == null ) return false;
		if(s1 == null && s2 == null ) return true;
		
		return s1.replaceAll("\\s", "").equalsIgnoreCase(  s2.replaceAll("\\s", "") );
	}
	
	/** Verify if the string is empty */
	public static boolean isEmpty(String string){
		return string == null || string.trim().length() == 0;
	}
	

	/** Verify if the string is not empty */
	public static boolean isNotEmpty(String string){
		return ! isEmpty(string);
	}
	
	/**Extract the class name from the repositoryPath:  Receives: "/../../.../Class.java" Returns: "Class.java" */
	public static String extractLastNameAfterSlash(String repositoryPath) {
		if(StringUtils.isNotEmpty(repositoryPath) ){
			if(repositoryPath.lastIndexOf("/") == -1)
				return repositoryPath;
			else
				return repositoryPath.substring(repositoryPath.lastIndexOf("/")+1, repositoryPath.length());
		}
		
		return "";
	}
	
	/** Extract the analyzed project name from the repositoryPath:  
	 * 
	 *  As the project usually have the same name, appends the merge side name in the end of the project 
	 *  Receives: "/../../.../SIGAA" and Returns: "SIGAA_SOURCE" or "SIGAA_TARGET" */
	public static String extractAnalyzedProjectName(String repositoryPath, MergeSide side) {
		return extractLastNameAfterSlash(repositoryPath)+ ( side == MergeSide.SOURCE ? "_"+MergeSide.SOURCE.name() : "_"+MergeSide.TARGET.name() ); 
	}
	
	/**
	 * Create the class signature removing the string until the project name.
	 * <p> Created at:  11/10/2013  </p>
	 *
	 * @param classPath
	 * @param projectName
	 */
	public static String createClasseSignature(String classPath, String projectName) {
		if (isEmpty(classPath) || isEmpty(projectName) ) 
			return "";
		if(! classPath.contains(projectName))
			throw new IllegalArgumentException("The path of the class does not contains the project name. Impossible generated the class signature."
					+ "Project name: \""+projectName+"\""+ "classpath: "+"\""+classPath+"\"");
		
		return classPath.substring(classPath.lastIndexOf(projectName.trim())+projectName.trim().length()+1, classPath.length() );	
	}
	
	
	/** Get the numbers after the "splitText" text, returning just the revision number */
	public static String extractRevisionNumber(String notes, String splitText) {
		
		if(StringUtils.isEmpty(notes) || ! notes.contains(splitText))
			return notes;
		
		int firstIndex = notes.indexOf(splitText)+splitText.length();
		int limitIndex = notes.length();
		int lastIndex = firstIndex;
		
		for (int currentIndex = firstIndex ;  currentIndex < limitIndex ;  currentIndex++) {
			if( Character.isDigit( notes.charAt(currentIndex) ) || notes.charAt(currentIndex) == ' '  || notes.charAt(currentIndex) == '\r' || notes.charAt(currentIndex) == '\n' ){
				lastIndex = currentIndex;
			}else{
				break;
			}
		}
		return notes.substring(firstIndex, ( lastIndex < limitIndex ? lastIndex+1 : limitIndex)   ).trim();
	}
	
	
	/** Get the numbers after the "splitText" text, returning all the revisions numbers */
	public static List<String> extractRevisionsNumbers(String notes, String splitText) {
		
		List<String> revisions = new ArrayList<String>();
		
		if(StringUtils.isEmpty(notes) || ! notes.contains(splitText))
			return revisions;
		
		int firstIndex = notes.indexOf(splitText)+splitText.length();
		int limitIndex = notes.length();
		
		int startIndex = -1;
		int endIndex = -1;
		
		for (int currentIndex = firstIndex ;  currentIndex < limitIndex ;  currentIndex++) {
			if( Character.isDigit( notes.charAt(currentIndex) ) ){
				if(startIndex == -1)
					startIndex = currentIndex;
			}else{
				if(startIndex != -1){
					endIndex = currentIndex;
					revisions.add(  notes.substring( startIndex, endIndex ).trim() );
					startIndex = -1;
					endIndex = -1;
				}
			}
		}
		
		if(startIndex != -1 && Character.isDigit( notes.charAt(limitIndex-1) ) ){
			revisions.add(  notes.substring( startIndex, limitIndex ).trim() );
		}
		
		return revisions;
	}
	
	/** Verify if the data match with the tiny form of revision number:  "r12345" */
	public static boolean matchRevisionsNumbersTinyForm(String notes){
		if( isEmpty(notes) || isEmpty("[r]\\d+") ) return false;
		Pattern p1 = Pattern.compile("[r]\\d+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = p1.matcher(notes);
		return matcher.find();
	}
	
	
	/** Get the revisions numbers in the minimum format, example: "r12345".  Return 12345, this is the revision number. */
	public static List<String> extrectRevisionsNumbersTinyForm(String notes){
		
		List<String> revisions = new ArrayList<String>();
		
		String re = "[r]\\d+";
		
		Pattern p1 = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p1.matcher(notes);
		
		while (matcher.find()){
			String tempText = matcher.group();
			if(isNotEmpty(tempText)){
				tempText = matcher.group().substring(1, tempText.length() ); // remove the "r" in the beginning, returning just the revision number
				if(isNotEmpty(tempText)){
					revisions.add( tempText  );
				}
			}
				
  		}
		
		return revisions;
	}
	
	
	/** Converte a string para ascii */
	public static String toAscii(String oldString) {
		if ( oldString == null ) {
			return oldString;
		}
		StringBuffer sb = new StringBuffer();
		int n = oldString.length();
		for (int i = 0; i < n; i++) {
			char c = oldString.charAt(i);
			int pos = UNICODE.indexOf(c);
			if (pos > -1) {
				sb.append(PLAIN_ASCII.charAt(pos));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
}
