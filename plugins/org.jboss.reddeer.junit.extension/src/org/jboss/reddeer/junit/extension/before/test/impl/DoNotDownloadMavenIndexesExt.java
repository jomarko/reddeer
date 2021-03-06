package org.jboss.reddeer.junit.extension.before.test.impl;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.properties.RedDeerProperties;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenPreferencePage;
import org.jboss.reddeer.junit.extensionpoint.IBeforeTest;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/** 
 * Extension for Extension point org.jboss.reddeer.junit.before.test. Disables
 * Maven Repo Index downloading on startup Use this system property to
 * enable/disable it:
 *
 * - reddeer.disable.maven.download.repo.index.on.startup=[true|false]
 * (default=true)
 * 
 * @author vlado pakan
 *
 */
public class DoNotDownloadMavenIndexesExt implements IBeforeTest {

	private static final Logger log = Logger
			.getLogger(DoNotDownloadMavenIndexesExt.class);

	private static final boolean DISABLE_MAVEN_DOWNLOAD_REPO_INDEX = RedDeerProperties.DISABLE_MAVEN_REPOSITORY_DOWNLOAD.getBooleanValue();

	private boolean tryToDisableDownloadingRepoIndexes = true;

	/** 
	 * See {@link IBeforeTest}.
	 */
	@Override
	public void runBeforeTest() {
		if (hasToRun()) {
			disableMavenDownloadRepoIndexOnStartup();
		}
	}

	/** 
	 * Disables downloading Maven repo indexes on startup.
	 */
	private void disableMavenDownloadRepoIndexOnStartup() {
		if (Platform
				.getPreferencesService()
				.getString("org.eclipse.m2e.core", "eclipse.m2.updateIndexes",
						"true", null).equalsIgnoreCase("true")) {
			log.debug("Trying to disable dowlading maven repo indexes on startup "
					+ "via Windows > Preferences > Maven");
			try {
				WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
				MavenPreferencePage mavenPreferencePage = new MavenPreferencePage();

				preferencesDialog.open();
				preferencesDialog.select(mavenPreferencePage);

				mavenPreferencePage.setDownloadRepoIndexOnStartup(false);
				preferencesDialog.ok();
				tryToDisableDownloadingRepoIndexes = false;
				log.debug("Dowlading maven repo indexes on startup disabled");
			} catch (CoreLayerException swtle) {
				log.warn(
						"Error when trying to disable dowlading maven repo indexes on startup",
						swtle);
				// try to close preferences shell in case it stayes open
				try {
					new DefaultShell("Preferences").close();

				} catch (RedDeerException swtle1) {
					// do nothing
				}
			}

		} else {
			tryToDisableDownloadingRepoIndexes = false;
		}
	}

	/** 
	 * See {@link IBeforeTest}.
	 * @return boolean
	 */
	@Override
	public boolean hasToRun() {
		return DoNotDownloadMavenIndexesExt.DISABLE_MAVEN_DOWNLOAD_REPO_INDEX
				&& tryToDisableDownloadingRepoIndexes;
	}

}
