package org.multibit.hd.ui.views;

import net.miginfocom.swing.MigLayout;
import org.multibit.hd.core.services.CoreServices;
import org.multibit.hd.ui.events.ViewEvents;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * <p>View to provide the following to application:</p>
 * <ul>
 * <li>Provision of components and layout for the sidebar display (LHS of split pane)</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class SidebarView {

  private final JPanel contentPanel;

  private JTree sidebarTree;

  public SidebarView() {

    CoreServices.uiEventBus.register(this);

    MigLayout layout = new MigLayout("filly");
    contentPanel = new JPanel(layout);

    contentPanel.add(createSidebarContent(), "push");

  }

  /**
   * @return The content panel for this View
   */
  public JPanel getContentPanel() {
    return contentPanel;
  }

  /**
   * @return The sidebar content
   */
  private JScrollPane createSidebarContent() {

    JScrollPane sidebarPane = new JScrollPane();

    sidebarTree = new JTree(createSidebarTreeNodes());
    sidebarTree.setShowsRootHandles(false);
    sidebarTree.setRootVisible(false);
    // TODO Integrate with styles
    sidebarTree.setBackground(new Color(240, 240, 240));
    sidebarTree.setVisibleRowCount(10);
    sidebarTree.setExpandsSelectedPaths(true);

    sidebarTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

        ViewEvents.fireShowDetailScreenEvent(Screen.MAIN_WALLET);


      }
    });

    sidebarPane.setViewportView(sidebarTree);
    // TODO Integrate with configuration
    sidebarPane.setPreferredSize(new Dimension(150, 1024));

    return sidebarPane;
  }


  private DefaultMutableTreeNode createSidebarTreeNodes() {

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Wallet");

    DefaultMutableTreeNode wallet = new DefaultMutableTreeNode("Wallet");
    wallet.add(new DefaultMutableTreeNode("Contacts"));
    wallet.add(new DefaultMutableTreeNode("Transactions"));
    root.add(wallet);

    DefaultMutableTreeNode trezor1 = new DefaultMutableTreeNode("Trezor 1");
    trezor1.add(new DefaultMutableTreeNode("Contacts"));
    trezor1.add(new DefaultMutableTreeNode("Transactions"));
    root.add(trezor1);

    DefaultMutableTreeNode trezor2 = new DefaultMutableTreeNode("Trezor 2");
    trezor2.add(new DefaultMutableTreeNode("Contacts"));
    trezor2.add(new DefaultMutableTreeNode("Transactions"));
    root.add(trezor2);

    root.add(new DefaultMutableTreeNode("Help"));
    root.add(new DefaultMutableTreeNode("History"));
    root.add(new DefaultMutableTreeNode("Preferences"));
    root.add(new DefaultMutableTreeNode("Tools"));

    return root;
  }

}