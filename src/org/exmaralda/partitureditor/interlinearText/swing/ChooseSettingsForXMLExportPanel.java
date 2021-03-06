/*
 * ChooseSettingsForXMLExportPanel.java
 *
 * Created on 27. Juni 2002, 09:39
 */

package org.exmaralda.partitureditor.interlinearText.swing;


/**
 *
 * @author  Thomas
 */
public class ChooseSettingsForXMLExportPanel extends javax.swing.JPanel {

    public static final short USE_PRINT_SETTINGS = 1;
    public static final short USE_RTF_SETTINGS = 2;
    public static final short USE_HTML_SETTINGS = 3;

    /** Creates new form ChooseSettingsForXMLExportPanel */
    public ChooseSettingsForXMLExportPanel() {
        initComponents();
        org.exmaralda.common.helpers.Internationalizer.internationalizeComponentToolTips(this);
    }

    public short getSelection(){
        if (usePrintSettingsRadioButton.isSelected()) return USE_PRINT_SETTINGS;
        else if (useRTFSettingsRadioButton.isSelected()) return USE_RTF_SETTINGS;
        else if (useHTMLSettingsRadioButton.isSelected()) return USE_HTML_SETTINGS;
        return 0;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        usePrintSettingsRadioButton = new javax.swing.JRadioButton();
        useRTFSettingsRadioButton = new javax.swing.JRadioButton();
        useHTMLSettingsRadioButton = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Settings options"));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        buttonGroup1.add(usePrintSettingsRadioButton);
        usePrintSettingsRadioButton.setSelected(true);
        usePrintSettingsRadioButton.setText("Use print settings");
        add(usePrintSettingsRadioButton);

        buttonGroup1.add(useRTFSettingsRadioButton);
        useRTFSettingsRadioButton.setText("Use RTF settings");
        add(useRTFSettingsRadioButton);

        buttonGroup1.add(useHTMLSettingsRadioButton);
        useHTMLSettingsRadioButton.setText("Use HTML settings");
        add(useHTMLSettingsRadioButton);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton useHTMLSettingsRadioButton;
    private javax.swing.JRadioButton usePrintSettingsRadioButton;
    private javax.swing.JRadioButton useRTFSettingsRadioButton;
    // End of variables declaration//GEN-END:variables

}
