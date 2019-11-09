package actions;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class BalloonPopup {
    private HyperlinkListener hyperlinkListener;

    public BalloonPopup() {

    }

    public void createBalloonPopup(JComponent component, Balloon.Position position, String balloonText, MessageType type) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(balloonText, type, hyperlinkListener)
                .setFadeoutTime(3000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(component), position);
    }

}
