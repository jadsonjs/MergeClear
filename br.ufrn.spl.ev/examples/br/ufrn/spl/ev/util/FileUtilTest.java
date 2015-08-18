/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.util;

import java.io.IOException;

/**
 * Tests of FileUtil
 *
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - Class Creation.
 * @since 22/11/2013
 *
 */
public class FileUtilTest {

	public static void main(String[] args) {
		try {
			FileUtil.replaceAFileText("C:/Users/jadson/Java/runtime-EclipseApplication/SIGAA_TARGET"+"/.project", "<name>"+"SIGAA"+"</name>", "<name>"+"SIGAA_TARGET"+"</name>");
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
