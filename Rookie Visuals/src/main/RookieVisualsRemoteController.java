package main;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Dialog for remote control of SoundBite slaves.
 * 
 * @author  Stefan Marks
 * @version 1.0 - 13.09.2013: Created
 */
public class RookieVisualsRemoteController extends javax.swing.JFrame
{
    /**
     * Creates a new SoundBite controller.
     */
    public RookieVisualsRemoteController()
    {
        clients = new DefaultListModel<>();
        vars    = new RookieVisualsVariables();
        
        clients.addElement(new RookieVisualClient("localhost"));

        nameListModel = new DefaultListModel();
        loadNameList();
        
        initComponents();
    }

    
    private void loadNameList()
    {
        try
        {
            File listFile = new File("./names.txt");
            BufferedReader br = new BufferedReader(new FileReader(listFile));
            while ( br.ready() )
            {
                String s = br.readLine();
                if ( !s.isEmpty() )
                {
                    nameListModel.addElement(s);
                }
            }
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog(this, 
                    "Could not load name list.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sets up the controls
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        javax.swing.JPanel pnlContent = new javax.swing.JPanel();
        javax.swing.JPanel pnlClients = new javax.swing.JPanel();
        javax.swing.JScrollPane scrlSlaves = new javax.swing.JScrollPane();
        lstClients = new javax.swing.JList();
        pnlSlaveButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        javax.swing.JPanel pnlButtons = new javax.swing.JPanel();
        javax.swing.JPanel pnlGUI = new javax.swing.JPanel();
        javax.swing.JLabel lblLogo = new javax.swing.JLabel();
        btnLogo = new javax.swing.JToggleButton();
        javax.swing.JLabel lblCurtain = new javax.swing.JLabel();
        btnCurtain = new javax.swing.JToggleButton();
        javax.swing.JPanel pnlNames = new javax.swing.JPanel();
        pnlNameList = new javax.swing.JPanel();
        scrlNames = new javax.swing.JScrollPane();
        lstNames = new javax.swing.JList();
        pnlNameButtons = new javax.swing.JPanel();
        btnSendName = new javax.swing.JButton();
        btnHideName = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SoundBite Remote Controller");

        pnlContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlContent.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        pnlClients.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Rookie Visual Clients"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlClients.setLayout(new java.awt.BorderLayout(0, 5));

        lstClients.setModel(clients);
        scrlSlaves.setViewportView(lstClients);

        pnlClients.add(scrlSlaves, java.awt.BorderLayout.CENTER);

        pnlSlaveButtons.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });
        pnlSlaveButtons.add(btnAdd);

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });
        pnlSlaveButtons.add(btnRemove);

        pnlClients.add(pnlSlaveButtons, java.awt.BorderLayout.PAGE_END);

        pnlContent.add(pnlClients);

        pnlButtons.setAlignmentY(0.0F);
        pnlButtons.setLayout(new java.awt.BorderLayout(0, 10));

        pnlGUI.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Visuals"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlGUI.setLayout(new java.awt.GridLayout(0, 2, 5, 5));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblLogo.setLabelFor(btnLogo);
        lblLogo.setText("Rookie Logo:");
        lblLogo.setMinimumSize(new java.awt.Dimension(100, 14));
        lblLogo.setPreferredSize(new java.awt.Dimension(100, 14));
        pnlGUI.add(lblLogo);

        btnLogo.setSelected(true);
        btnLogo.setText("Visible");
        btnLogo.setMinimumSize(new java.awt.Dimension(50, 20));
        btnLogo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLogoActionPerformed(evt);
            }
        });
        pnlGUI.add(btnLogo);

        lblCurtain.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCurtain.setLabelFor(btnCurtain);
        lblCurtain.setText("Curtain:");
        lblCurtain.setMinimumSize(new java.awt.Dimension(100, 14));
        lblCurtain.setPreferredSize(new java.awt.Dimension(100, 14));
        pnlGUI.add(lblCurtain);

        btnCurtain.setText("Closed");
        btnCurtain.setMinimumSize(new java.awt.Dimension(50, 20));
        btnCurtain.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCurtainActionPerformed(evt);
            }
        });
        pnlGUI.add(btnCurtain);

        pnlButtons.add(pnlGUI, java.awt.BorderLayout.PAGE_END);

        pnlNames.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Names"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlNames.setLayout(new java.awt.BorderLayout(0, 5));

        pnlNameList.setLayout(new java.awt.BorderLayout());

        lstNames.setModel(nameListModel);
        lstNames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstNames.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lstNamesMouseClicked(evt);
            }
        });
        scrlNames.setViewportView(lstNames);

        pnlNameList.add(scrlNames, java.awt.BorderLayout.CENTER);

        pnlNames.add(pnlNameList, java.awt.BorderLayout.CENTER);

        pnlNameButtons.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnSendName.setText("Next Name");
        btnSendName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNextNameActionPerformed(evt);
            }
        });
        pnlNameButtons.add(btnSendName);

        btnHideName.setText("Hide");
        btnHideName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHideNameActionPerformed(evt);
            }
        });
        pnlNameButtons.add(btnHideName);

        pnlNames.add(pnlNameButtons, java.awt.BorderLayout.PAGE_END);

        pnlButtons.add(pnlNames, java.awt.BorderLayout.CENTER);

        pnlContent.add(pnlButtons);

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

        
       
    
    /**
     * Adds a new slave to the list.
     * 
     * @param evt the event that triggered this action
     */
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        String hostname = JOptionPane.showInputDialog(this,
            "Enter the IP address of hostname of the client to add:", 
            "Add new client",
            JOptionPane.PLAIN_MESSAGE);
        if ( (hostname != null) && !hostname.isEmpty() )
        {
            RookieVisualClient slave = new RookieVisualClient(hostname);
            if ( slave.isActive() )
            {
                clients.addElement(slave);
            }
            else
            {
                JOptionPane.showMessageDialog(this, 
                    "Client could not be added!",
                    "Error while adding client",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    
    /***
     * Removes selected slaves from the list.
     * 
     * @param evt event that triggered this action
     */
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
        List selected = lstClients.getSelectedValuesList();
        if ( selected.isEmpty() ) return;
        
        // create message
        String msg = "Do you really want to remove the following clients:\n";
        for ( Object slave : selected )
        {
            msg += ((RookieVisualClient) slave).toString() + "\n";
        }
        // ask user
        int choice = JOptionPane.showConfirmDialog(this,
            msg,
            "Remove clients",
            JOptionPane.YES_NO_OPTION);
        // remove entries
        if ( choice == JOptionPane.YES_OPTION )
        {
            for ( Object object : selected )
            {
                clients.removeElement(object);
            }
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    
    private void btnCurtainActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCurtainActionPerformed
    {//GEN-HEADEREND:event_btnCurtainActionPerformed
        boolean curtainOpen = btnCurtain.isSelected();
        btnCurtain.setText(curtainOpen ? "Open" : "Closed");
        vars.curtainOpen.set(curtainOpen);
        sendUpdate();
    }//GEN-LAST:event_btnCurtainActionPerformed

    
    private void btnLogoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLogoActionPerformed
    {//GEN-HEADEREND:event_btnLogoActionPerformed
        boolean logoVisible = btnLogo.isSelected();
        btnLogo.setText(logoVisible ? "Visible" : "Hidden");
        vars.logoVisible.set(logoVisible);
        if ( logoVisible )
        {
            vars.currentName.set("");
        }
        sendUpdate();
    }//GEN-LAST:event_btnLogoActionPerformed

    
    private void btnNextNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextNameActionPerformed
    {//GEN-HEADEREND:event_btnNextNameActionPerformed
        btnLogo.setSelected(false);
        vars.logoVisible.set(false);
        btnLogo.setText("Hidden");
        if ( lstNames.getSelectedIndex() < 0 )
        {
            lstNames.setSelectedIndex(0);
        }
        else
        {
            int idx = lstNames.getSelectedIndex();
            idx = Math.min(idx + 1, lstNames.getModel().getSize());
            lstNames.setSelectedIndex(idx);
            lstNames.ensureIndexIsVisible(idx);
        }
        vars.currentName.set(lstNames.getSelectedValue().toString());
        sendUpdate();
    }//GEN-LAST:event_btnNextNameActionPerformed

    
    private void btnHideNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHideNameActionPerformed
    {//GEN-HEADEREND:event_btnHideNameActionPerformed
        vars.currentName.set("");
        sendUpdate();
    }//GEN-LAST:event_btnHideNameActionPerformed

    private void lstNamesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lstNamesMouseClicked
    {//GEN-HEADEREND:event_lstNamesMouseClicked
        if ( evt.getClickCount() > 1 )
        {
            // double click on name: show it
            btnLogo.setSelected(false);
            vars.logoVisible.set(false);
            btnLogo.setText("Hidden");
            if ( lstNames.getSelectedIndex() < 0 )
            {
                lstNames.setSelectedIndex(0);
            }
            vars.currentName.set(lstNames.getSelectedValue().toString());
            sendUpdate();
        }
    }//GEN-LAST:event_lstNamesMouseClicked

    
    /**
     * Sends a message only with updated values.
     */
    private void sendUpdate()
    {
        OSCPacket packet  = vars.getUpdatePacket();
        Object[]  targets = lstClients.getSelectedValuesList().toArray();
        if ( targets.length == 0 )
        {
            targets = clients.toArray();
        }
        for ( Object client : targets )
        {
            ((RookieVisualClient) client).sendPacket(packet);
        }
    }
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame app = new RookieVisualsRemoteController();
                app.setLocationRelativeTo(null);
                app.setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JToggleButton btnCurtain;
    private javax.swing.JButton btnHideName;
    private javax.swing.JToggleButton btnLogo;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSendName;
    private javax.swing.JList lstClients;
    private javax.swing.JList lstNames;
    private javax.swing.JPanel pnlNameButtons;
    private javax.swing.JPanel pnlNameList;
    private javax.swing.JPanel pnlSlaveButtons;
    private javax.swing.JScrollPane scrlNames;
    // End of variables declaration//GEN-END:variables

    private final DefaultListModel<RookieVisualClient>  clients;
    private final RookieVisualsVariables                vars;
    private final DefaultListModel                      nameListModel;

    
    /**
     * Class for encapsulating Rookie Visual Clients, 
     * their state, and the output for the list model.
     */
    private class RookieVisualClient
    {
        public RookieVisualClient(String hostname)
        {
            this.address = null; 
            this.port    = null;
            try
            {
                this.address = InetAddress.getByName(hostname);
                port = new OSCPortOut(address);
            }
            catch ( UnknownHostException e )
            {
                System.err.println("Could not add client");
            }
            catch ( SocketException e )
            {
                System.err.println("Could not open OSC port");
            }
        }
        
        public boolean isActive()
        {
            return (port != null);
        }
        
        public void sendPacket(OSCPacket packet)
        {
            try
            {
                port.send(packet);
            }
            catch (IOException e)
            {
                System.err.println("Could not send packet to client '" + address + "'");
            }
        }
        
        @Override
        public String toString()
        {
            return address.getHostAddress();
        }
        
        private InetAddress address;
        private OSCPortOut  port;
    }
}
