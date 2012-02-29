/* 
 GeoGebra - Dynamic Mathematics for Everyone
 http://www.geogebra.org

 This file is part of GeoGebra.

 This program is free software; you can redistribute it and/or modify it 
 under the terms of the GNU General Public License as published by 
 the Free Software Foundation.

 */

package geogebra.common.euclidian;

import geogebra.common.awt.Color;
import geogebra.common.awt.Dimension;
import geogebra.common.awt.Font;
import geogebra.common.factories.AwtFactory;
import geogebra.common.factories.SwingFactory;
import geogebra.common.gui.inputfield.AutoCompleteTextField;
import geogebra.common.javax.swing.Box;
import geogebra.common.javax.swing.JLabel;
import geogebra.common.kernel.StringTemplate;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoTextField;
import geogebra.common.main.AbstractApplication;

import geogebra.common.awt.event.FocusEvent;
import geogebra.common.awt.event.KeyEvent;


//import javax.swing.SwingUtilities;

/**
 * Checkbox for free GeoBoolean object.
 * 
 * @author Michael
 */
public final class DrawTextField extends Drawable implements RemoveNeeded {
	/** textfield */
	final GeoTextField geoTextField;

	private boolean isVisible;

	private String oldCaption;
	/** textfield component*/
	AutoCompleteTextField textField;
	private JLabel label;
	//ButtonListener bl;
	private InputFieldListener ifListener;
	private InputFieldKeyListener ifKeyListener;
	private Box box = SwingFactory.prototype.createHorizontalBox();

	/**
	 * @param view view
	 * @param geo textfield
	 */
	public DrawTextField(AbstractEuclidianView view, GeoTextField geo) {
		this.view = view;
		this.geoTextField = geo;
		this.geo = geo;

		// action listener for checkBox
		//bl = new ButtonListener();
		ifListener = new InputFieldListener();
		ifKeyListener = new InputFieldKeyListener();
		textField = SwingFactory.prototype.newAutoCompleteTextField(geo.getLength(), view.getApplication(), this);
		textField.showPopupSymbolButton(true);
		textField.setAutoComplete(false);
		textField.enableColoring(false);
		label = SwingFactory.prototype.newJLabel("Label");
		
		//label.setLabelFor(textField); <- next row
		textField.setLabel(label);
		
		textField.setVisible(true);
		label.setVisible(true);
//		((geogebra.gui.inputfield.AutoCompleteTextField) textField).addFocusListener(bl);
		textField.addFocusListener(AwtFactory.prototype.newFocusListener(ifListener));
//		label.addMouseListener(bl);
//		label.addMouseMotionListener(bl);
		textField.addKeyListener(AwtFactory.prototype.newKeyListener(ifKeyListener));
		box.add(label);
		box.add(textField);
		
		view.add(box);

		// Add mouse listeners to textField so that it becomes draggable
		// on a right click. These listeners are registered first to prevent
		// the JTextField listeners from initiating editing.
		/*
		 * MouseListener[] ml = textField.getMouseListeners(); for(int i = 0;
		 * i<ml.length; i++){ textField.removeMouseListener(ml[i]); }
		 * MouseMotionListener[] mml = textField.getMouseMotionListeners();
		 * for(int i = 0; i<mml.length; i++){
		 * textField.removeMouseMotionListener(mml[i]); }
		 * 
		 * textField.addMouseListener(bl); for(int i = 0; i<mml.length; i++){
		 * textField.addMouseMotionListener(mml[i]); }
		 * 
		 * textField.addMouseMotionListener(bl); for(int i = 0; i<ml.length;
		 * i++){ textField.addMouseListener(ml[i]); }
		 */

		update();
	}

	
	/**
	 * Listens to events in this textfield
	 * @author Michael + Judit
	 */
	public class InputFieldListener extends geogebra.common.awt.event.FocusListener{

		/**
		 * Creates new listener
		 */
		public InputFieldListener() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param e focus event
		 */
		public void focusGained(FocusEvent e) {
			view.getEuclidianController().textfieldHasFocus(true);
			geoTextField.updateText(textField);
	
		}
	
