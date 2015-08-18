/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.miners;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.ast.java.JavaCompilation;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureDiffNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureDifferencer;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureNode.Type;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureTreeBuilder;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ChangeDistillerTest {

	
	public static void main(String[] args) {
		
		differenceFromCompilatesUnit();
		
		//differenceFromFiles();
	}
	
	public static void differenceFromCompilatesUnit(){
		
		JavaStructureNode fLeft;
	    JavaStructureNode fRight;
	    StructureDiffNode fDiffs;
		
	    
	    fLeft = createStructureTree("public class Foo {}");
	    fRight = createStructureTree("public class Foo { void method(int a) { a = 24; } }"); // add method
	    
	    StructureDifferencer differencer = new StructureDifferencer();
        differencer.extractDifferences(fLeft, fRight);
        fDiffs = differencer.getDifferences();
        
        System.out.println( (fDiffs.getChildren().size() ) );
        StructureDiffNode methodAddition = fDiffs.getChildren().get(0).getChildren().get(0);
       
        System.out.println( methodAddition.getDiffType()  );
        System.out.println( (methodAddition.getChildren() ) );
        System.out.println( (methodAddition.getLeft() ) );
        System.out.println( (methodAddition.getRight().getName() ) );
	  
	}
	
	
	
	public static void differenceFromFiles(){
		
	    
		File left = new File("MyClass.java_v1");
		File right = new File("MyClass.java_v2");

		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		try {
		    distiller.extractClassifiedSourceCodeChanges(left, right); 
		} catch(Exception e) {
		    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
		       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
		       attach the full stack trace along with the two files that you tried to distill. */
		    System.err.println("Warning: error while change distilling. " + e.getMessage());
		}

		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes != null) {
		    for(SourceCodeChange change : changes) {
		        System.out.println(change.getChangeType());
		        System.out.println(change.getChangedEntity().getUniqueName());
		    }
		}
	}
	
	
	
	private static JavaStructureNode createStructureTree(String source) {
        JavaCompilation compilation = CompilationUtils.compileSource(source);
        CompilationUnitDeclaration cu = compilation.getCompilationUnit();
        JavaStructureNode root = new JavaStructureNode(Type.CU, null, null, cu);
        cu.traverse(new JavaStructureTreeBuilder(root), (CompilationUnitScope) null);
        return root;
    }

	
}
