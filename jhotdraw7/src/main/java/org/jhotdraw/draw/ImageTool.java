/*
 * @(#)ImageTool.java  2.0  2008-05-24
 *
 * Copyright (c) 1996-2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.draw;

import java.awt.image.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.gui.Worker;
import org.jhotdraw.util.*;
import org.jhotdraw.geom.*;
import org.jhotdraw.undo.*;

/**
 * A tool to create new figures that implement the ImageHolderFigure
 * interface, such as ImageFigure. The figure to be created is specified by a
 * prototype.
 * <p>
 * Immediately, after the ImageTool has been activated, it opens a JFileChooser,
 * letting the user specify an image file. The the user then performs 
 * the following mouse gesture:
 * <ol>
 * <li>Press the mouse button and drag the mouse over the DrawingView. 
 * This defines the bounds of the Figure.</li>
 * </ol>
 * 
 * @author Werner Randelshofer
 * @version 2.0 2008-05-24 Changed behavior of ImageTool. 
 * <br>1.1 2008-05-17 Honor toolDoneAfterCreation property.
 * <br>1.0 December 14, 2006 Created.
 */
public class ImageTool extends CreationTool {

   protected JFileChooser fileChooser;
   
   protected Thread workerThread;
   
    /** Creates a new instance. */
    public ImageTool(ImageHolderFigure prototype) {
        super(prototype);
    }

    /** Creates a new instance. */
    public ImageTool(ImageHolderFigure prototype, Map attributes) {
        super(prototype, attributes);
    }

    public void activate(DrawingEditor editor) {
        super.activate(editor);
        
        if (workerThread != null) {
            try {
                workerThread.join();
            } catch (InterruptedException ex) {
              // ignore
            }
        }
        
        if (getFileChooser().showOpenDialog(getView().getComponent()) == JFileChooser.APPROVE_OPTION) {
            final File file = getFileChooser().getSelectedFile();
            final ImageHolderFigure loaderFigure = ((ImageHolderFigure) prototype.clone());
            Worker worker = new Worker() {

                public Object construct() {
                    try {
                        ((ImageHolderFigure) loaderFigure).loadImage(file);
                    } catch (Throwable t) {
                        return t;
                    }
                    return null;
                }

                public void finished(Object value) {
                    if (value instanceof Throwable) {
                        Throwable t = (Throwable) value;
                        JOptionPane.showMessageDialog(getView().getComponent(),
                                t.getMessage(),
                                null,
                                JOptionPane.ERROR_MESSAGE);
                        getDrawing().remove(createdFigure);
                        fireToolDone();
                    } else {
                            try {
                        if (createdFigure == null) {
                                ((ImageHolderFigure) prototype).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                        } else {
                                ((ImageHolderFigure) createdFigure).setImage(loaderFigure.getImageData(), loaderFigure.getBufferedImage());
                        }
                            } catch (IOException ex) {
                        JOptionPane.showMessageDialog(getView().getComponent(),
                                ex.getMessage(),
                                null,
                                JOptionPane.ERROR_MESSAGE);
                            }
                    }
                }
            };
            workerThread = new Thread(worker);
            workerThread.start();
        } else {
            getDrawing().remove(createdFigure);
            if (isToolDoneAfterCreation()) {
                fireToolDone();
            }
        }
    }
    
    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        return fileChooser;
    }
}