		/**
		 * @param e focus event
		 */
		public void focusLost(FocusEvent e) {
			view.getEuclidianController().textfieldHasFocus(false);	
			geoTextField.textObjectUpdated(textField);
	
		}
	}
	

	
	/**
	 * Listens to key events in this textfield
	 * @author Michael + Judit
	 */
	public class InputFieldKeyListener extends geogebra.common.awt.event.KeyListener{

		/**
		 * Creates new listener
		 */
		public InputFieldKeyListener() {
			// TODO Auto-generated constructor stub
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == '\n') {
				view.getEuclidianController().textfieldHasFocus(false);
				geoTextField.textObjectUpdated(textField);
				geoTextField.textSubmitted();
			}
		}
	}
	
	
	
	
//	private class ButtonListener implements MouseListener, MouseMotionListener,
//			FocusListener, KeyListener {
//
//		private boolean dragging = false;
//		private final EuclidianController ec = ((EuclidianView)view).getEuclidianController();
//
//		public ButtonListener() {
//			// TODO Auto-generated constructor stub
//		}
//
//		/**
//		 * Handles click on check box. Changes value of GeoBoolean.
//		 */
//		@SuppressWarnings("unused")
//		public void itemStateChanged(ItemEvent e) {
//			// TODO delete?
//		}
//
//		public void mouseDragged(MouseEvent e) {
//
//			dragging = true;
//			e.translatePoint(box.getX(), box.getY());
//			ec.mouseDragged(e);
//			((EuclidianView)view).setToolTipText(null);
//		}
//
//		public void mouseMoved(MouseEvent e) {
//
//			e.translatePoint(box.getX(), box.getY());
//			ec.mouseMoved(e);
//			((EuclidianView)view).setToolTipText(null);
//		}
//
//		public void mouseClicked(MouseEvent e) {
//
//			if (e.getClickCount() > 1) {
//				return;
//			}
//
//			e.translatePoint(box.getX(), box.getY());
//			ec.mouseClicked(e);
//		}
//
//		public void mousePressed(MouseEvent e) {
//
//			// prevent textField editing on right click
//			if (Application.isRightClick(e)) {
//				e.consume();
//			}
//
//			dragging = false;
//			e.translatePoint(box.getX(), box.getY());
//			ec.mousePressed(e);
//		}
//
//		public void mouseReleased(MouseEvent e) {
//
//			// prevent textField editing on right click
//			if (Application.isRightClick(e)) {
//				e.consume();
//			}
//
//			if (!dragging && !e.isMetaDown() && !e.isPopupTrigger()
//					&& (view.getMode() == EuclidianConstants.MODE_MOVE)) {
//				// handle LEFT CLICK
//				// geoBool.setValue(!geoBool.getBoolean());
//				// geoBool.updateRepaint();
//				// geo.runScript();
//				//
//
//				// make sure itemChanged does not change
//				// the value back my faking a drag
//				dragging = true;
//			} else {
//				// handle right click and dragging
//				e.translatePoint(box.getX(), box.getY());
//				ec.mouseReleased(e);
//			}
//
//		}
//
//		public void mouseEntered(MouseEvent arg0) {
//			if (!textField.hasFocus()) {
//				hit = true;
//				((EuclidianView)view).setToolTipText(null);
//				geoButton.updateText(textField);
//			}
//		}
//		public void mouseExited(MouseEvent arg0) {
//			hit = false;
//		}
//
//		public void focusGained(FocusEvent e) {
//			((EuclidianView)view).getEuclidianController().textfieldHasFocus(true);
//			geoTextField.updateText(textField);
//
//		}
//
//		public void focusLost(FocusEvent e) {
//			((EuclidianView)view).getEuclidianController().textfieldHasFocus(false);
//
//			geoTextField.textObjectUpdated(textField);
//
//		}
//
//		public void keyPressed(KeyEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//
//		public void keyReleased(KeyEvent e) {
//			if (e.getKeyChar() == '\n') {
//				((EuclidianView)view).getEuclidianController().textfieldHasFocus(false);
//				geoTextField.textObjectUpdated(textField);
//			}
//
//		}
//
//		public void keyTyped(KeyEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//	}
	
	private int oldLength = 0;
	@Override
	final public void update() {
		isVisible = geo.isEuclidianVisible();
		// textField.setVisible(isVisible);
		// label.setVisible(isVisible);
		box.setVisible(isVisible);
		int length = geoTextField.getLength();
		if(length!=oldLength){
			textField.setColumns(length);
			textField.showPopupSymbolButton(length>8);
		}
		if (!isVisible) {
			return;
		}

		// show hide label by setting text
		if (geo.isLabelVisible()) {
			// get caption to show r
			String caption = geo.getCaption(StringTemplate.defaultTemplate);
			if (!caption.equals(oldCaption)) {
				oldCaption = caption;
				labelDesc = GeoElement.indicesToHTML(caption, true);
			}
			box.setVisible(false); // avoid redraw error
			label.setText(labelDesc);
			box.setVisible(true);
		} else {
			// put back to "" from "   " so that the position is the same in ggb40 and ggb42
			label.setText("");
		}

		int fontSize = view.getFontSize() + geoTextField.getFontSize();
		AbstractApplication app = view.getApplication();

		Font vFont = view.getFont();
		Font font = app.getFontCanDisplay(textField.getText(), false,
				vFont.getStyle(), fontSize);

		textField.setOpaque(true);
		label.setOpaque(false);
		textField.setFont(font);
		label.setFont(font);
		textField.setForeground(geo.getObjectColor());
		label.setForeground(geo.getObjectColor());
		Color bgCol = geo.getBackgroundColor();
		textField.setBackground(bgCol != null ? bgCol : view.getBackgroundCommon());

		textField.setFocusable(true);
		textField.setEditable(true);
		geoTextField.updateText(textField);
		// set checkbox state
		// jButton.removeItemListener(bl);
		// jButton.setSelected(geo.getBoolean());
		// jButton.addItemListener(bl);

		xLabel = geo.labelOffsetX;
		yLabel = geo.labelOffsetY;
		Dimension prefSize = box.getPreferredSize();
		labelRectangle.setBounds(xLabel, yLabel, prefSize.getWidth(),
				prefSize.getHeight());
		box.setBounds(labelRectangle);
	}

	@SuppressWarnings("unused")
	private void updateLabel() {
		// TODO delete?
		/*
		 * xLabel = geo.labelOffsetX; yLabel = geo.labelOffsetY;
		 * 
		 * labelRectangle.setBounds(xLabel, yLabel, ((textSize == null) ? 0 :
		 * textSize.x), 12);
		 */

	}

	@Override
	final public void draw(geogebra.common.awt.Graphics2D g2) {
		if (isVisible) {
			if (geo.doHighlighting()) {
				label.setOpaque(true);
				label.setBackground(Color.lightGray);

			} else {
				label.setOpaque(false);
			}
		}
	}

	/**
	 * Removes button from view again
	 */
	final public void remove() {
		view.remove(box);
	}

	/**
	 * was this object clicked at? (mouse pointer location (x,y) in screen
	 * coords)
	 */
	@Override
	final public boolean hit(int x, int y) {
		//AbstractApplication.debug(x + " " + y + " " + box.getBounds().contains(x, y));
		return box.getBounds().contains(x, y);
	}

	@Override
	final public boolean isInside(geogebra.common.awt.Rectangle rect) {
		return rect.contains(labelRectangle);
	}

	/**
	 * Returns false
	 */
	@Override
	public boolean hitLabel(int x, int y) {
		return false;
	}

	@Override
	final public GeoElement getGeoElement() {
		return geo;
	}

	@Override
	final public void setGeoElement(GeoElement geo) {
		this.geo = geo;
	}

	/**
	 * @param str input string
	 */
	public void setFocus(final String str) {
		textField.requestFocus();
		if (str != null) {
//			SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					textField.setText(str);
//				}
//			});
			textField.wrapSetText(str);
		}
		
	}

}
