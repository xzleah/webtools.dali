/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.db;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.datatools.enablement.jdt.classpath.DriverClasspathContainer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jpt.jpa.db.internal.DTPConnectionProfileFactory;
import org.osgi.framework.BundleContext;

/**
 * The JPT DB plug-in lifecycle implementation.
 * Globally available connection profile factory.
 * <p>
 * Provisional API: This class is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public class JptJpaDbPlugin extends Plugin {
	private DTPConnectionProfileFactory connectionProfileFactory;

	private static JptJpaDbPlugin INSTANCE;  // sorta-final

	/**
	 * Return the singleton JPT DB plug-in.
	 */
	public static JptJpaDbPlugin instance() {
		return INSTANCE;
	}

	// ********** public static methods **********

	public static ConnectionProfileFactory getConnectionProfileFactory() {
		return INSTANCE.getConnectionProfileFactory_();
	}

	// ********** plug-in implementation **********

	/**
	 * The constructor
	 */
	public JptJpaDbPlugin() {
		super();
		if (INSTANCE != null) {
			throw new IllegalStateException();
		}
		// this convention is *wack*...  ~bjv
		INSTANCE = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.connectionProfileFactory != null) {
			this.connectionProfileFactory.stop();
			this.connectionProfileFactory = null;
		}
		INSTANCE = null;
		super.stop(context);
	}

	private synchronized ConnectionProfileFactory getConnectionProfileFactory_() {
		if (this.connectionProfileFactory == null) {
			this.connectionProfileFactory = buildConnectionProfileFactory();
	        this.connectionProfileFactory.start();			
		}
		return this.connectionProfileFactory;
	}

	private DTPConnectionProfileFactory buildConnectionProfileFactory() {
		return DTPConnectionProfileFactory.instance();
	}

	/**
	 * Creates a jar list container for the given DTP driver.
	 */
	public IClasspathContainer buildDriverClasspathContainerFor(String driverName) {
		return new DriverClasspathContainer(driverName);
	}

}