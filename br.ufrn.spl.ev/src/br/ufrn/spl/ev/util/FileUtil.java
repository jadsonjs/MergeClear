/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for work with files
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 22/11/2013
 *
 */
public class FileUtil {

	/**
	 * Replace a File text for a new one.
	 * 
	 * <p> Created at:  22/11/2013  </p>
	 *
	 * @param fileName
	 * @param word
	 * @param replaceWord
	 * @throws IOException
	 */
	public static void replaceAFileText(String fileName, String word, String replaceWord) throws IOException {
	    
	    String tempFileName = fileName+"_temp";

	    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));

	    String linha;
	    while ((linha = reader.readLine()) != null) {
	        if (linha.contains(word)) {
	            linha = linha.replace(word, replaceWord);
	        }
	        writer.write(linha + "\n");
	    }

	    writer.close();        
	    reader.close();

	    new File(fileName).delete();
	    new File(tempFileName).renameTo(new File(fileName));
	}
	
	
	
	/**
	 * Replace a File text for a new one.
	 * 
	 * <p> Created at:  22/11/2013  </p>
	 *
	 * @param fileName
	 * @param word
	 * @param replaceWord
	 * @throws IOException
	 */
	public static List<String> readFileContent(String fileName) throws IOException {
	    
		List<String> content = new ArrayList<String>();
		
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));

	    String linha;
	    while ((linha = reader.readLine()) != null) {
	    	content.add(linha);
	       
	    }
	    
	    reader.close();
	    return content;
	}
	
}
