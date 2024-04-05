package org.jhotdraw.draw.figure;

public class TextFigureEdit extends TextFigureForm {
  // CONNECTING
  // COMPOSITE FIGURES
  // CLONING
  // EVENT HANDLING
  @Override
  public void invalidate() {
    super.invalidate();
    textLayout = null;
  }

  @Override
  protected void validate() {
    super.validate();
    textLayout = null;
  }

  public boolean isTextOverflow() {
    return false;
  }
}
