/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.selection;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jpt.core.JpaFile;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class TextEditorSelectionParticipant
	implements JpaSelectionParticipant
{
	private final JpaSelectionManager selectionManager;

	final ITextEditor textEditor;

	private final IPropertyListener editorInputListener;

	private final ISelectionChangedListener editorSelectionListener;

	private JpaSelection currentSelection;

	private boolean forwardSelection = true;  // TODO this just smells wrong  ~bjv


	public TextEditorSelectionParticipant(JpaSelectionManager selectionManager, ITextEditor textEditor) {
		super();
		this.selectionManager = selectionManager;
		this.textEditor = textEditor;
		this.editorInputListener = new EditorInputListener();
		this.textEditor.addPropertyListener(this.editorInputListener);
		this.editorSelectionListener = new EditorSelectionListener();
		this.postSelectionProvider().addPostSelectionChangedListener(this.editorSelectionListener);
		this.currentSelection = this.calculateSelection();
	}

	// ********** IJpaSelectionParticipant implementation **********

	public JpaSelection getSelection() {
		return this.currentSelection;
	}

	public void selectionChanged(JpaSelectionEvent evt) {
		JpaSelection newSelection = evt.getSelection();
		
		if ((newSelection == JpaSelection.NULL_SELECTION)
			|| newSelection.equals(this.currentSelection)) {
			return;
		}
		
		if (getActiveTextEditor() != textEditor) {
			return;
		}
		
		this.forwardSelection = false;
		TextRange textRange = newSelection.getSelectedNode().getSelectionTextRange();
		if (textRange != null) {
			this.textEditor.selectAndReveal(textRange.getOffset(), textRange.getLength());
			this.currentSelection = newSelection;
		}
		this.forwardSelection = true;
	}

	public boolean disposeOnHide() {
		return true;
	}

	public void dispose() {
		this.textEditor.removePropertyListener(this.editorInputListener);
		this.postSelectionProvider().removePostSelectionChangedListener(this.editorSelectionListener);
	}


	// ********** internal methods **********

	private JpaSelection calculateSelection() {
		ISelection selection = this.textEditor.getSelectionProvider().getSelection();
		if (! (selection instanceof ITextSelection)) {
			return JpaSelection.NULL_SELECTION;
		}

		JpaFile jpaFile = this.jpaFile();
		if (jpaFile == null) {
			return JpaSelection.NULL_SELECTION;
		}

		JpaStructureNode selectedNode = jpaFile.getStructureNode(((ITextSelection) selection).getOffset());
		if (selectedNode == null) {
			return JpaSelection.NULL_SELECTION;
		}

		return new DefaultJpaSelection(selectedNode);
	}
	
	private IWorkbenchPage getActivePage() {
		return textEditor.getEditorSite().getWorkbenchWindow().getActivePage();
	}
	
	private IWorkbenchPart getActivePart() {
		IWorkbenchPage activePage = getActivePage();
		return (activePage == null) ? null: activePage.getActivePart();
	}
	
	private IEditorPart getActiveEditor() {
		IWorkbenchPage activePage = getActivePage();
		return (activePage == null) ? null: activePage.getActiveEditor();
	}
	
	private ITextEditor getActiveTextEditor() {
		return getTextEditor(getActiveEditor());
	}
	
	private ITextEditor getTextEditor(IWorkbenchPart part) {
		return (part == null) ? null : (ITextEditor) part.getAdapter(ITextEditor.class);
	}
	
	private JpaFile jpaFile() {
		IEditorInput input = this.textEditor.getEditorInput();
		if ( ! (input instanceof IFileEditorInput)) {
			return null;
		}
		return JptCorePlugin.getJpaFile(((IFileEditorInput) input).getFile());
	}

	private IPostSelectionProvider postSelectionProvider() {
		return (IPostSelectionProvider) this.textEditor.getSelectionProvider();
	}


	// ********** listener callbacks **********

	void editorInputChanged() {
		JpaSelection newSelection = this.calculateSelection();
		if (newSelection.equals(this.currentSelection)) {
			return;
		}
		this.currentSelection = newSelection;
		
		if (this.forwardSelection) {
			this.selectionManager.select(newSelection, this);
		}
	}

	void editorSelectionChanged(SelectionChangedEvent event) {
		// This is a bit kludgey.  We check to see if the selection event 
		// occurred when a participating part is active (and so, ostensibly,
		// *because* of the participating part).  If so, we reselect the valid 
		// text.
		IWorkbenchPart activePart = getActivePart();
		if (getTextEditor(activePart) != textEditor && selectionManager.isRegistered(activePart)) {
			if (currentSelection.isEmpty()) {
				return;
			}
			
			this.forwardSelection = false;
			TextRange textRange = currentSelection.getSelectedNode().getSelectionTextRange();
			if (textRange != null) {
				this.textEditor.selectAndReveal(textRange.getOffset(), textRange.getLength());
			}
			this.forwardSelection = true;
			
			return;
		}
		
		JpaSelection newSelection = this.calculateSelection();
		if (newSelection.equals(this.currentSelection)) {
			return;
		}
		this.currentSelection = newSelection;

		if (this.forwardSelection) {
			this.selectionManager.select(newSelection, this);
		}
	}


	// ********** listeners **********

	private class EditorInputListener implements IPropertyListener {
		EditorInputListener() {
			super();
		}
		public void propertyChanged(Object source, int propId) {
			if ((source == TextEditorSelectionParticipant.this.textEditor)
						&& (propId == IEditorPart.PROP_INPUT)) {
				TextEditorSelectionParticipant.this.editorInputChanged();
			}
		}
	}


	private class EditorSelectionListener implements ISelectionChangedListener {
		EditorSelectionListener() {
			super();
		}
		public void selectionChanged(SelectionChangedEvent event) {
			TextEditorSelectionParticipant.this.editorSelectionChanged(event);
		}
	}

}
