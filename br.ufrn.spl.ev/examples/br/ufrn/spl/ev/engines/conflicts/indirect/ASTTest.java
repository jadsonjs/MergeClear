/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 *  http://www.vogella.com/articles/EclipseJDT/article.html
 *  Lars Vogel
 *  Version 1.6
 *  Copyright © 2009, 2010, 2011, 2012 Lars Vogel
 *  08.08.2012
 *
 */
public class ASTTest {

	public void analyseMethods(IProject project) throws JavaModelException {
	    
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
	    
	    // parse(JavaCore.create(project));
	    for (IPackageFragment mypackage : packages) {
	      if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
	        createAST(mypackage);
	      }
	    }
	  }

	 private void createAST(IPackageFragment mypackage) throws JavaModelException {
	    
		 for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
	     
	      // Now create the AST for the ICompilationUnits (Usually a class of the package)
	      ASTNode parse = parse(unit);
	      
	     // printASTNode(parse);
	      
	      // and visit the methods using this AST, in my sense similar of JDT, but here we have the source code whole information
	      MethodVisitor visitor = new MethodVisitor();
	      parse.accept(visitor);
	      
	      FieldVisitor visitor2 = new FieldVisitor();
	      parse.accept(visitor2);
	      
	      
	      for (MethodDeclaration method : visitor.getMethods()) {
	        System.out.println(" Method name: " + method.getName()  + " Return type: " + method.getReturnType2());
	      }
	      
	      for (FieldDeclaration field : visitor2.getFields()) {
	    	  Object o = field.fragments().get(0);
			  if(o instanceof VariableDeclarationFragment){
				  System.out.println(" Field name: " + ((VariableDeclarationFragment) o).getName() + " Type: " + field.getType());
			  }
		        
		  }

	    }
	}

	  
	/** 
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the 
	 * Java source file 
	 * 
	 * Create the whole source code of the CompilationUnit, including the comments
	 * 
	 * @param unit  
	 * @return 
	 */
	private static ASTNode parse(ICompilationUnit unit) {
	    ASTParser parser = ASTParser.newParser(AST.JLS4);
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    parser.setSource(unit);
	    parser.setResolveBindings(true);
	    return (ASTNode) parser.createAST(null); // parse
	  }
	
	
	
	private void printASTNode(ASTNode node) {
	    List properties= node.structuralPropertiesForType();

	    for (Iterator iterator= properties.iterator(); iterator.hasNext();) {
	        Object desciptor= iterator.next();

	        if (desciptor instanceof SimplePropertyDescriptor) {
	            SimplePropertyDescriptor simple= (SimplePropertyDescriptor)desciptor;
	            Object value= node.getStructuralProperty(simple);
	            if(simple!= null)
	            	System.out.println(simple.getId() + " (" + ( value != null ? value.toString() : "no value")+ ")");
	        } else if (desciptor instanceof ChildPropertyDescriptor) {
	            ChildPropertyDescriptor child= (ChildPropertyDescriptor)desciptor;
	            ASTNode childNode= (ASTNode)node.getStructuralProperty(child);
	            if (childNode != null) {
	                System.out.println("Child (" + child.getId() + ") {");
	                printASTNode(childNode);
	                System.out.println("}");
	             }
	        } else {
	             ChildListPropertyDescriptor list= (ChildListPropertyDescriptor)desciptor;
	             System.out.println("List (" + list.getId() + "){");
	             printASTNodes((List)node.getStructuralProperty(list));
	             System.out.println("}");
	        }
	    }
	}
	
	private void printASTNodes(List nodes) {
	    for (Iterator iterator= nodes.iterator(); iterator.hasNext();) {
	        ASTNode node= (ASTNode)iterator.next();
	        printASTNode(node);
	    }
	}


}


class MethodVisitor extends ASTVisitor {
	  List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

	  @Override
	  public boolean visit(MethodDeclaration node) {
	    methods.add(node);
	    return super.visit(node);
	  }

	  public List<MethodDeclaration> getMethods() {
	    return methods;
	  }
} 


class FieldVisitor extends ASTVisitor {
	  List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

	  @Override
	  public boolean visit(FieldDeclaration node) {
		  fields.add(node);
		  return super.visit(node);
	  }

	  public List<FieldDeclaration> getFields() {
	    return fields;
	  }
} 



