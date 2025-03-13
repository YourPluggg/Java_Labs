import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Создаём окно
        JFrame frame = new JFrame("Integral Calculator");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель для ввода данных
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 3, 5, 5));
        JTextField lowerBoundField = new JTextField("Lower Bound");
        JTextField upperBoundField = new JTextField("Upper Bound");
        JTextField stepField = new JTextField("Step");
        inputPanel.add(lowerBoundField);
        inputPanel.add(upperBoundField);
        inputPanel.add(stepField);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton calculateButton = new JButton("Calculate");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);

        // Отступ между полями и таблицей
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Модель и таблица
        String[] columnNames = {"Lower Bound", "Upper Bound", "Step", "Result"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 3; // Разрешаем редактирование только первых трех колонок
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Добавление строки
        addButton.addActionListener(e -> {
            String lower = lowerBoundField.getText();
            String upper = upperBoundField.getText();
            String step = stepField.getText();
            tableModel.addRow(new Object[]{lower, upper, step, ""});
        });

        // Удаление строки
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });

        // Вычисление интеграла
        calculateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    double lower = Double.parseDouble(tableModel.getValueAt(selectedRow, 0).toString());
                    double upper = Double.parseDouble(tableModel.getValueAt(selectedRow, 1).toString());
                    double step = Double.parseDouble(tableModel.getValueAt(selectedRow, 2).toString());
                    double result = computeIntegral(lower, upper, step);
                    tableModel.setValueAt(result, selectedRow, 3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Добавляем компоненты в окно
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Метод для вычисления интеграла
    public static double computeIntegral(double LowLim, double UpLim, double step) {
        double start, h;
        double sumS = 0;
        start = LowLim;
        do {
            h = Math.min(step, (UpLim - start));
            sumS += h * 0.5 * (Math.cos(start) + Math.cos(start + h));
            start += h;
        } while (start < UpLim);

        return sumS;
    }
}
