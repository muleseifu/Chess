package Chess.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * SettingsDialog – allows the user to configure sound, board theme, and timer.
 * Settings are persisted to Settings.properties in the working directory.
 */
public class SettingsDialog extends JDialog {

    private static final String SETTINGS_FILE = "Settings.properties";

    private JCheckBox soundToggle;
    private JComboBox<String> themeSelector;
    private JCheckBox timerToggle;
    private JSpinner  timerMinutes;

    public SettingsDialog(JFrame parent) {
        super(parent, "Settings", true);
        setSize(340, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
        loadSettings();
    }

    private void initUI() {
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        content.setBackground(new Color(50, 50, 50));
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        // --- Sound ---
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(label("Sound Effects:"), gbc);
        soundToggle = new JCheckBox();
        soundToggle.setBackground(new Color(50, 50, 50));
        gbc.gridx = 1;
        content.add(soundToggle, gbc);

        // --- Theme ---
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(label("Board Theme:"), gbc);
        themeSelector = new JComboBox<>(new String[]{"Classic", "Wood", "Dark"});
        gbc.gridx = 1;
        content.add(themeSelector, gbc);

        // --- Timer ---
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(label("Enable Timer:"), gbc);
        timerToggle = new JCheckBox();
        timerToggle.setBackground(new Color(50, 50, 50));
        gbc.gridx = 1;
        content.add(timerToggle, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        content.add(label("Minutes per Player:"), gbc);
        timerMinutes = new JSpinner(new SpinnerNumberModel(10, 1, 60, 1));
        gbc.gridx = 1;
        content.add(timerMinutes, gbc);

        // --- Buttons ---
        JButton apply  = new JButton("Apply");
        JButton cancel = new JButton("Cancel");
        apply.setBackground(new Color(70, 130, 180));
        apply.setForeground(Color.WHITE);
        cancel.setBackground(new Color(100, 100, 100));
        cancel.setForeground(Color.WHITE);

        apply.addActionListener(e -> { applySettings(); dispose(); });
        cancel.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(50, 50, 50));
        btnPanel.add(cancel);
        btnPanel.add(apply);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(50, 50, 50));
        add(content, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        return lbl;
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /** Persist settings to Settings.properties and refresh GUI theme. */
    public void applySettings() {
        Properties props = new Properties();
        props.setProperty("sound",   String.valueOf(soundToggle.isSelected()));
        props.setProperty("theme",   (String) themeSelector.getSelectedItem());
        props.setProperty("timer",   String.valueOf(timerToggle.isSelected()));
        props.setProperty("minutes", timerMinutes.getValue().toString());

        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            props.store(fos, "Chess Settings");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Could not save settings: " + ex.getMessage(),
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Read settings from Settings.properties. */
    public void loadSettings() {
        Properties props = new Properties();
        File f = new File(SETTINGS_FILE);
        if (!f.exists()) return;

        try (FileInputStream fis = new FileInputStream(f)) {
            props.load(fis);
            soundToggle.setSelected(Boolean.parseBoolean(props.getProperty("sound", "true")));
            themeSelector.setSelectedItem(props.getProperty("theme", "Classic"));
            timerToggle.setSelected(Boolean.parseBoolean(props.getProperty("timer", "false")));
            timerMinutes.setValue(Integer.parseInt(props.getProperty("minutes", "10")));
        } catch (IOException | NumberFormatException ex) {
            // Use defaults silently
        }
    }

    // -------------------------------------------------------------------------
    // Accessors (for applying theme to board, etc.)
    // -------------------------------------------------------------------------

    public boolean isSoundEnabled()    { return soundToggle.isSelected(); }
    public String  getTheme()          { return (String) themeSelector.getSelectedItem(); }
    public boolean isTimerEnabled()    { return timerToggle.isSelected(); }
    public int     getTimerMinutes()   { return (int) timerMinutes.getValue(); }
}
