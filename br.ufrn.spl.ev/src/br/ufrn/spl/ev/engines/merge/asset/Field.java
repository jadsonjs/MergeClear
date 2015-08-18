package br.ufrn.spl.ev.engines.merge.asset;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Field representation 
 * 
 * @author Gleydson Lima
 *
 */
public class Field {

	private FieldDeclaration field;

	private String name;

	public Field(FieldDeclaration field) {

		this.field = field;

		for (Object o : field.fragments()) {

			if (o instanceof VariableDeclarationFragment) {
				this.name = ((VariableDeclarationFragment) o).getName().toString();
			}
		}

	}

	public FieldDeclaration getField() {
		return field;
	}

	public void setField(FieldDeclaration field) {
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldDeclaration getJDTField() {
		return field;
	}

}