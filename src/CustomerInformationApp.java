import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerInformationApp extends JFrame {
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton previousButton, nextButton;
    private DefaultTableModel tableModel;
    private int currentRow = 0;

    public CustomerInformationApp() {
        setTitle("Customer Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel idLabel = new JLabel("ID: ");
        idField = new JTextField(10);
        JLabel lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone: ");
        phoneField = new JTextField(15);

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow > 0) {
                    currentRow--;
                    displayCustomerInfo();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow < tableModel.getRowCount() - 1) {
                    currentRow++;
                    displayCustomerInfo();
                }
            }
        });

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load customer data from the database
        loadCustomerData();
        displayCustomerInfo();
    }

    private void loadCustomerData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e1", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");

            tableModel = new DefaultTableModel();
            tableModel.setColumnIdentifiers(new String[]{"ID", "Last Name", "First Name", "Phone"});

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String phone = resultSet.getString("phone");

                tableModel.addRow(new Object[]{id, lastName, firstName, phone});
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCustomerInfo() {
        if (tableModel.getRowCount() > 0) {
            idField.setText(String.valueOf(tableModel.getValueAt(currentRow, 0)));
            lastNameField.setText(String.valueOf(tableModel.getValueAt(currentRow, 1)));
            firstNameField.setText(String.valueOf(tableModel.getValueAt(currentRow, 2)));
            phoneField.setText(String.valueOf(tableModel.getValueAt(currentRow, 3)));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerInformationApp app = new CustomerInformationApp();
            app.setVisible(true);
        });
    }
}