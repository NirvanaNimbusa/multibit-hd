package org.multibit.hd.ui.views.components;

import org.multibit.hd.ui.views.themes.Themes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * <p>Utility to provide the following to UI:</p>
 * <ul>
 * <li>Provision of localised text boxes</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class TextBoxes {

  /**
   * The size of the password text area in characters
   */
  public static final int PASSWORD_AREA = 240;

  /**
   * Maintain a reference to the platform password echo character
   */
  private static char passwordEchoChar=new JPasswordField().getEchoChar();

  /**
   * Utilities have no public constructor
   */
  private TextBoxes() {
  }

  /**
   * @return A new "Recipient" auto-completer
   */
  public static JTextField newRecipient() {

    JTextField textField = new JTextField(40);
    textField.setBackground(Themes.currentTheme.dataEntryBackground());

    return textField;
  }

  /**
   * @return A new "Password" text field
   */
  public static JPasswordField newPassword() {

    JPasswordField passwordField = new JPasswordField(40);
    passwordField.setBackground(Themes.currentTheme.dataEntryBackground());

    return passwordField;
  }

  /**
   * @return A new "Notes" text area
   */
  public static JTextArea newNotes() {

    JTextArea textArea = new JTextArea(5, 40);
    textArea.setBackground(Themes.currentTheme.dataEntryBackground());

    return textArea;
  }

  /**
   * @return A new "seed phrase" text area for display only (no copy/paste etc)
   */
  public static JTextArea newDisplaySeedPhrase() {

    // Build off the enter seed phrase
    JTextArea textArea = newEnterSeedPhrase();

    // Prevent copy/paste operations
    textArea.setTransferHandler(null);
    textArea.setEditable(false);

    return textArea;

  }

  /**
   * @return A new "seed phrase" text area for entry
   */
  public static JTextArea newEnterSeedPhrase() {

    // Keep this in line with the PASSWORD_AREA constant
    JTextArea textArea = new JTextArea(6, 40);

    textArea.setBackground(Themes.currentTheme.readOnlyBackground());
    textArea.setFont(new Font("Courier New", Font.PLAIN, 14));

    // Ensure we provide a suitable inner margin to allow letters to be clear
    Border margin = new EmptyBorder(2, 4, 2, 4);
    textArea.setBorder(margin);

    // Ensure line and word wrapping occur as required
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    return textArea;

  }

  /**
   * @return A new "Amount" text field for currency entry
   */
  public static JTextField newCurrency(String amount) {

    JTextField textField = new JTextField(amount, 20);
    textField.setBackground(Themes.currentTheme.dataEntryBackground());

    return textField;
  }

  /**
   * @return The platform echo character for password fields
   */
  public static char getPasswordEchoChar() {

    return passwordEchoChar;
  }
}