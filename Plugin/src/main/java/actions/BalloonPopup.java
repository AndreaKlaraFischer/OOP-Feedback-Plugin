package actions;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;


public class BalloonPopup {
    public BalloonPopup() {

    }

    public void createBalloonPopup(JComponent component, Balloon.Position position, String balloonText, MessageType type) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(balloonText, type, null)
                .setFadeoutTime(3000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(component), position);
    }

    public void createStickyBalloonPopup(JComponent component, Balloon.Position position, String balloonText, MessageType type) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(balloonText, type, null)
                .setHideOnCloseClick(true)
                .createBalloon()
                .show(RelativePoint.getCenterOf(component), position);
    }
}
