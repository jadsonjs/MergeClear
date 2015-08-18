package br.ufrn.spl.ev.engines.merge.asset;

import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Method representation to abstract JDT manipulation and including aditional features.
 * 
 * @author Gleydson
 *
 */
public class Method {

	private MethodDeclaration method;
	
	public Method(MethodDeclaration method) {
		this.method = method;
	}
	
	public MethodDeclaration getJDTMehod() {
		return method;
	}
	
	public String getName() {
		return method.getName().getIdentifier();
	}
	
}