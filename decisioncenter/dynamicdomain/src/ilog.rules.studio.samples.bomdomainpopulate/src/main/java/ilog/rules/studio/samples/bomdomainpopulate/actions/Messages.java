package ilog.rules.studio.samples.bomdomainpopulate.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "ilog.rules.studio.samples.bomdomainpopulate.actions.i18n.messages"; //$NON-NLS-1$
	public static String UpdateBomDomainAction_BOM8ENTRY;
	public static String UpdateBomDomainAction_CONTRIBUTION;
	public static String UpdateBomDomainAction_PLUGIN_NAME;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
