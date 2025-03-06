import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Список для хранения данных
    private static List<recIntegral> line = new ArrayList<>();
    // Список для удалённых данных
    private static List<recIntegral> backup = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Калькулятор");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель для ввода данных
        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
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
        JButton clearButton = new JButton("Clear");
        JButton fillButton = new JButton("Fill from Collection");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(fillButton);

        // Таблица и модель
        String[] columnNames = {"Lower Bound", "Upper Bound", "Step", "Result"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 3;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Добавление записи в таблицу и коллекцию
        addButton.addActionListener(e -> {
            try {
                double lower = Double.parseDouble(lowerBoundField.getText());
                double upper = Double.parseDouble(upperBoundField.getText());
                double step = Double.parseDouble(stepField.getText());
                recIntegral record = new recIntegral(lower, upper, step, 0);
                line.add(record); // Добавляем в основную коллекцию
                tableModel.addRow(new Object[]{lower, upper, step, ""});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Удаление записи
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                recIntegral removedRecord = line.remove(selectedRow); // Удаляем из основной коллекции
                backup.add(removedRecord); // Сохраняем в бэкап
                tableModel.removeRow(selectedRow);
            }
        });

        // Вычисление интеграла
        calculateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    recIntegral record = line.get(selectedRow);
                    record.result = computeIntegral(record.lowerBound, record.upperBound, record.step);
                    tableModel.setValueAt(record.result, selectedRow, 3);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Calculation error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Очистка таблицы и сохранение данных в бэкап
        clearButton.addActionListener(e -> {
            backup.addAll(line); // Сохраняем всё, что было в таблице
            line.clear(); // Очищаем основную коллекцию
            tableModel.setRowCount(0); // Очищаем таблицу
        });

        // Заполнение таблицы из коллекции и восстановление удалённых данных
        fillButton.addActionListener(e -> {
            tableModel.setRowCount(0); // Очистить таблицу перед добавлением данных
            System.out.println("Filling table from collection... Total records: " + line.size());

            // Заполняем таблицу текущими данными
            for (recIntegral record : line) {
                tableModel.addRow(new Object[]{record.lowerBound, record.upperBound, record.step, record.result});
            }

            // Восстанавливаем удалённые данные
            if (!backup.isEmpty()) {
                System.out.println("Restoring " + backup.size() + " records from backup...");
                line.addAll(backup);
                backup.clear(); // Очистка бэкапа после восстановления

                for (recIntegral record : line) {
                    tableModel.addRow(new Object[]{record.lowerBound, record.upperBound, record.step, record.result});
                }
            }
        });

        // Добавляем компоненты в окно
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tableScrollPane, BorderLayout.SOUTH);

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
