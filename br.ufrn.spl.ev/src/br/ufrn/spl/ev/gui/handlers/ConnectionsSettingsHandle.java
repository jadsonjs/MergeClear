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

import br.ufrn.spl.ev.gui.swt.ConnectionSettingsUI;

/**
 * 
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class ConnectionsSettingsHandle implements IHandler {


	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		ConnectionSettingsUI settings = new ConnectionSettingsUI();
    	settings.configure();
		
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
