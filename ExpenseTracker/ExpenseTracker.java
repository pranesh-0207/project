import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseTracker extends JFrame {
    private static final String DATA_FILE = "expenses.csv";
    private static final DateTimeFormatter DT = DateTimeFormatter.ISO_LOCAL_DATE;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JTextField fromField;
    private JTextField toField;
    private JLabel statusLabel;

    private List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTracker app = new ExpenseTracker();
            app.setVisible(true);
        });
    }

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadData();
        refreshTable();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton exportBtn = new JButton("Export CSV");
        JButton summaryBtn = new JButton("Summary");
        JButton refreshBtn = new JButton("Refresh");
        topBar.add(addBtn); topBar.add(editBtn); topBar.add(delBtn);
        topBar.add(exportBtn); topBar.add(summaryBtn); topBar.add(refreshBtn);
        add(topBar, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(18);
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{""});
        categoryCombo.setPreferredSize(new Dimension(160, 24));
        filterPanel.add(categoryCombo);
        filterPanel.add(new JLabel("From:"));
        fromField = new JTextField(10);
        filterPanel.add(fromField);
        filterPanel.add(new JLabel("To:"));
        toField = new JTextField(10);
        filterPanel.add(toField);
        JButton applyFilter = new JButton("Apply");
        JButton clearFilter = new JButton("Clear");
        filterPanel.add(applyFilter);
        filterPanel.add(clearFilter);
        add(filterPanel, BorderLayout.AFTER_LAST_LINE);

        String[] cols = {"ID","Title","Amount","Category","Date","Notes"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        delBtn.addActionListener(e -> deleteSelected());
        exportBtn.addActionListener(e -> exportCsv());
        summaryBtn.addActionListener(e -> showSummary());
        refreshBtn.addActionListener(e -> { loadData(); refreshTable(); });

        applyFilter.addActionListener(e -> refreshTable());
        clearFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                categoryCombo.setSelectedIndex(0);
                fromField.setText("");
                toField.setText("");
                refreshTable();
            }
        });

        searchField.addActionListener(e -> refreshTable());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) openEditDialog(); }
        });
    }

    private void loadData() {
        expenses.clear();
        Path p = Paths.get(DATA_FILE);
        if (!Files.exists(p)) {
            try { Files.createFile(p); } catch (IOException ex) {}
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                Expense ex = Expense.fromCsv(line);
                if (ex != null) expenses.add(ex);
            }
        } catch (IOException ex) {}
    }

    private void saveData() {
        Path p = Paths.get(DATA_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (Expense e : expenses) {
                bw.write(e.toCsv());
                bw.newLine();
            }
        } catch (IOException ex) {}
    }

    private void refreshTable() {
        String query = safeLower(searchField.getText());
        String cat = safeString((String) categoryCombo.getSelectedItem());
        String from = safeString(fromField.getText());
        String to = safeString(toField.getText());

        List<Expense> filtered = new ArrayList<>();
        for (Expense e : expenses) {
            boolean ok = true;
            if (!query.isEmpty()) ok &= (e.title.toLowerCase().contains(query) || e.notes.toLowerCase().contains(query));
            if (!cat.isEmpty()) ok &= cat.equals(e.category);
            if (!from.isEmpty()) {
                try { ok &= !e.date.isBefore(LocalDate.parse(from, DT)); } catch (Exception ex) {}
            }
            if (!to.isEmpty()) {
                try { ok &= !e.date.isAfter(LocalDate.parse(to, DT)); } catch (Exception ex) {}
            }
            if (ok) filtered.add(e);
        }

        filtered.sort(new Comparator<Expense>() {
            public int compare(Expense a, Expense b) {
                int d = b.date.compareTo(a.date);
                if (d != 0) return d;
                return Integer.compare(b.id, a.id);
            }
        });

        Set<String> cats = new TreeSet<>();
        for (Expense e : expenses) if (e.category != null && !e.category.isEmpty()) cats.add(e.category);
        String selected = safeString((String) categoryCombo.getSelectedItem());
        categoryCombo.removeAllItems(); categoryCombo.addItem("");
        for (String c : cats) categoryCombo.addItem(c);
        categoryCombo.setSelectedItem(selected);

        tableModel.setRowCount(0);
        double total = 0.0;
        for (Expense e : filtered) {
            tableModel.addRow(new Object[]{e.id, e.title, String.format("%.2f", e.amount), e.category, e.date.format(DT), e.notes});
            total += e.amount;
        }
        statusLabel.setText(String.format("%d record(s) - Total: %.2f", filtered.size(), total));
    }

    private void openAddDialog() {
        ExpenseDialog dlg = new ExpenseDialog(this, "Add Expense", null);
        dlg.setVisible(true);
        Expense res = dlg.getResult();
        if (res != null) {
            int nid = expenses.stream().mapToInt(x -> x.id).max().orElse(0) + 1;
            res.id = nid;
            expenses.add(res);
            saveData();
            refreshTable();
        }
    }

    private void openEditDialog() {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        int id = Integer.parseInt(tableModel.getValueAt(sel, 0).toString());
        Expense ex = null;
        for (Expense e : expenses) if (e.id == id) { ex = e; break; }
        if (ex == null) return;
        ExpenseDialog dlg = new ExpenseDialog(this, "Edit Expense", ex.copy());
        dlg.setVisible(true);
        Expense res = dlg.getResult();
        if (res != null) {
            ex.title = res.title;
            ex.amount = res.amount;
            ex.category = res.category;
            ex.date = res.date;
            ex.notes = res.notes;
            saveData();
            refreshTable();
        }
    }

    private void deleteSelected() {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        int id = Integer.parseInt(tableModel.getValueAt(sel, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Delete selected expense?") == JOptionPane.YES_OPTION) {
            Iterator<Expense> it = expenses.iterator();
            while (it.hasNext()) if (it.next().id == id) { it.remove(); break; }
            saveData();
            refreshTable();
        }
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("expenses_export.csv"));
        int rv = fc.showSaveDialog(this);
        if (rv != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try (BufferedWriter bw = Files.newBufferedWriter(f.toPath())) {
            bw.write("ID,Title,Amount,Category,Date,Notes");
            bw.newLine();
            for (Expense e : expenses) {
                bw.write(e.toCsv());
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exported");
        } catch (IOException ex) {}
    }

    private void showSummary() {
        Map<String, Double> sums = new TreeMap<>();
        for (Expense e : expenses) {
            String c = (e.category == null || e.category.isEmpty()) ? "Uncategorized" : e.category;
            sums.put(c, sums.getOrDefault(c, 0.0) + e.amount);
        }
        StringBuilder sb = new StringBuilder();
        double total = 0.0;
        for (Map.Entry<String, Double> en : sums.entrySet()) {
            sb.append(String.format("%s : %.2f%n", en.getKey(), en.getValue()));
            total += en.getValue();
        }
        sb.append(String.format("%nTotal: %.2f%n", total));
        JOptionPane.showMessageDialog(this, sb.toString(), "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private String safeString(String s) { return s == null ? "" : s.trim(); }
    private String safeLower(String s) { return s == null ? "" : s.trim().toLowerCase(); }

    private static class Expense {
        int id;
        String title;
        double amount;
        String category;
        LocalDate date;
        String notes;

        Expense() {}
        Expense(int id, String title, double amount, String category, LocalDate date, String notes) {
            this.id = id; this.title = title; this.amount = amount; this.category = category; this.date = date; this.notes = notes;
        }

        static Expense fromCsv(String line) {
            List<String> p = parse(line);
            if (p.size() < 6) return null;
            try {
                return new Expense(
                        Integer.parseInt(p.get(0)),
                        p.get(1),
                        Double.parseDouble(p.get(2)),
                        p.get(3),
                        LocalDate.parse(p.get(4), DT),
                        p.get(5)
                );
            } catch (Exception ex) { return null; }
        }

        String toCsv() {
            return join(Arrays.asList(
                    Integer.toString(id),
                    title == null ? "" : title,
                    String.format("%.2f", amount),
                    category == null ? "" : category,
                    date == null ? LocalDate.now().format(DT) : date.format(DT),
                    notes == null ? "" : notes
            ));
        }

        Expense copy() { return new Expense(id, title, amount, category, date, notes); }

        private static List<String> parse(String line) {
            List<String> out = new ArrayList<>();
            boolean q = false;
            StringBuilder cur = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch == '"') {
                    if (q && i+1 < line.length() && line.charAt(i+1) == '"') { cur.append('"'); i++; }
                    else q = !q;
                } else if (ch == ',' && !q) { out.add(cur.toString()); cur.setLength(0); }
                else cur.append(ch);
            }
            out.add(cur.toString());
            return out;
        }

        private static String join(List<String> f) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String x : f) {
                if (!first) sb.append(',');
                first = false;
                if (x == null) x = "";
                boolean need = x.contains(",") || x.contains("\"");
                String r = x.replace("\"", "\"\"");
                if (need) sb.append('"').append(r).append('"'); else sb.append(r);
            }
            return sb.toString();
        }
    }

    private static class ExpenseDialog extends JDialog {
        private JTextField titleField, amountField, categoryField, dateField;
        private JTextArea notesArea;
        private Expense result;

        ExpenseDialog(Frame parent, String title, Expense exp) {
            super(parent, title, true);
            setSize(400, 350);
            setLocationRelativeTo(parent);
            JPanel p = new JPanel(new GridLayout(6,2,5,5));
            titleField = new JTextField(exp == null ? "" : exp.title);
            amountField = new JTextField(exp == null ? "0.00" : String.format("%.2f", exp.amount));
            categoryField = new JTextField(exp == null ? "" : exp.category);
            dateField = new JTextField(exp == null ? LocalDate.now().format(DT) : exp.date.format(DT));
            notesArea = new JTextArea(exp == null ? "" : exp.notes);
            p.add(new JLabel("Title:")); p.add(titleField);
            p.add(new JLabel("Amount:")); p.add(amountField);
            p.add(new JLabel("Category:")); p.add(categoryField);
            p.add(new JLabel("Date:")); p.add(dateField);
            p.add(new JLabel("Notes:")); p.add(new JScrollPane(notesArea));
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            JPanel bp = new JPanel();
            bp.add(save); bp.add(cancel);
            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { onSave(); }
            });
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { dispose(); }
            });
            add(p, BorderLayout.CENTER);
            add(bp, BorderLayout.SOUTH);
        }

        private void onSave() {
            try {
                String t = titleField.getText().trim();
                double amt = Double.parseDouble(amountField.getText().trim());
                String c = categoryField.getText().trim();
                LocalDate d = LocalDate.parse(dateField.getText().trim(), DT);
                String n = notesArea.getText().trim();
                result = new Expense(0, t, amt, c, d, n);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }

        Expense getResult() { return result; }
    }
}
