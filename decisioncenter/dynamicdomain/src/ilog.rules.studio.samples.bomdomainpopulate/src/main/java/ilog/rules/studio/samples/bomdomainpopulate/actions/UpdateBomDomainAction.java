/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2022. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate.actions;

import ilog.rules.bom.IlrObjectModel;
import ilog.rules.studio.model.IlrElement;
import ilog.rules.studio.model.IlrStudioModelPlugin;
import ilog.rules.studio.model.bom.IlrBOM;
import ilog.rules.studio.model.bom.IlrBOMEntry;
import ilog.rules.studio.model.resource.IlrResourceElement;
import ilog.rules.util.IlrVisitor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Empty implementation of action, showing the API used to access the BOM entry 
 * that is encapsulated within the RDT model 
 */
public class UpdateBomDomainAction implements IObjectActionDelegate {

	private IlrBOMEntry bomentry;


	/**
	 * This implementation does nothing
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * runs the action, and displays a message box with native information
	 * on the current object model
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Shell shell = new Shell();
		IlrBOM bom = bomentry.getBOM();
		IlrObjectModel objectmodel = bom.getObjectModel();
		objectmodel.accept(new IlrVisitor() {
		
			public void visit(Object o) {
				// visit the BOM as needed
			}
		
		});
		//TODO: really do something here
		MessageDialog.openInformation(
				shell,
				Messages.UpdateBomDomainAction_PLUGIN_NAME,
				Messages.UpdateBomDomainAction_CONTRIBUTION + bomentry.getDisplayName() + Messages.UpdateBomDomainAction_BOM8ENTRY);

		
	}

	/**
	 * updates the BOM entry attribute when the selection changes
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	    if (selection instanceof IStructuredSelection) {
	        Object obj = ((IStructuredSelection)selection).getFirstElement();
	        IResource rscElement = null;
	        if (obj instanceof IResource) {
	        	rscElement = (IResource) obj;
	        }
	        else if (obj instanceof IlrResourceElement) {
	        	IlrResourceElement ilogRscElement = (IlrResourceElement) obj;
	        	rscElement = ilogRscElement.getResource();
	        }
	        else if (obj instanceof IAdaptable) {
	        	IAdaptable adaptable = (IAdaptable) obj;
	        	rscElement = (IResource) adaptable.getAdapter(IResource.class);
	        }
        	// Retrieve the IlrElement attached to this resource, thanks to the IlrResourceManager
        	IlrElement elt = IlrStudioModelPlugin.getResourceManager().getElementFromResource(rscElement);
        	if (elt instanceof IlrBOM) {
        		bomentry = ((IlrBOM)elt).getBOMEntry();
        	}
        
	    }

	}
}
