import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HealthcareManagementApp {
    private static final String PATIENT_FILE = "patient_records.ser";

    // Patient Class
    private static class Patient implements Serializable {
        String patientId;
        String name;
        String age;
        String contactNumber;
        String disease;
        String lastVisitDate;
        String nextVisitDate;
        
        // New health-specific fields
        String bpLevel;
        String sugarLevel;
        List<Medication> medications;

        Patient(String patientId, String name, String age, String contactNumber, 
                String disease, String bpLevel, String sugarLevel, 
                List<Medication> medications, String lastVisitDate, String nextVisitDate) {
            this.patientId = patientId;
            this.name = name;
            this.age = age;
            this.contactNumber = contactNumber;
            this.disease = disease;
            this.bpLevel = bpLevel;
            this.sugarLevel = sugarLevel;
            this.medications = medications;
            this.lastVisitDate = lastVisitDate;
            this.nextVisitDate = nextVisitDate;
        }
    }

    // Medication Class
    private static class Medication implements Serializable {
        String name;
        String dose;
        String frequency;
        String additionalInstructions;

        Medication(String name, String dose, String frequency, String additionalInstructions) {
            this.name = name;
            this.dose = dose;
            this.frequency = frequency;
            this.additionalInstructions = additionalInstructions;
        }
    }
    // Login Page Class
    private static class LoginPage extends JFrame {
        public LoginPage() {
            setTitle("Healthcare Management System");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
            // Use BorderLayout for better centering
            setLayout(new BorderLayout());

            // Create a panel to center login components
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;

            // Username components
            gbc.gridx = 0;
            gbc.gridy = 0;
            centerPanel.add(new JLabel("Username:"), gbc);
        
            gbc.gridy = 1;
            JTextField usernameField = new JTextField(20); // Set a reasonable width
            centerPanel.add(usernameField, gbc);

            // Password components
            gbc.gridy = 2;
            centerPanel.add(new JLabel("Password:"), gbc);
        
            gbc.gridy = 3;
            JPasswordField passwordField = new JPasswordField(20); // Set a reasonable width
            centerPanel.add(passwordField, gbc);

            // Login Button
            gbc.gridy = 4;
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(e -> {
                if ("admin".equals(usernameField.getText()) && "password".equals(new String(passwordField.getPassword()))) {
                    new DashboardPage();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials");
                }
            });
            centerPanel.add(loginButton, gbc);

            // Add centered panel to the center of BorderLayout
            add(centerPanel, BorderLayout.CENTER);

            setLocationRelativeTo(null);
        }
    }

    private static class Reminder implements Serializable {
        String patientId;
        String patientName;
        Date reminderDate;
        String reminderType; // "VISIT" or "MEDICATION"
        String description;
        boolean isCompleted;

        Reminder(String patientId, String patientName, Date reminderDate, 
                String reminderType, String description) {
            this.patientId = patientId;
            this.patientName = patientName;
            this.reminderDate = reminderDate;
            this.reminderType = reminderType;
            this.description = description;
            this.isCompleted = false;
        }
    }

    private static class DashboardPage extends JFrame {
        private List<Patient> patientList;
        private DefaultTableModel tableModel;
        private JTable patientTable;
        private Date loginDate;
        private List<Reminder> reminderList;
        private Timer notificationTimer;
        private JPanel notificationPanel;
        private static final String REMINDER_FILE = "reminders.ser";

        public DashboardPage() {
            setTitle("Patient Dashboard");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            loginDate = new Date();
            
            // Load existing patient records
            patientList = loadPatientRecords();

            // Create top panel for date and logout
            JPanel topPanel = new JPanel(new BorderLayout());
            
            // Date label
            JLabel dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(loginDate));
            dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
            topPanel.add(dateLabel, BorderLayout.WEST);

            // Logout button
            JButton logoutButton = new JButton("Logout");
            logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
            logoutButton.addActionListener(e -> {
                savePatientRecords(patientList);
                new LoginPage().setVisible(true);
                dispose();
            });
            topPanel.add(logoutButton, BorderLayout.EAST);

            add(topPanel, BorderLayout.NORTH);

            // Modify table model columns
            String[] columnNames = {"Patient ID", "Name", "Disease", "Contact", "Last Visit", "Next Visit"};
            tableModel = new DefaultTableModel(columnNames, 0);
            
            // Populate table with existing patients
            for (Patient patient : patientList) {
                tableModel.addRow(new Object[]{
                    patient.patientId,
                    patient.name,
                    patient.disease,
                    patient.contactNumber,
                    patient.lastVisitDate,
                    patient.nextVisitDate
                });
            }

            patientTable = new JTable(tableModel);

            JScrollPane tableScrollPane = new JScrollPane(patientTable);
            add(tableScrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton registerButton = new JButton("Register Patient");
            registerButton.addActionListener(e -> openPatientRegistrationDialog());
            buttonPanel.add(registerButton);

            JButton viewDetailsButton = new JButton("View Details");
            viewDetailsButton.addActionListener(e -> viewPatientDetails());
            buttonPanel.add(viewDetailsButton);

            JButton updateButton = new JButton("Update Patient");
            updateButton.addActionListener(e -> updatePatientDetails());
            buttonPanel.add(updateButton);

            // Add Remove Patient Button
            JButton removeButton = new JButton("Remove Patient");
            removeButton.addActionListener(e -> removeSelectedPatient());
            buttonPanel.add(removeButton);

            add(buttonPanel, BorderLayout.SOUTH);

            // Initialize reminder list
            reminderList = loadReminders();
            
            // Create notification panel
            notificationPanel = new JPanel();
            notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
            notificationPanel.setBorder(BorderFactory.createTitledBorder("Notifications & Reminders"));
            
            JScrollPane notificationScroll = new JScrollPane(notificationPanel);
            notificationScroll.setPreferredSize(new Dimension(250, 0));
            add(notificationScroll, BorderLayout.EAST);

            // Add Reminder button to button panel
            JButton addReminderButton = new JButton("Add Reminder");
            addReminderButton.addActionListener(e -> openAddReminderDialog());
            buttonPanel.add(addReminderButton);

            // Start notification checker
            startNotificationChecker();
            
            // Load and display current notifications
            updateNotificationPanel();

            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void startNotificationChecker() {
            notificationTimer = new Timer(true);
            notificationTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> checkReminders());
                }
            }, 0, 60000); // Check every minute
        }

        private void checkReminders() {
            Date now = new Date();
            boolean updatedReminders = false;
            
            for (Reminder reminder : reminderList) {
                if (!reminder.isCompleted && reminder.reminderDate.before(now)) {
                    // Show notification
                    String message = String.format("%s: %s - %s", 
                        reminder.reminderType, 
                        reminder.patientName, 
                        reminder.description);
                    
                    showNotification(message);
                    updatedReminders = true;
                }
            }
            
            if (updatedReminders) {
                updateNotificationPanel();
                saveReminders(reminderList);
            }
        }

        private void showNotification(String message) {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Reminder",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        private void updateNotificationPanel() {
            notificationPanel.removeAll();
            Date now = new Date();
            
            // Sort reminders by date
            List<Reminder> sortedReminders = new ArrayList<>(reminderList);
            sortedReminders.sort((r1, r2) -> r1.reminderDate.compareTo(r2.reminderDate));
            
            for (Reminder reminder : sortedReminders) {
                if (!reminder.isCompleted) {
                    JPanel reminderItemPanel = createReminderPanel(reminder);
                    notificationPanel.add(reminderItemPanel);
                    notificationPanel.add(Box.createVerticalStrut(5));
                }
            }
            
            notificationPanel.revalidate();
            notificationPanel.repaint();
        }

        private JPanel createReminderPanel(Reminder reminder) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEtchedBorder());
            
            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reminder.reminderDate);
            JLabel label = new JLabel(String.format("<html><b>%s</b><br>%s<br>%s<br>%s</html>",
                reminder.reminderType,
                reminder.patientName,
                reminder.description,
                dateStr
            ));
            panel.add(label, BorderLayout.CENTER);
            
            JButton completeButton = new JButton("Complete");
            completeButton.addActionListener(e -> {
                reminder.isCompleted = true;
                updateNotificationPanel();
                saveReminders(reminderList);
            });
            panel.add(completeButton, BorderLayout.EAST);
            
            return panel;
        }

        private void openAddReminderDialog() {
            JDialog dialog = new JDialog(this, "Add Reminder", true);
            dialog.setLayout(new GridLayout(0, 2, 10, 10));
            dialog.setSize(400, 300);

            // Patient selection
            JComboBox<String> patientCombo = new JComboBox<>();
            for (Patient patient : patientList) {
                patientCombo.addItem(patient.patientId + " - " + patient.name);
            }

            // Reminder type
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"VISIT", "MEDICATION"});
            
            // Date and time selection
            JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
            dateSpinner.setEditor(dateEditor);
            dateSpinner.setValue(new Date());

            // Description
            JTextField descriptionField = new JTextField();

            // Add components
            dialog.add(new JLabel("Patient:"));
            dialog.add(patientCombo);
            dialog.add(new JLabel("Type:"));
            dialog.add(typeCombo);
            dialog.add(new JLabel("Date & Time:"));
            dialog.add(dateSpinner);
            dialog.add(new JLabel("Description:"));
            dialog.add(descriptionField);

            // Save button
            JButton saveButton = new JButton("Save Reminder");
            saveButton.addActionListener(e -> {
                String[] patientInfo = ((String)patientCombo.getSelectedItem()).split(" - ");
                Reminder newReminder = new Reminder(
                    patientInfo[0],
                    patientInfo[1],
                    (Date)dateSpinner.getValue(),
                    (String)typeCombo.getSelectedItem(),
                    descriptionField.getText()
                );
                reminderList.add(newReminder);
                updateNotificationPanel();
                saveReminders(reminderList);
                dialog.dispose();
            });
            dialog.add(saveButton);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        private void saveReminders(List<Reminder> reminders) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REMINDER_FILE))) {
                oos.writeObject(reminders);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving reminders");
            }
        }

        @SuppressWarnings("unchecked")
        private List<Reminder> loadReminders() {
            try {
                File file = new File(REMINDER_FILE);
                if (file.exists()) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        return (List<Reminder>) ois.readObject();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading reminders");
            }
            return new ArrayList<>();
        }

        // Method to save patient records to file
        private void savePatientRecords(List<Patient> patients) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATIENT_FILE))) {
                oos.writeObject(patients);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving patient records");
            }
        }

        // Override dispose to clean up timer
        @Override
        public void dispose() {
            if (notificationTimer != null) {
                notificationTimer.cancel();
            }
            super.dispose();
        }

        // Method to load patient records from file
        private List<Patient> loadPatientRecords() {
            try {
                File file = new File(PATIENT_FILE);
                if (file.exists()) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        return (List<Patient>) ois.readObject();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading patient records");
            }
            return new ArrayList<>();
        }

        // Validate input fields method
        private boolean validateInputFields(JTextField patientIdField, JTextField nameField, JTextField contactField) {
            if (patientIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Patient ID cannot be empty");
                return false;
            }
            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty");
                return false;
            }
            if (contactField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Contact number cannot be empty");
                return false;
            }
            return true;
        }

        private void openPatientRegistrationDialog() {
            JDialog dialog = new JDialog(this, "Register New Patient", true);
            dialog.setLayout(new GridLayout(0, 2, 10, 10));
            dialog.setSize(600, 500);

            // Basic Patient Information
            JTextField patientIdField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField contactField = new JTextField();
            JTextField diseaseField = new JTextField();
            
            // Health Specific Fields
            JTextField bpLevelField = new JTextField();
            JTextField sugarLevelField = new JTextField();
            JTextField lastVisitDateField = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(loginDate));
            JTextField nextVisitDateField = new JTextField();

            // Recent Surgeries Field
            JTextField recentSurgeriesField = new JTextField();

            // Medication Panel
            List<Medication> medications = new ArrayList<>();
            
            JButton addMedicationButton = new JButton("Add Medication");
            addMedicationButton.addActionListener(e -> {
                JTextField medNameField = new JTextField();
                JTextField medDoseField = new JTextField();
                JTextField medFrequencyField = new JTextField();

                int option = JOptionPane.showConfirmDialog(dialog, 
                    new Object[]{
                        "Medication Name:", medNameField,
                        "Dose:", medDoseField,
                        "Frequency:", medFrequencyField
                    }, 
                    "Add Medication", 
                    JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    Medication med = new Medication(
                        medNameField.getText(), 
                        medDoseField.getText(), 
                        medFrequencyField.getText(), 
                        ""  // Empty additional instructions
                    );
                    medications.add(med);
                }
            });

            // Add components to dialog
            dialog.add(new JLabel("Patient ID:"));
            dialog.add(patientIdField);
            dialog.add(new JLabel("Name:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Age:"));
            dialog.add(ageField);
            dialog.add(new JLabel("Contact Number:"));
            dialog.add(contactField);
            dialog.add(new JLabel("Disease:"));
            dialog.add(diseaseField);
            dialog.add(new JLabel("BP Level:"));
            dialog.add(bpLevelField);
            dialog.add(new JLabel("Sugar Level:"));
            dialog.add(sugarLevelField);
            dialog.add(new JLabel("Last Visit Date:"));
            dialog.add(lastVisitDateField);
            dialog.add(new JLabel("Next Visit Date:"));
            dialog.add(nextVisitDateField);
            dialog.add(new JLabel("Recent Surgeries:"));
            dialog.add(recentSurgeriesField);
            dialog.add(new JLabel("Medications:"));
            dialog.add(addMedicationButton);

            // Save Button
            JButton saveButton = new JButton("Save Patient");
            saveButton.addActionListener(e -> {
                if (validateInputFields(patientIdField, nameField, contactField)) {
                    // Update Medication class constructor to match removed field
                    Patient newPatient = new Patient(
                        patientIdField.getText(),
                        nameField.getText(),
                        ageField.getText(),
                        contactField.getText(),
                        diseaseField.getText(),
                        bpLevelField.getText(),
                        sugarLevelField.getText(),
                        medications,
                        lastVisitDateField.getText(),
                        nextVisitDateField.getText()
                    );
                    patientList.add(newPatient);
                    tableModel.addRow(new Object[]{
                        newPatient.patientId,
                        newPatient.name,
                        newPatient.disease,
                        newPatient.contactNumber,
                        newPatient.lastVisitDate,
                        newPatient.nextVisitDate
                    });
                    dialog.dispose();
                }
            });
            dialog.add(saveButton);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }

        private void viewPatientDetails() {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a patient");
                return;
            }

            String patientId = (String) tableModel.getValueAt(selectedRow, 0);
            Patient patient = patientList.stream()
                .filter(p -> p.patientId.equals(patientId))
                .findFirst()
                .orElse(null);

            if (patient != null) {
                JDialog detailsDialog = new JDialog(this, "Detailed Patient Information", true);
                detailsDialog.setLayout(new BorderLayout());
                detailsDialog.setSize(500, 400);

                // Patient Basic Info Panel
                JPanel basicInfoPanel = new JPanel(new GridLayout(0, 2));
                basicInfoPanel.add(new JLabel("Patient ID:"));
                basicInfoPanel.add(new JLabel(patient.patientId));
                basicInfoPanel.add(new JLabel("Name:"));
                basicInfoPanel.add(new JLabel(patient.name));
                basicInfoPanel.add(new JLabel("BP Level:"));
                basicInfoPanel.add(new JLabel(patient.bpLevel));
                basicInfoPanel.add(new JLabel("Sugar Level:"));
                basicInfoPanel.add(new JLabel(patient.sugarLevel));

                // Medications Table
                String[] medColumns = {"Medication", "Dose", "Frequency", "Instructions"};
                DefaultTableModel medTableModel = new DefaultTableModel(medColumns, 0);
                patient.medications.forEach(med -> 
                    medTableModel.addRow(new Object[]{
                        med.name, med.dose, med.frequency, med.additionalInstructions
                    })
                );
                JTable medicationTable = new JTable(medTableModel);

                detailsDialog.add(basicInfoPanel, BorderLayout.NORTH);
                detailsDialog.add(new JScrollPane(medicationTable), BorderLayout.CENTER);

                detailsDialog.setLocationRelativeTo(this);
                detailsDialog.setVisible(true);
            }
        }

        private void updatePatientDetails() {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a patient to update");
                return;
            }

            String patientId = (String) tableModel.getValueAt(selectedRow, 0);
            Patient patient = patientList.stream()
                .filter(p -> p.patientId.equals(patientId))
                .findFirst()
                .orElse(null);

            if (patient != null) {
                JDialog updateDialog = new JDialog(this, "Update Patient", true);
                updateDialog.setLayout(new GridLayout(0, 2, 10, 10));
                updateDialog.setSize(600, 500);

                // Next Visit Date Update
                JTextField nextVisitField = new JTextField(patient.nextVisitDate);
                updateDialog.add(new JLabel("Next Visit Date:"));
                updateDialog.add(nextVisitField);

                // Medications Update
                JButton updateMedicationsButton = new JButton("Update Medications");
                JTable medicationTable = createMedicationTable(patient);
                JScrollPane medicationScrollPane = new JScrollPane(medicationTable);
                
                updateMedicationsButton.addActionListener(e -> {
                    JDialog medUpdateDialog = new JDialog(this, "Update Medications", true);
                    medUpdateDialog.setLayout(new BorderLayout());
                    medUpdateDialog.setSize(500, 400);

                    // Medication Table
                    medUpdateDialog.add(new JScrollPane(medicationTable), BorderLayout.CENTER);

                    // Buttons for adding/removing medications
                    JPanel buttonPanel = new JPanel();
                    JButton addMedButton = new JButton("Add Medication");
                    addMedButton.addActionListener(addE -> {
                        JTextField medNameField = new JTextField();
                        JTextField medDoseField = new JTextField();
                        JTextField medFreqField = new JTextField();
                        JTextField medInstrField = new JTextField();

                        int option = JOptionPane.showConfirmDialog(medUpdateDialog, 
                            new Object[]{
                                "Medication Name:", medNameField,
                                "Dose:", medDoseField,
                                "Frequency:", medFreqField,
                                "Instructions:", medInstrField
                            }, 
                            "Add Medication", 
                            JOptionPane.OK_CANCEL_OPTION);

                        if (option == JOptionPane.OK_OPTION) {
                            Medication newMed = new Medication(
                                medNameField.getText(),
                                medDoseField.getText(),
                                medFreqField.getText(),
                                medInstrField.getText()
                            );
                            patient.medications.add(newMed);
                            DefaultTableModel model = (DefaultTableModel)medicationTable.getModel();
                            model.addRow(new Object[]{
                                newMed.name, 
                                newMed.dose, 
                                newMed.frequency, 
                                newMed.additionalInstructions
                            });
                        }
                    });

                    JButton removeMedButton = new JButton("Remove Medication");
                    removeMedButton.addActionListener(removeE -> {
                        int selectedMedRow = medicationTable.getSelectedRow();
                        if (selectedMedRow != -1) {
                            DefaultTableModel model = (DefaultTableModel)medicationTable.getModel();
                            patient.medications.remove(selectedMedRow);
                            model.removeRow(selectedMedRow);
                        } else {
                            JOptionPane.showMessageDialog(medUpdateDialog, "Please select a medication to remove");
                        }
                    });

                    buttonPanel.add(addMedButton);
                    buttonPanel.add(removeMedButton);
                    medUpdateDialog.add(buttonPanel, BorderLayout.SOUTH);

                    medUpdateDialog.setLocationRelativeTo(this);
                    medUpdateDialog.setVisible(true);
                });

                updateDialog.add(new JLabel("Medications:"));
                updateDialog.add(updateMedicationsButton);
                updateDialog.add(medicationScrollPane);

                JButton saveButton = new JButton("Save Changes");
                saveButton.addActionListener(e -> {
                    patient.nextVisitDate = nextVisitField.getText();
                    
                    // Update table
                    tableModel.setValueAt(patient.nextVisitDate, selectedRow, 5);
                    
                    updateDialog.dispose();
                });
                updateDialog.add(saveButton);

                updateDialog.setLocationRelativeTo(this);
                updateDialog.setVisible(true);
            }
        }

        private void removeSelectedPatient() {
             int selectedRow = patientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a patient to remove");
                return;
            }

            String patientId = (String) tableModel.getValueAt(selectedRow, 0);
    
            // Confirm removal
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to remove this patient?", 
                "Confirm Removal", 
                JOptionPane.YES_NO_OPTION
            );
    
            if (confirm == JOptionPane.YES_OPTION) {
                // Remove from patient list
                patientList.removeIf(p -> p.patientId.equals(patientId));
        
                // Remove from table model
                tableModel.removeRow(selectedRow);
            }
        }

        // Helper method to create medication table
        private JTable createMedicationTable(Patient patient) {
            String[] medColumns = {"Medication", "Dose", "Frequency", "Instructions"};
            DefaultTableModel medTableModel = new DefaultTableModel(medColumns, 0);
            
            patient.medications.forEach(med -> 
                medTableModel.addRow(new Object[]{
                    med.name, med.dose, med.frequency, med.additionalInstructions
                })
            );
            
            return new JTable(medTableModel);
        }
    }


    // Main method remains the same
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new LoginPage().setVisible(true);
        });
    }
}
