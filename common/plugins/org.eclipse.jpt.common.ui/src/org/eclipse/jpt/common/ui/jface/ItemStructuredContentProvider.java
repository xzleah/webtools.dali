/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.jface;

/**
 * Implementations of this interface can be used to maintain the elements
 * of a specific input element. The implementation will monitor
 * the item for any changes that affect the elements and forward them
 * appropriately to the {@link Manager}.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @see org.eclipse.jface.viewers.IContentProvider
 * @see org.eclipse.jface.viewers.IStructuredContentProvider
 */
public interface ItemStructuredContentProvider {
	/**
	 * Return the input element's elements.
	 * <strong>NB:</strong>
	 * When this method is called, the item is an <em>input</em> element.
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, Object, Object)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(Object)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
	 */
	Object[] getElements();

	/**
	 * Dispose the item content provider.
	 * Remove any item listeners as appropriate.
	 */
	void dispose();


	/**
	 * An item structured content provider's manager is notified whenever the
	 * input element's elements have changed.
	 */
	interface Manager {
		/**
		 * The elements for the specified input element have changed.
		 * Update appropriately.
		 */
		void updateElements(Object inputElement);

		/**
		 * The specified element has been removed from the input element.
		 * Dispose of the element's providers, if necessary.
		 */
		void dispose(Object element);
	}


	/**
	 * Factory interface for constructing item content providers.
	 */
	public interface Factory {
		/**
		 * Build a structured content provider for the specified item.
		 */
		ItemStructuredContentProvider buildProvider(Object item, Manager manager);
	}
}
