package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class DNASequenceApp extends JFrame implements ActionListener {

    // input components
    private JTextField idField;
    private JComboBox<String> nameBox;
    private JTextField seqField;
    private JTextField chromoField;

    // buttons for actions
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton saveButton;
    private JButton searchButton;

    // table and table model to display data
    private JTable table;
    private DefaultTableModel model;

    // constructor to build the interface
    public DNASequenceApp() {
        setTitle("DNA Sequence Management System");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // input panel with grid layout
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Sequence Details"));

        inputPanel.add(new JLabel("Sequence ID"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Organism Name"));
        String[] organisms = {
            "Homo sapiens",
            "Escherichia coli",
            "Drosophila melanogaster",
            "Mus musculus",
            "Saccharomyces cerevisiae"
        };
        nameBox = new JComboBox<>(organisms);
        inputPanel.add(nameBox);

        inputPanel.add(new JLabel("Raw Sequence"));
        seqField = new JTextField();
        inputPanel.add(seqField);

        inputPanel.add(new JLabel("Chromosome Number"));
        chromoField = new JTextField();
        inputPanel.add(chromoField);

        // button panel with flow layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        saveButton = new JButton("Save");
        searchButton = new JButton("Search");

        // register action listeners
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);
        saveButton.addActionListener(this);
        searchButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(searchButton);

        // top container holding inputs and buttons
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // table to show records
        String[] columns = {
            "Sequence ID", "Organism Name", "Raw Sequence", "Chromosome", "Length", "GC Content (%)"
        };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("DNA Records"));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // load saved records from text file at startup
        loadDataFromFile();

        // click on table row to fill inputs
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameBox.setSelectedItem(model.getValueAt(row, 1).toString());
                    seqField.setText(model.getValueAt(row, 2).toString());
                    chromoField.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    // handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addRecord();
        } else if (e.getSource() == deleteButton) {
            deleteRecord();
        } else if (e.getSource() == updateButton) {
            updateRecord();
        } else if (e.getSource() == saveButton) {
            saveDataToFile(true);
        } else if (e.getSource() == searchButton) {
            searchRecord();
        }
    }

    // load records from text file at the start of project
    private void loadDataFromFile() {
        File file = new File("dna_sequences.txt");
        if (!file.exists()) {
            saveDefaultFile();
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String rawSeq = parts[2].trim();
                        int chromo = Integer.parseInt(parts[3].trim());
                        DNASequence seq = new DNASequence(id, name, rawSeq, chromo);
                        addRow(seq);
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading from text file");
        }
    }

    // write default records if text file does not exist
    private void saveDefaultFile() {
        try {
            FileWriter fw = new FileWriter("dna_sequences.txt");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("SEQ-1092,Homo sapiens,ATGCGATCG,17");
            pw.println("SEQ-2041,Escherichia coli,GCATTCGA,1");
            pw.println("SEQ-3088,Drosophila melanogaster,AATTTGCGC,3");
            pw.close();
        } catch (Exception e) {
            // ignore
        }
    }

    // save current table data into text file
    private void saveDataToFile(boolean showMessage) {
        try {
            FileWriter fw = new FileWriter("dna_sequences.txt");
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < model.getRowCount(); i++) {
                String id = model.getValueAt(i, 0).toString();
                String name = model.getValueAt(i, 1).toString();
                String rawSeq = model.getValueAt(i, 2).toString();
                String chromo = model.getValueAt(i, 3).toString();
                pw.println(id + "," + name + "," + rawSeq + "," + chromo);
            }
            pw.close();
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Table data saved to dna_sequences.txt");
            }
        } catch (Exception e) {
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Error saving data to text file");
            }
        }
    }

    // add one row into the table
    private void addRow(DNASequence seq) {
        model.addRow(new Object[]{
            seq.getSequenceId(),
            seq.getOrganismName(),
            seq.getRawSequence(),
            seq.getChromosomeNumber(),
            seq.getSequenceLength(),
            seq.getGcContent()
        });
    }

    // helper method to check if a string contains only digits
    private boolean isInteger(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    // helper method to check if sequence contains only valid bases
    private boolean isValidDNA(String seq) {
        if (seq == null || seq.length() == 0) {
            return false;
        }
        for (int i = 0; i < seq.length(); i++) {
            char ch = seq.charAt(i);
            if (ch != 'A' && ch != 'T' && ch != 'C' && ch != 'G' &&
                ch != 'a' && ch != 't' && ch != 'c' && ch != 'g') {
                return false;
            }
        }
        return true;
    }

    // function to add new record with validation
    private void addRecord() {
        String id = idField.getText().trim();
        String name = nameBox.getSelectedItem().toString();
        String seqText = seqField.getText().trim();
        String chromoText = chromoField.getText().trim();

        // validation for empty fields
        if (id.length() == 0 || seqText.length() == 0 || chromoText.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all text fields");
            return;
        }

        // validation for sequence id length
        if (id.length() > 10) {
            JOptionPane.showMessageDialog(this, "Sequence ID cannot exceed 10 characters");
            return;
        }

        // check if sequence id already exists
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "Sequence ID must be unique");
                return;
            }
        }

        // validation for dna bases
        if (!isValidDNA(seqText)) {
            JOptionPane.showMessageDialog(this, "Raw sequence must contain only A, T, C, G bases");
            return;
        }

        // validation for raw sequence length
        if (seqText.length() > 7) {
            JOptionPane.showMessageDialog(this, "Raw sequence cannot exceed 7 characters");
            return;
        }

        // validation for chromosome number
        if (!isInteger(chromoText)) {
            JOptionPane.showMessageDialog(this, "Chromosome must be a positive number");
            return;
        }

        if (chromoText.length() > 7) {
            JOptionPane.showMessageDialog(this, "Chromosome number cannot exceed 7 digits");
            return;
        }

        int chromo = Integer.parseInt(chromoText);
        if (chromo <= 0) {
            JOptionPane.showMessageDialog(this, "Chromosome number cannot be zero");
            return;
        }

        DNASequence seq = new DNASequence(id, name, seqText, chromo);
        addRow(seq);
        saveDataToFile(false);
        JOptionPane.showMessageDialog(this, "Record added to table successfully");
    }

    // function to search record by id
    private void searchRecord() {
        String searchId = idField.getText().trim();
        if (searchId.length() == 0) {
            JOptionPane.showMessageDialog(this, "Enter a Sequence ID to search");
            return;
        }

        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(searchId)) {
                table.setRowSelectionInterval(i, i);
                idField.setText(model.getValueAt(i, 0).toString());
                nameBox.setSelectedItem(model.getValueAt(i, 1).toString());
                seqField.setText(model.getValueAt(i, 2).toString());
                chromoField.setText(model.getValueAt(i, 3).toString());
                JOptionPane.showMessageDialog(this, "Record found");
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Record not found");
        }
    }

    // function to update selected record with validation
    private void updateRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row from the table first");
            return;
        }

        String id = idField.getText().trim();
        String name = nameBox.getSelectedItem().toString();
        String seqText = seqField.getText().trim();
        String chromoText = chromoField.getText().trim();

        // validation for empty fields
        if (id.length() == 0 || seqText.length() == 0 || chromoText.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all text fields");
            return;
        }

        // validation for sequence id length
        if (id.length() > 10) {
            JOptionPane.showMessageDialog(this, "Sequence ID cannot exceed 10 characters");
            return;
        }

        // check if sequence id already exists for another record
        for (int i = 0; i < model.getRowCount(); i++) {
            if (i != row && model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "Sequence ID already exists for another record");
                return;
            }
        }

        // validation for dna bases
        if (!isValidDNA(seqText)) {
            JOptionPane.showMessageDialog(this, "Raw sequence must contain only A, T, C, G bases");
            return;
        }

        // validation for raw sequence length
        if (seqText.length() > 7) {
            JOptionPane.showMessageDialog(this, "Raw sequence cannot exceed 7 characters");
            return;
        }

        // validation for chromosome number
        if (!isInteger(chromoText)) {
            JOptionPane.showMessageDialog(this, "Chromosome must be a positive number");
            return;
        }

        if (chromoText.length() > 7) {
            JOptionPane.showMessageDialog(this, "Chromosome number cannot exceed 7 digits");
            return;
        }

        int chromo = Integer.parseInt(chromoText);
        if (chromo <= 0) {
            JOptionPane.showMessageDialog(this, "Chromosome number cannot be zero");
            return;
        }

        DNASequence seq = new DNASequence(id, name, seqText, chromo);

        model.setValueAt(seq.getSequenceId(), row, 0);
        model.setValueAt(seq.getOrganismName(), row, 1);
        model.setValueAt(seq.getRawSequence(), row, 2);
        model.setValueAt(seq.getChromosomeNumber(), row, 3);
        model.setValueAt(seq.getSequenceLength(), row, 4);
        model.setValueAt(seq.getGcContent(), row, 5);

        saveDataToFile(false);
        JOptionPane.showMessageDialog(this, "Record updated successfully");
    }

    // function to delete selected record with confirmation
    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row from the table first");
            return;
        }

        // confirmation dialog before deleting
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            saveDataToFile(false);
            JOptionPane.showMessageDialog(this, "Record deleted successfully");
        }
    }

    // main method to run the program
    public static void main(String[] args) {
        DNASequenceApp app = new DNASequenceApp();
        app.setVisible(true);
    }
}
