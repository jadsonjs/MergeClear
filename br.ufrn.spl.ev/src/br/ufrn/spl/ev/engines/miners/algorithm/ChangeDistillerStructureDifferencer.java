/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.algorithm;

import japa.parser.ast.CompilationUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;

import br.ufrn.spl.ev.engines.miners.CompilationUtils;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLocation;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.ast.java.JavaCompilation;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureDiffNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureDifferencer;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureDifferencer.DiffType;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode.Type;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureTreeBuilder;

/**
 * Use the ChangeDistiller algorithm to detect differences between two version of the same class
 * 
 * 
 * https://bitbucket.org/sealuzh/tools-changedistiller/wiki/Home
 * 
 * References:
 *   Beat Fluri and Harald C. Gall. Classifying Change Types for Qualifying Change Couplings. In Proceedings of the 14th International Conference on Program Comprehension, pp. 35-45, IEEE Computer Society, 2006.
 *   Beat Fluri, Michael Würsch, Martin Pinzger, and Harald C. Gall. Change Distilling: Tree Differencing for Fine-Grained Source Code Change Extraction. IEEE Transaction on Software Engineering, 33(11), pp. 725-743, 2007.
 *   Harald C. Gall, Beat Fluri, and Martin Pinzger. Change Analysis with Evolizer and ChangeDistiller. IEEE Software, 26(1), pp. 26-33, 2009.
 *
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeDistillerStructureDifferencer implements StructureDifferencerAlgorithm{

	
	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.miners.algorithm.StructureDifferencerAlgorithm#retrieveDifferencesInClass(japa.parser.ast.CompilationUnit, japa.parser.ast.CompilationUnit, br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog, br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog, br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog)
	 */
	@Override
	public void retrieveDifferencesInClass(CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit, FeatureChangeLog featureChangeLog, ChangeLog changeLog, ClassChangeLog clazz) {
		
		StructureDiffNode sDiff = structureDiff(actualCompilationUnit, previousCompilationUnit);
		
		List<SourceCodeChange> sourceCodeChanges = fileDiff(actualCompilationUnit, previousCompilationUnit);
	    
		extractEvolutionInformation(sDiff, sourceCodeChanges, clazz);
		
	}
	
	
	private StructureDiffNode structureDiff(CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit) {
		
		JavaStructureNode fLeft = createStructureTree(previousCompilationUnit.toString());
	    JavaStructureNode fRight = createStructureTree(actualCompilationUnit.toString());
	    
	    StructureDifferencer differencer = new StructureDifferencer();
        differencer.extractDifferences(fLeft, fRight);
        StructureDiffNode fDiffs = differencer.getDifferences();
        
        printDiffNode(fDiffs);
        
        return fDiffs;
	}
	
	
	private List<SourceCodeChange> fileDiff(CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit) {
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
	
		File previousVersion = null;
		File actualVersion = null;
		
		try {
			previousVersion = getFileFromCompilationUnit(previousCompilationUnit);
			actualVersion = getFileFromCompilationUnit(actualCompilationUnit);
			
		    distiller.extractClassifiedSourceCodeChanges(previousVersion, actualVersion); 
		} catch(Exception e) {
		    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
		       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
		       attach the full stack trace along with the two files that you tried to distill. */
		    System.err.println("Warning: error while change distilling. " + e.getMessage());
		}finally{
			actualVersion.delete();
			previousVersion.delete();
		}
		
		System.out.println("/////////////Chages By File ////////////////");
		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		printDiff(changes);
		return changes;
	}
	
	
	
	private void extractEvolutionInformation(StructureDiffNode node, List<SourceCodeChange> sourceCodeChanges, ClassChangeLog clazz) {
		
		if(node == null) return;
		
		if(! node.getDiffType().equals(DiffType.NO_CHANGE) ){
			
			StructureNode actualNote = ( node.getDiffType().equals(DiffType.DELETION) ? node.getLeft() : node.getRight() );
			
			JavaStructureNode javaActualNote = (JavaStructureNode) actualNote;
			
			if(node.isFieldDiffNode() ){
				FieldChangeLog fieldChangeLog = buildField(javaActualNote, convertChageTypes( node.getDiffType()) );
				clazz.addFieldChangeLog(fieldChangeLog);
				clazz.addChangeLocation(ChangeLocation.BODY);
		    }
			
		    if( node.isMethodOrConstructorDiffNode() ){
		    	MethodChangeLog mcl = buildMethod(javaActualNote, convertChageTypes( node.getDiffType()));
		    	clazz.addMethodChangeLog(mcl);
		    	clazz.addChangeLocation(ChangeLocation.BODY);
		    }
		}
		
		// for each of the children
		if(node.hasChildren()){
	        for (StructureDiffNode n : node.getChildren()) {
	        	extractEvolutionInformation(n, sourceCodeChanges, clazz);
			}
		}
		
	}



	
	////////////////////////// auxiliary methods  ////////////////////////////////
	

	

	private void printDiff(List<SourceCodeChange> changes) {
		
		System.out.println( "\n\n=================================" );
		if(changes != null) {
		    for(SourceCodeChange change : changes) {
		    	
		    	System.out.println(">>> "+change.getChangeType()+" level of signifcance: "+change.getChangeType().getSignificance()+" body ?"+change.getChangeType().isBodyChange() );
		    	
		    	// the two main classes //
		    	 
		    	System.out.println("getRootEntit: "+change.getRootEntity());  // that describes in which attribute, class, or method the changes was made.
		    	
		    	System.out.println("getChangedEntit: "+change.getChangedEntity()); //  that describes which source code entity has changed. In case of
		   
		    	System.out.println("type: "+change.getChangedEntity().getType() );
		    	System.out.println("getUniqueName: "+change.getChangedEntity().getUniqueName());
		    	System.out.println("getModifiers: "+change.getChangedEntity().getModifiers());
		    	System.out.println("getSourceRange: "+ change.getChangedEntity().getSourceRange());
		    	
		    	if(change  instanceof Insert){
		    		Insert i = (Insert) change;
		    		System.out.println(i);
		    	}
		    }
		}
	}
	
	
	@SuppressWarnings("unused")
	private void printDiffNode(StructureDiffNode actualNode) {
		
		if(actualNode == null)
			return;
		
		System.out.println( "\n\n--------------------------------------" );
		System.out.println( " tipo "+ actualNode.getDiffType()  );
        System.out.println( " is method "+ actualNode.isMethodOrConstructorDiffNode()  );
        
        System.out.println( " is field "+ actualNode.isFieldDiffNode()  );
        
        
        if(actualNode.getRight() != null){
        	System.out.println( "\t right node "  );
        	StructureNode nodeChange = actualNode.getRight();
        	System.out.println( "\t"+nodeChange.getName()  );
        	//System.out.println( "\t"+nodeChange.getContent()  );
        	System.out.println( "\t"+nodeChange.getFullyQualifiedName()  );
        }
        if(actualNode.getLeft() != null){
        	System.out.println( "\t left node "  );
        	StructureNode nodeChange = actualNode.getLeft();
        	System.out.println( "\t"+nodeChange.getName()  );
        	//System.out.println( "\t"+nodeChange.getContent()  );
        	System.out.println( "\t"+nodeChange.getFullyQualifiedName()  );
        }
        if(actualNode.hasChildren())
        for (StructureDiffNode node : actualNode.getChildren()) {
        	printDiffNode(node);
		}
	}

	
	
	
	/**
	 * Convert the DiffType of the ChangeDistiller to our ChangeTypeRepository. They are equivalent
	 * 
	 * @param diffType
	 * @return
	 */
	private String convertChageTypes(DiffType diffType) {
		if(diffType.equals(DiffType.ADDITION) )
			return ChangeTypeRepository.ADDED;
		if(diffType.equals(DiffType.CHANGE) )
			return ChangeTypeRepository.UPDATED;
		if(diffType.equals(DiffType.DELETION) )
			return ChangeTypeRepository.DELETE;
		return null;
	}




	/**
	 * 
	 * Auxiliary method of "retrieveChangedFields" and "retrieveChangedMethods"
	 * this method is responsible to build the FieldChangeLog object
	 * 
	 * @param field
	 * @param variable
	 * @param changeType
	 * @return
	 */
	private static FieldChangeLog buildField(JavaStructureNode javaStructureNode, String changeType) {
		ASTNode node = javaStructureNode.getASTNode();
		FieldDeclaration fieldNode = (FieldDeclaration) node;
		FieldChangeLog fieldChangeLog = new FieldChangeLog(fieldNode.name.toString(), fieldNode.type + ":"+ fieldNode.name.toString(), changeType);
		return fieldChangeLog;
	}
	
	
	/**
	 * Auxiliary method of "retrieveChangedMethods" this method is responsible
	 * for building the MethodChangeLog object
	 * 
	 * @param method
	 * @param changeType
	 * @return
	 */
	private MethodChangeLog buildMethod(JavaStructureNode javaStructureNode, String changeType) {
		ASTNode node = javaStructureNode.getASTNode();
		if(node instanceof MethodDeclaration){
			MethodDeclaration methodNode = (MethodDeclaration) node;
			MethodChangeLog mcl = new MethodChangeLog(extractSimpleMethodName(javaStructureNode.getName()), methodNode.returnType+"#"+ javaStructureNode.getName(), changeType);
			return mcl;
		}else{
			if(node instanceof ConstructorDeclaration){
				//ConstructorDeclaration constructorNode = (ConstructorDeclaration) node;
				MethodChangeLog mcl = new MethodChangeLog(extractSimpleMethodName(javaStructureNode.getName()), javaStructureNode.getName(), changeType);
				return mcl;
			}
		}
		return null;
	}
	
	/** This method returns the "simple" method name used in our model. 
	 * This name is the method name without the parameter. 
	 * In other words, the name until the fists parenthesis. */
	public String extractSimpleMethodName(String methodFullName){
		return methodFullName == null ? " " : methodFullName.substring(0, methodFullName.indexOf("("));
	}
	
	
	
	/*
	 * Transfor a compilation unit to a java file that will be the input from ChangeDistiller
	 *
	 */
	private File getFileFromCompilationUnit( CompilationUnit cu ){
		File temp = null;
		FileOutputStream fos=null;
		try {
			temp = File.createTempFile("temp", ".java");
			
		    fos = new FileOutputStream(temp);
		    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fos));
		    w.append(cu.toString());
		    w.flush();
		    w.close();
		    fos.close();
		    
	    } catch (IOException e) {
			e.printStackTrace();
		}
		
		return temp;
	}
	
	
	private static JavaStructureNode createStructureTree(String source) {
        JavaCompilation compilation = CompilationUtils.compileSource(source);
        CompilationUnitDeclaration cu = compilation.getCompilationUnit();
        JavaStructureNode root = new JavaStructureNode(Type.CU, null, null, cu);
        cu.traverse(new JavaStructureTreeBuilder(root), (CompilationUnitScope) null);
        return root;
    }

}
