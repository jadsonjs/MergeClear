/*
 *
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 *
 */
package br.ufrn.spl.ev.gui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import br.ufrn.spl.ev.gui.swt.ShowEvolutionsUI;

/**
 * <p>Treats the commands of eclipse Menu of the Tool </p>
 *
 * @author jadson - jadson@info.ufrn.br
 *
 * @since 18/04/2013
 * @version 1.0 - Class Creation
 */
public class ExecuteEvolutionEngineHandle implements IHandler {


	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		// show the main screen of the application //
		ShowEvolutionsUI evolutionUI = new ShowEvolutionsUI();	
		evolutionUI.openUI();
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		
	}

}
