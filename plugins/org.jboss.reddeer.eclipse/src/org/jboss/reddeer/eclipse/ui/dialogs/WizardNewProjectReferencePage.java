package org.jboss.reddeer.eclipse.ui.dialogs;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Second page of New General Project wizard
 * 
 * @author vlado pakan
 * 
 */
public class WizardNewProjectReferencePage extends WizardPage {
	
	private final Logger log = Logger
			.getLogger(WizardNewProjectReferencePage.class);

	/**
	 * Sets a given project references.
	 * 
	 * @param referencedProjects Project references
	 */
	public void setProjectReferences(String... referencedProjects) {
		log.debug("Set Project references to: ");
		DefaultTable tbProjectReferences = new DefaultTable();
		for (String tableItemLabel : referencedProjects) {
			log.debug(tableItemLabel);
			TableItem tiReferencedProject = tbProjectReferences
					.getItem(tableItemLabel);
			tiReferencedProject.setChecked(true);
		}
	}
}
