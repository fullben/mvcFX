package de.fullben.editor.ui.about;

import de.fullben.mvcfx.Controller;
import java.io.IOException;

public class AboutController implements Controller {

  private AboutView view;

  public AboutController() {
    view = null;
  }

  @Override
  public void startUp() throws IOException {
    if (view != null) {
      return;
    }
    view = new AboutView(this);
    view.show();
  }
}
