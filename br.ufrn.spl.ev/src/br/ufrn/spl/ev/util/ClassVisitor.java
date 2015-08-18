package br.ufrn.spl.ev.util;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * <p> A visit patter to CompilationUnits to the some information</p>
 *
 * <p>We can do this using "fors" too.</p>
 * 
 * @author Daniel Alencar
 * @vesion 1.0 - Class Creation.
 * @since ??/??/2013
 *
 */
public class ClassVisitor extends VoidVisitorAdapter<Object>
{
	/*This map Contains the fields and fields names. Used to see if a field was update */
	private Map<String, FieldDeclaration> fields;
	private List<MethodDeclaration> methods;

	
	public ClassVisitor()
	{
		fields = new TreeMap<String, FieldDeclaration>();
		methods = new ArrayList<MethodDeclaration>();
	}
	
	public void visit(MethodDeclaration n, Object arg)
	{
		methods.add(n);
	}
	
	public void visit(FieldDeclaration f, Object arg)
	{
		//
		// if a field have several variables create one field for each variable.
		// this when you create severals fields in a same line
		// like: int a, b, c, d;
		// in your model, we create 4 fields
		//
		for (VariableDeclarator variable : f.getVariables()) {
			fields.put(variable.getId().getName(), f);
		}
	}
	
	public Map<String, FieldDeclaration> getFields(){
		return fields;
	}
	
	public List<MethodDeclaration> getMethods(){
		return methods;
	}
	
}
