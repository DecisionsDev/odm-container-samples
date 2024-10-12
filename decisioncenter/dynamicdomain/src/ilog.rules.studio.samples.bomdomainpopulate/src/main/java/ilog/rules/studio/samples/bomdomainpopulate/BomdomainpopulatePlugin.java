/*
* Licensed Materials - Property of IBM
* 5725-B69 5655-W88 5655-Y31 5724-X98 5724-Y15 5655-V82 
* Copyright IBM Corp. 1987, 2022. All Rights Reserved.
*
* Note to U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package ilog.rules.studio.samples.bomdomainpopulate;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class BomdomainpopulatePlugin extends AbstractUIPlugin {
	//The shared instance.
	private static BomdomainpopulatePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;

     /**
	 *  Initialize the resource bundle in the constructor
	 */
	public BomdomainpopulatePlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("ilog.rules.studio.samples.bomdomainpopulate.BomdomainpopulatePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}


	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static BomdomainpopulatePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = BomdomainpopulatePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	  /**
	   * Logs the given throwable exception with no specific message.
	   */
	  public static void log(Throwable throwable) {
	  	log(throwable, null);
	  }
	  /**
	   * Logs the given throwable exception.
	   */
	  public static void log(Throwable throwable, String msg) {
	  	String reportMsg = throwable.getMessage() != null ? throwable.getMessage() : "";
	  	if (msg != null)
	  		reportMsg = msg + "\n" + reportMsg; 
	  	log(new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(), IStatus.ERROR, reportMsg, throwable));
	  }
	  
	  /**
	   * Logs a message from the specified <code>status</code>.
	   */
	  public static void log(IStatus status) {
	    if (plugin != null) {
			plugin.getLog().log(status);
		}
	  }
}
