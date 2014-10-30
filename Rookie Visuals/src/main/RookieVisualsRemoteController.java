package main;

import analyser.AudioInput;
import analyser.AudioManager;
import analyser.SpectrumAnalyser;
import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCParameter;
import com.illposed.osc.OSCPortOut;
import ddf.minim.Minim;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
     * 
     * @param input the audio input to use for spectrum analysis
     */
    public RookieVisualsRemoteController(AudioInput input)
    {
        vars = new RookieVisualsVariables();
        
        clients = new DefaultListModel<RookieVisualClient>();
        loadClientList();

        nameListModel = new DefaultListModel();
        loadNameList();
        
        initComponents();
        
        // initialise Audio
        minim = new Minim(null);
        minim.setInputMixer(input.getMixer());
        
        // create audio analyser
        audioAnalyser = new SpectrumAnalyser(30, 20, 2, 1);
        // attach input to audio analyser
        audioAnalyser.attachToAudio(minim.getLineIn());
        
        initSpectrumSliders();
        
        audioAnalyser.registerListener(new SpectrumListener());
        
        sendUpdate(true); // initialise clients
    }

    
    private void loadNameList()
    {
        try
        {
            File listFile = new File("./names.txt");
            BufferedReader br = new BufferedReader(new FileReader(listFile));
            while ( br.ready() )
            {
                String name = br.readLine();
                if ( !name.isEmpty() )
                {
                    nameListModel.addElement(name);
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

    
    private void loadClientList()
    {
        clients.addElement(new RookieVisualClient("localhost"));
        try
        {
            File listFile = new File("./clients.txt");
            BufferedReader br = new BufferedReader(new FileReader(listFile));
            while ( br.ready() )
            {
                String address = br.readLine();
                if ( !address.isEmpty() )
                {
                    clients.addElement(new RookieVisualClient(address));
                }
            }
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog(this, 
                    "Could not load client address list.",
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        javax.swing.JPanel pnlAudio = new javax.swing.JPanel();
        pnlFrequencies = new javax.swing.JPanel();
        javax.swing.JPanel pnlAudioControls = new javax.swing.JPanel();
        javax.swing.JLabel lblVolume = new javax.swing.JLabel();
        sldVolume = new javax.swing.JSlider();
        javax.swing.JLabel lblDecay = new javax.swing.JLabel();
        sldDecay = new javax.swing.JSlider();

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

        pnlAudio.setLayout(new java.awt.BorderLayout());

        pnlFrequencies.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Frequency Spectrum"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnlFrequencies.setLayout(new java.awt.GridLayout(1, 0));
        pnlAudio.add(pnlFrequencies, java.awt.BorderLayout.CENTER);

        pnlAudioControls.setLayout(new java.awt.GridBagLayout());

        lblVolume.setLabelFor(sldVolume);
        lblVolume.setText("Volume:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        pnlAudioControls.add(lblVolume, gridBagConstraints);

        sldVolume.setMajorTickSpacing(50);
        sldVolume.setMinorTickSpacing(5);
        sldVolume.setPaintLabels(true);
        sldVolume.setPaintTicks(true);
        sldVolume.setToolTipText("");
        sldVolume.setValue(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        pnlAudioControls.add(sldVolume, gridBagConstraints);

        lblDecay.setLabelFor(sldDecay);
        lblDecay.setText("Decay Time:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 6);
        pnlAudioControls.add(lblDecay, gridBagConstraints);

        sldDecay.setMajorTickSpacing(50);
        sldDecay.setMaximum(99);
        sldDecay.setMinimum(75);
        sldDecay.setMinorTickSpacing(5);
        sldDecay.setToolTipText("");
        sldDecay.setValue(99);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlAudioControls.add(sldDecay, gridBagConstraints);

        pnlAudio.add(pnlAudioControls, java.awt.BorderLayout.PAGE_END);

        pnlContent.add(pnlAudio);

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

       
    private void initSpectrumSliders()
    {
        sldSpectrum = new JSlider[vars.spectrum.length];
        for ( int i = 0 ; i < sldSpectrum.length ; i++ )
        {
            JSlider sld = new JSlider();
            sld.setOrientation(javax.swing.JSlider.VERTICAL);
            sldSpectrum[i] = sld;
            sld.addChangeListener(new SliderListener(vars.spectrum[i]));
            pnlFrequencies.add(sld);
        }
    }
       
    
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
        sendUpdate(true); // initialise new client
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
        btnCurtain.setText(curtainOpen ? "Active" : "Closed");
        vars.curtainOpen.set(curtainOpen);
        sendUpdate(false);
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
        sendUpdate(false);
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
        sendUpdate(false);
    }//GEN-LAST:event_btnNextNameActionPerformed

    
    private void btnHideNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHideNameActionPerformed
    {//GEN-HEADEREND:event_btnHideNameActionPerformed
        vars.currentName.set("");
        sendUpdate(false);
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
            sendUpdate(false);
        }
    }//GEN-LAST:event_lstNamesMouseClicked

    
    /**
     * Sends a message only with updated values.
     * 
     * @param fullUpdate <code>true</code> for sending all variable values,
     *                   <code>false</code> for only sending incremetal updates
     */
    private void sendUpdate(boolean fullUpdate)
    {
        OSCPacket packet  = fullUpdate ? vars.getFullPacket() : vars.getUpdatePacket();
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
        AudioInput input = RookieVisuals.selectAudioInput();
        if ( input != null )
        {
            JFrame app = new RookieVisualsRemoteController(input);
            app.setLocationRelativeTo(null);
            app.setVisible(true);
            while ( app.isVisible() ) 
            { 
                try
                {
                    Thread.sleep(1000);
                } 
                catch (InterruptedException ex)
                {
                    // ignore
                }
            }
        }
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
    private javax.swing.JPanel pnlFrequencies;
    private javax.swing.JPanel pnlNameButtons;
    private javax.swing.JPanel pnlNameList;
    private javax.swing.JPanel pnlSlaveButtons;
    private javax.swing.JScrollPane scrlNames;
    private javax.swing.JSlider sldDecay;
    private javax.swing.JSlider sldVolume;
    // End of variables declaration//GEN-END:variables

    private final DefaultListModel<RookieVisualClient>  clients;
    private final RookieVisualsVariables                vars;
    private final DefaultListModel                      nameListModel;

    // live audio input
    private final Minim             minim;
    private final SpectrumAnalyser  audioAnalyser;
    private       JSlider[]         sldSpectrum;
    
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
    
    
    private class SliderListener implements ChangeListener
    {
        public SliderListener(OSCParameter<Float> parameter)
        {
            this.parameter = parameter;
        }
        
        @Override
        public void stateChanged(ChangeEvent e)
        {
            JSlider sld = (JSlider) e.getSource();
            parameter.set(sld.getValue() / 100.0f);
        }
        
        private final OSCParameter<Float> parameter;
    }
    
    
    private class SpectrumListener implements SpectrumAnalyser.Listener
    {
        public SpectrumListener()
        {
            final int n = vars.spectrum.length;
            lastValue = new float[n];
            sum       = new float[n];
            max       = new float[n]; 
            count     = new int[n];
            // initialise
            for (int i = 0; i < n; i++)
            {
                lastValue[i] = 0;
                max[i]       = 1;                
            }
        }
        
        
        @Override
        public void analysisUpdated(SpectrumAnalyser analyser)
        {
            int varCount  = sldSpectrum.length;
            int specCount = audioAnalyser.getSpectrumBandCount();
            
            // reset
            for ( int i = 0; i < sum.length; i++ )
            {
                sum[i]   = 0;
                max[i]  *= 0.999f;
                count[i] = 0;
            }
            
            // accumulate
            for ( int i = 0 ; i < specCount ; i++ )
            {
                int idx = i * varCount / specCount;
                sum[idx] += audioAnalyser.getSpectrumInfo(0).intensity[i];
                count[idx]++;
            }
                
            // save to OSC vars
            for ( int i = 0; i < sum.length; i++ )
            {
                float value = sum[i] / count[i];
                value *= sldVolume.getValue() / 100.0f;
                max[i] = Math.min(1.0f, Math.max(1.0f, Math.max(value, max[i])));
                lastValue[i] = Math.max(lastValue[i] * (sldDecay.getValue() / 100.0f), value);
                int   intValue = (int) (100 * lastValue[i] / max[i]);
                sldSpectrum[i].setValue(intValue);
            }
            
            sendUpdate(false);
        }
        
        private final float[] lastValue;
        private final float[] sum;
        private final float[] max;
        private final int[]   count;
    }
    
}
