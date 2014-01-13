package org.multibit.hd.ui.views.components;

import org.multibit.hd.core.api.MessageKey;
import org.multibit.hd.ui.i18n.Languages;
import org.multibit.hd.ui.views.fonts.AwesomeDecorator;
import org.multibit.hd.ui.views.fonts.AwesomeIcon;
import org.multibit.hd.ui.views.themes.Themes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * <p>Utility to provide the following to UI:</p>
 * <ul>
 * <li>Provision of localised buttons</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class Labels {

  public static final float BALANCE_LARGE_FONT_SIZE = 42.0f;
  public static final float BALANCE_NORMAL_FONT_SIZE = 28.0f;
  public static final float PANEL_CLOSE_FONT_SIZE = 28.0f;

  /**
   * Utilities have no public constructor
   */
  private Labels() {
  }

  /**
   * @param key    The resource key for the i18n message text
   * @param values The data values for token replacement in the message text
   *
   * @return A new label with default styling
   */
  public static JLabel newLabel(MessageKey key, Object... values) {
    return new JLabel(Languages.safeText(key, values));
  }

  /**
   * @param key The message key
   *
   * @return A new label with appropriate font and theme
   */
  public static JLabel newTitleLabel(MessageKey key) {

    JLabel label = newLabel(key);

    // Font
    Font font = label.getFont().deriveFont(BALANCE_LARGE_FONT_SIZE);
    label.setFont(font);

    // Theme
    label.setForeground(Themes.currentTheme.text());

    return label;

  }

  /**
   * <p>Create a new label with appropriate font/theme for a note. Interpret the contents of the text as Markdown for HTML translation.</p>
   *
   * @param keys   The message keys for each line referencing simple HTML (standard wrapping/breaking elements like {@literal <html></html>} and {@literal <br/>} will be provided)
   * @param values The substitution values for each line if applicable
   *
   * @return A new label with HTML formatting to correctly render the line break and contents
   */
  public static JLabel newNoteLabel(MessageKey[] keys, Object[][] values) {

    String[] safeHtml = new String[keys.length];
    for (int i = 0; i < keys.length; i++) {
      if (values.length > 0) {
        // Substitution is required
        safeHtml[i] = Languages.safeText(keys[i], values[i]);
      } else {
        // Key only
        safeHtml[i] = Languages.safeText(keys[i]);
      }
    }

    // Wrap in HTML to ensure line breaks are respected
    StringBuilder sb = new StringBuilder("<html>");
    for (String line : safeHtml) {
      sb.append(line);
      sb.append("<br/><br/>");
    }
    sb.append("</html>");

    JLabel label = new JLabel(sb.toString());

    // Theme
    label.setForeground(Themes.currentTheme.text());

    return label;

  }

  /**
   * <p>A "status" label sets a label with a check or cross icon</p>
   *
   * @param key    The message key
   * @param values The substitution values
   * @param status True if a check icon is required, false for a cross
   *
   * @return A new label
   */
  public static JLabel newStatusLabel(MessageKey key, Object[] values, boolean status) {

    JLabel label = Labels.newLabel(key, values);

    if (status) {
      AwesomeDecorator.bindIcon(AwesomeIcon.CHECK, label, true, AwesomeDecorator.NORMAL_ICON_SIZE);
    } else {
      AwesomeDecorator.bindIcon(AwesomeIcon.TIMES, label, true, AwesomeDecorator.NORMAL_ICON_SIZE);
    }

    return label;
  }

  /**
   * @return A new "verification" status label
   */
  public static JLabel newVerificationStatus(boolean status) {

    JLabel label = newStatusLabel(MessageKey.VERIFICATION_STATUS, null, status);

    return label;
  }

  /**
   * @return A new "seed phrase created" status label
   */
  public static JLabel newSeedPhraseCreatedStatus(boolean status) {
    return newStatusLabel(MessageKey.SEED_PHRASE_CREATED_STATUS, null, status);
  }

  /**
   * @return A new "wallet password created" status label
   */
  public static JLabel newWalletPasswordCreatedStatus(boolean status) {
    return newStatusLabel(MessageKey.WALLET_PASSWORD_CREATED_STATUS, null, status);
  }

  /**
   * @return A new "wallet created" status label
   */
  public static JLabel newWalletCreatedStatus(boolean status) {
    return newStatusLabel(MessageKey.WALLET_CREATED_STATUS, null, status);
  }

  /**
   * @return A new "backup location" status label
   */
  public static JLabel newBackupLocationStatus(boolean status) {
    return newStatusLabel(MessageKey.BACKUP_LOCATION_STATUS, null, status);
  }

  /**
   * @return A new "Select language" label
   */
  public static JLabel newSelectLanguageLabel() {

    return new JLabel(Languages.safeText(MessageKey.DISPLAY_LANGUAGE));
  }

  /**
   * @param mouseAdapter The mouse adapter that provides the event handling
   *
   * @return A new panel close "X" label with icon
   */
  public static JLabel newPanelCloseLabel(MouseAdapter mouseAdapter) {

    JLabel panelCloseLabel = new JLabel();

    // Font
    Font panelCloseFont = panelCloseLabel.getFont().deriveFont(PANEL_CLOSE_FONT_SIZE);
    panelCloseLabel.setFont(panelCloseFont);

    AwesomeDecorator.applyIcon(AwesomeIcon.TIMES, panelCloseLabel, true, 16);
    panelCloseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    panelCloseLabel.addMouseListener(mouseAdapter);

    return panelCloseLabel;
  }

  /**
   * <p>The balance labels</p>
   * <ul>
   * <li>[0]: Primary value, possibly decorated with leading symbol/code, to 2dp</li>
   * <li>[1]: Secondary value covering remaining decimal places</li>
   * <li>[2]: Placeholder for trailing symbol/code</li>
   * <li>[3]: Localised exchange rate display</li>
   * </ul>
   *
   * @return A new collection of labels that together form a balance display
   */
  public static JLabel[] newBalanceLabels() {

    JLabel primaryBalanceLabel = new JLabel("0.00");
    JLabel secondaryBalanceLabel = new JLabel("");
    JLabel trailingSymbolLabel = new JLabel("");
    JLabel exchangeLabel = new JLabel("");

    // Font
    Font balanceFont = primaryBalanceLabel.getFont().deriveFont(BALANCE_LARGE_FONT_SIZE);
    Font decimalFont = primaryBalanceLabel.getFont().deriveFont(BALANCE_NORMAL_FONT_SIZE);

    primaryBalanceLabel.setFont(balanceFont);
    secondaryBalanceLabel.setFont(decimalFont);
    trailingSymbolLabel.setFont(balanceFont);
    exchangeLabel.setFont(decimalFont);

    // Theme
    primaryBalanceLabel.setForeground(Themes.currentTheme.text());
    secondaryBalanceLabel.setForeground(Themes.currentTheme.fadedText());
    trailingSymbolLabel.setForeground(Themes.currentTheme.text());
    exchangeLabel.setForeground(Themes.currentTheme.text());

    return new JLabel[]{

      primaryBalanceLabel,
      secondaryBalanceLabel,
      trailingSymbolLabel,
      exchangeLabel
    };

  }

  /**
   * @return A new "Enter password" label
   */
  public static JLabel newEnterPassword() {

    return newLabel(MessageKey.ENTER_PASSWORD);
  }

  /**
   * @return A new "Confirm password" label
   */
  public static JLabel newConfirmPassword() {

    return newLabel(MessageKey.CONFIRM_PASSWORD);
  }

  /**
   * @return A new "You are about to send" message
   */
  public static JLabel newConfirmSendAmount() {

    return newLabel(MessageKey.CONFIRM_SEND_MESSAGE);
  }

  public static JLabel newSeedSize() {
    return newLabel(MessageKey.SEED_SIZE);
  }

  /**
   * @return A new "welcome" note
   */
  public static JLabel newWelcomeNote() {

    return newNoteLabel(new MessageKey[]{
      MessageKey.WELCOME_NOTE_1,
      MessageKey.WELCOME_NOTE_2,
      MessageKey.WELCOME_NOTE_3,
      MessageKey.WELCOME_NOTE_4
    }, new Object[][]{});
  }

  /**
   * @return A new "wallet password" note
   */
  public static JLabel newWalletPasswordNote() {

    return newNoteLabel(new MessageKey[]{
      MessageKey.WALLET_PASSWORD_NOTE_1,
      MessageKey.WALLET_PASSWORD_NOTE_2,
      MessageKey.WALLET_PASSWORD_NOTE_3
    }, new Object[][]{});

  }

  /**
   * @return A new "seed warning" note
   */
  public static JLabel newSeedWarningNote() {

    return newNoteLabel(new MessageKey[]{
      MessageKey.SEED_WARNING_NOTE_1,
      MessageKey.SEED_WARNING_NOTE_2,
      MessageKey.SEED_WARNING_NOTE_3,
      MessageKey.SEED_WARNING_NOTE_4,
    }, new Object[][]{});
  }

  /**
   * @return A new "confirm seed phrase" note
   */
  public static JLabel newConfirmSeedPhraseNote() {

    return newNoteLabel(new MessageKey[]{
      MessageKey.CONFIRM_SEED_PHRASE_NOTE_1,
      MessageKey.CONFIRM_SEED_PHRASE_NOTE_2,
      MessageKey.CONFIRM_SEED_PHRASE_NOTE_3,
      MessageKey.CONFIRM_SEED_PHRASE_NOTE_4
    }, new Object[][]{});
  }

  /**
   * @return A new "select backup directory" note
   */
  public static JLabel newSelectBackupDirectoryNote() {

    return newNoteLabel(new MessageKey[]{
      MessageKey.SELECT_BACKUP_LOCATION_NOTE_1,
      MessageKey.SELECT_BACKUP_LOCATION_NOTE_2,
      MessageKey.SELECT_BACKUP_LOCATION_NOTE_3,
      MessageKey.SELECT_BACKUP_LOCATION_NOTE_4
    }, new Object[][]{});

  }
}
