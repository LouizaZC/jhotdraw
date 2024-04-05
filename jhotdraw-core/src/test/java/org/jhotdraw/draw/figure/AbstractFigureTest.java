/*
 * Copyright (C) 2015 JHotDraw.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.jhotdraw.draw.figure;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jhotdraw.draw.DefaultDrawing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tw
 */
public class AbstractFigureTest {

  @Test
  public void testChangedWithoutWillChange() {
    assertThrows(IllegalStateException.class, () -> new AbstractFigureImpl().changed());
  }

  @Test
  public void testWillChangeChangedEvents() {
    AbstractAttributedFigure figure = new AbstractFigureImpl();
    assertEquals(figure.getChangingDepth(), 0);
    figure.willChange();
    assertEquals(figure.getChangingDepth(), 1);
    figure.willChange();
    assertEquals(figure.getChangingDepth(), 2);
    figure.changed();
    assertEquals(figure.getChangingDepth(), 1);
    figure.changed();
    assertEquals(figure.getChangingDepth(), 0);
  }
  @Test
  public void testChangeFigureOrder() {

    DefaultDrawing drawing = new DefaultDrawing();

    Figure figure1 = new AbstractFigureImpl();
    Figure figure2 = new AbstractFigureImpl();

    drawing.add(figure1);
    drawing.add(figure2);

    drawing.remove(figure1);

    assertSame(drawing.getChild(0), figure2);

  }

  @Test
  public void testRemoveSecondFigure() {
    DefaultDrawing drawing = new DefaultDrawing();

    Figure figure1 = new AbstractFigureImpl();
    Figure figure2 = new AbstractFigureImpl();

    drawing.add(figure1);
    drawing.add(figure2);

    drawing.remove(figure2);

    assertSame(drawing.getChild(0), figure1);
  }

  @Test
  public void testClone() {

    AbstractAttributedFigure figure = new AbstractFigureImpl();
    AbstractAttributedFigure clonedFigure = figure.clone();

    assertNotSame(figure, clonedFigure);
    assertEquals(figure.attr(), clonedFigure.attr());

    assertEquals(figure.isSelectable(), clonedFigure.isSelectable());
    assertEquals(figure.isRemovable(), clonedFigure.isRemovable());
    assertEquals(figure.isVisible(), clonedFigure.isVisible());
    assertEquals(figure.isTransformable(), clonedFigure.isTransformable());
    assertEquals(figure.isConnectable(), clonedFigure.isConnectable());

  }



  public class AbstractFigureImpl extends AbstractAttributedFigure {

    @Override
    public void draw(Graphics2D g) {}

    @Override
    public Rectangle2D.Double getBounds(double scale) {
      return null;
    }

    @Override
    public boolean contains(Point2D.Double p) {
      return true;
    }

    @Override
    public Object getTransformRestoreData() {
      return null;
    }

    @Override
    public void restoreTransformTo(Object restoreData) {}

    @Override
    public void transform(AffineTransform tx) {}

    @Override
    public Rectangle2D.Double getDrawingArea(double factor) {
      return null;
    }

    @Override
    public Attributes attr() {
      return null;
    }

    @Override
    protected void drawFill(Graphics2D g) {}

    @Override
    protected void drawStroke(Graphics2D g) {}

    @Override
    public boolean contains(Point2D.Double p, double scaleDenominator) {
      return false;
    }
  }
}
