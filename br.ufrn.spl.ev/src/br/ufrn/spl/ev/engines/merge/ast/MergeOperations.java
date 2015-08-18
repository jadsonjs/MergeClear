package br.ufrn.spl.ev.engines.merge.ast;

import br.ufrn.spl.ev.engines.merge.asset.Field;
import br.ufrn.spl.ev.engines.merge.asset.Method;


/**
 * Interface de operações de merge que operam sobre uma unidade de compilação.
 * 
 * @author Gleydson Lima
 *
 */
public interface MergeOperations {

	public void addField(Field field);
	
	public void updateField( Field fieldTarget );
	
	public void removeField(Field field);
	
	public void addMethod(Method method);
	
	public void updateMethod(Method methodTarget );
	
	public void removeMethod(Method method);
	
}
