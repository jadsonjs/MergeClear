/**
 * 
 */
package br.ufrn.spl.ev.gui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The factory of evolution Perspective
 * 
 * @author jadson - jadsonjs@gmail.com
 *
 */
public class EvolutionPerspectiveFactory implements IPerspectiveFactory {

	/** Define the layout of the perspective */
	public void createInitialLayout(IPageLayout layout) {
        layout.createFolder("left", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);;
        layout.createFolder("right", IPageLayout.RIGHT, 0.6f, IPageLayout.ID_EDITOR_AREA);;
        layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, IPageLayout.ID_EDITOR_AREA);;
        layout.createFolder("top", IPageLayout.TOP, 0.6f, IPageLayout.ID_EDITOR_AREA);;
    }
}
