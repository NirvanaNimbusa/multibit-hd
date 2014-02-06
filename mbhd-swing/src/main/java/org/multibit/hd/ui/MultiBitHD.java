package org.multibit.hd.ui;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.Uninterruptibles;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.mtgox.v2.MtGoxExchange;
import org.multibit.hd.core.api.WalletData;
import org.multibit.hd.core.config.Configurations;
import org.multibit.hd.core.managers.BackupManager;
import org.multibit.hd.core.managers.InstallationManager;
import org.multibit.hd.core.managers.WalletManager;
import org.multibit.hd.core.services.BitcoinNetworkService;
import org.multibit.hd.core.services.CoreServices;
import org.multibit.hd.core.services.ExchangeTickerService;
import org.multibit.hd.core.utils.OSUtils;
import org.multibit.hd.ui.audio.Sounds;
import org.multibit.hd.ui.controllers.HeaderController;
import org.multibit.hd.ui.controllers.MainController;
import org.multibit.hd.ui.controllers.SidebarController;
import org.multibit.hd.ui.events.controller.ControllerEvents;
import org.multibit.hd.ui.events.view.ViewEvents;
import org.multibit.hd.ui.i18n.Languages;
import org.multibit.hd.ui.i18n.MessageKey;
import org.multibit.hd.ui.platform.GenericApplication;
import org.multibit.hd.ui.views.*;
import org.multibit.hd.ui.views.components.Panels;
import org.multibit.hd.ui.views.wizards.Wizards;
import org.multibit.hd.ui.views.wizards.welcome.WelcomeWizardState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * <p>Main entry point to the application</p>
 */
public class MultiBitHD {

  private static final Logger log = LoggerFactory.getLogger(MultiBitHD.class);

  // TODO Implement this
  private static GenericApplication genericApplication = null;

  private static BitcoinNetworkService bitcoinNetworkService;

  /**
   * <p>Main entry point to the application</p>
   *
   * @param args None specified
   */
  public static void main(final String[] args) throws InterruptedException, UnsupportedLookAndFeelException {

    // We guarantee the JDK version through the packager so we can use this direct
    UIManager.setLookAndFeel(new NimbusLookAndFeel());

    // Start the core services
    CoreServices.main(args);

    // Pre-load sound library
    Sounds.initialise();

    if (OSUtils.isMac()) {
      System.getProperties().setProperty("com.apple.mrj.application.apple.menu.about.name", Languages.safeText(MessageKey.APPLICATION_TITLE));
    }

    ExchangeTickerService exchangeTickerService = CoreServices.newExchangeService(MtGoxExchange.class.getName());
    bitcoinNetworkService = CoreServices.newBitcoinNetworkService();

    // Create views
    HeaderView headerView = new HeaderView();
    SidebarView sidebarView = new SidebarView();
    DetailView detailView = new DetailView();
    FooterView footerView = new FooterView();

    MainView mainView = new MainView(
      headerView.getContentPanel(),
      sidebarView.getContentPanel(),
      detailView.getContentPanel(),
      footerView.getContentPanel()
    );

    // Create controllers
    MainController mainController = new MainController();
    HeaderController headerController = new HeaderController();
    SidebarController sidebarController = new SidebarController();

    // Initialise the wallet manager, which will load the current wallet if available
    File applicationDataDirectory = InstallationManager.createApplicationDataDirectory();

    // Start up the exchange service
    exchangeTickerService.start();

    WalletManager.INSTANCE.initialise(applicationDataDirectory);
    BackupManager.INSTANCE.initialise(applicationDataDirectory, null); // TODO load up the cloud backup if available from properties and insert here

    // Show the UI for the current locale
    ControllerEvents.fireChangeLocaleEvent(Configurations.currentConfiguration.getLocale());

    if (WalletManager.INSTANCE.getCurrentWalletData().isPresent()) {
      // There is a wallet present - warm start
      WalletData walletData =  WalletManager.INSTANCE.getCurrentWalletData().get();
      log.debug("The current wallet is:\nWallet id = '" + walletData.getWalletId().toString() + "\n" + walletData.getWallet().toString());

      // TODO need to show warm start dialog (to get password) and unencrypt wallet and contacts

    } else {
      // Show the Welcome wizard
      log.debug("There is no current wallet so showing the 'WelcomeWizard'");
      Panels.showLightBox(Wizards.newClosingWelcomeWizard(WelcomeWizardState.WELCOME_SELECT_LANGUAGE).getWizardPanel());
    }

    // TODO enable the user to switch between the existing wallets

    // Start the bitcoin network service
    bitcoinNetworkService.start();

    Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);

    // If the network starts ok start downloading blocks to catch up with the current blockchain
    if (bitcoinNetworkService.isStartedOk()) {
      bitcoinNetworkService.downloadBlockChain();
    }

    // Provide a starting balance
    // TODO Get this from CoreServices - bitcoinj wallet class should not appear in GUI code
    BigInteger satoshis;
    Optional<WalletData> currentWalletData = WalletManager.INSTANCE.getCurrentWalletData();
    if (currentWalletData.isPresent()) {
      // Use the real wallet data
      satoshis = currentWalletData.get().getWallet().getBalance();
    } else {
      // Use some dummy data
      satoshis = BigInteger.ZERO;
    }
    ViewEvents.fireBalanceChangedEvent(
      satoshis,
      MoneyUtils.fromSatoshi(0),
      "Unknown"
    );
  }

  private void registerEventListeners() {

    // TODO Get this working

    log.info("Configuring native event handling");

  }

  public static BitcoinNetworkService getBitcoinNetworkService() {
    return bitcoinNetworkService;
  }
}
