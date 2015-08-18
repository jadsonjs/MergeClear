package br.ufrn.spl.ev.engines.merge.ast;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import br.ufrn.spl.ev.engines.merge.asset.Field;
import br.ufrn.spl.ev.engines.merge.asset.Method;
import br.ufrn.spl.ev.engines.merge.asset.Type;
import br.ufrn.spl.ev.engines.merge.engine.Repository;
import br.ufrn.spl.ev.engines.merge.engine.exceptions.MergeRuntimeException;

/**
 * Compilation Unit with support of merge operations.
 * 
 * @author Gleydson Lima
 * 
 */
public class MergeCompilationUnit implements MergeOperations {

	private Type classe;

	private CompilationUnit cUnit;

	private ASTParser parser = ASTParser.newParser(AST.JLS4);

	/** Objetos de AST e Manipulação da AST */
	private AST ast;

	private ASTRewrite rewriter;

	private ListRewrite listDeclarations;

	private boolean initModifications = false;

	public MergeCompilationUnit(IType type) throws Exception {
		init(type);
	}

	public void init(IType type) throws Exception {

		classe = new Type(type);

		parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(classe.getJDTType().getCompilationUnit());
		parse();

	}

	/**
	 * Parse the AST Nodes contruting a Merge Model of the assets
	 * 
	 * @throws Exception
	 */
	public void parse() throws Exception {

		cUnit = (CompilationUnit) parser.createAST(null);

		cUnit.accept(new ASTVisitor() {

			public boolean visit(TypeDeclaration node) {

				classe.addMethods(node.getMethods());
				classe.addFields(node.getFields());
				classe.setTypeDeclaration(node);
				return true;
			}

			public boolean visit(ImportDeclaration node) {

				classe.addImport(node);
				return true;
			}

		});

	}

	private void initModifications() {

		if (!initModifications) {

			try {
				reload();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (ast == null) {
				ast = cUnit.getAST();
				rewriter = ASTRewrite.create(ast);

				if (classe.getLastMethod() != null)
					listDeclarations = rewriter.getListRewrite(classe.getTypeDeclaration(),
							TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

			}

			initModifications = true;
		}
	}

	public Method getMethod(String name) {
		return classe.getMethod(name);
	}

	public Field getField(String name) {
		return classe.getField(name);
	}

	public Method getLastMethod() {
		return classe.getLastMethod();
	}

	@Override
	public void addMethod(Method method) {

		try {

			initModifications();
			listDeclarations.insertLast(method.getJDTMehod(), null);
			System.out.println("Adding method " + method.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	@Override
	public void addField(Field field) {

		try {
			initModifications();

			listDeclarations.insertLast(field.getJDTField(), null);
			System.out.println("Adding field " + field.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	@Override
	public void removeMethod(Method method) {

		try {

			initModifications();
			listDeclarations.remove(method.getJDTMehod(), null);
			System.out.println("Removing method " + method.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	@Override
	public void removeField(Field field) {

		try {
			
			if ( field == null ) {
				throw new MergeRuntimeException("You are tring to remove a null field");
			}

			initModifications();
			listDeclarations.remove(field.getJDTField(), null);
			System.out.println("Removing field " + field.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	@Override
	public void updateField(Field fieldTarget) {

		try {

			initModifications();

			Field actualField = classe.getField(fieldTarget.getName());
			rewriter.replace(actualField.getJDTField(), fieldTarget.getJDTField(), null);

			System.out.println("Updating field " + fieldTarget.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	@Override
	public void updateMethod(Method methodTarget) {

		try {

			initModifications();

			Method metodoLocal = classe.getMethod(methodTarget.getName());
			if (metodoLocal == null)
				throw new RuntimeException("Method " + methodTarget.getName() + " not found in target");

			rewriter.replace(metodoLocal.getJDTMehod(), methodTarget.getJDTMehod(), null);

			System.out.println("Updating method " + methodTarget.getName() + " in " + classe.getFullName());

		} catch (RuntimeException rte) {
			throw new MergeRuntimeException(rte);
		}
	}

	/**
	 * Apply changes in AST
	 * 
	 * @return
	 * @throws JavaModelException
	 * @throws IllegalArgumentException
	 * @throws MalformedTreeException
	 * @throws BadLocationException
	 */
	public String applyChanges() throws Exception {

		if (initModifications) {

			TextEdit edits = rewriter.rewriteAST();

			ICompilationUnit unit = classe.getJDTType().getCompilationUnit();

			// apply the text edits to the compilation unit
			Document document = new Document(unit.getSource());

			edits.apply(document);

			// this is the code for adding statements
			unit.getBuffer().setContents(document.get());

			unit.save(null, true);

			return document.get();

		} else {
			System.out.println("WARN: No modification was done in this MergeCompilationUnit");
			return null;
		}
	}

	private void reload() throws Exception {

		IType type = Repository.findClassInTarget(classe.getFullName());
		init(type);

	}

	public AST getAST() {
		return cUnit.getAST();
	}

	public CompilationUnit getCUnit() {
		return cUnit;
	}

	public void setcUnit(CompilationUnit cUnit) {
		this.cUnit = cUnit;
	}

	public ASTParser getParser() {
		return parser;
	}

	public void setParser(ASTParser parser) {
		this.parser = parser;
	}

}
