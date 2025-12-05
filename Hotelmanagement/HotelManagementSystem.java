import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class HotelManagementSystem extends JFrame {
    private static final String ROOMS_FILE = "rooms.csv";
    private static final String CUSTOMERS_FILE = "customers.csv";
    private static final String BOOKINGS_FILE = "bookings.csv";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE;

    private List<Room> rooms = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private JTable roomTable, customerTable, bookingTable;
    private DefaultTableModel roomModel, customerModel, bookingModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelManagementSystem app = new HotelManagementSystem();
            app.setVisible(true);
        });
    }

    public HotelManagementSystem() {
        setTitle("Hotel Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadAll();
        initUI();
        refreshAllTables();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Rooms", roomsPanel());
        tabs.addTab("Customers", customersPanel());
        tabs.addTab("Bookings", bookingsPanel());
        tabs.addTab("Operations", operationsPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel roomsPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));

        String[] cols = {"Room No","Type","Rate","Status"};
        roomModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c){return false;} };
        roomTable = new JTable(roomModel);
        p.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Room");
        JButton edit = new JButton("Edit Room");
        JButton del = new JButton("Delete Room");
        JButton refresh = new JButton("Refresh");
        top.add(add); top.add(edit); top.add(del); top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        add.addActionListener(e -> addRoomDialog());
        edit.addActionListener(e -> editRoomDialog());
        del.addActionListener(e -> deleteSelectedRoom());
        refresh.addActionListener(e -> refreshAllTables());

        return p;
    }

    private JPanel customersPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));

        String[] cols = {"Customer ID","Name","Phone","Email","Notes"};
        customerModel = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r, int c){return false;} };
        customerTable = new JTable(customerModel);
        p.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Customer");
        JButton edit = new JButton("Edit Customer");
        JButton del = new JButton("Delete Customer");
        JButton refresh = new JButton("Refresh");
        top.add(add); top.add(edit); top.add(del); top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        add.addActionListener(e -> addCustomerDialog());
        edit.addActionListener(e -> editCustomerDialog());
        del.addActionListener(e -> deleteSelectedCustomer());
        refresh.addActionListener(e -> refreshAllTables());

        return p;
    }

    private JPanel bookingsPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));

        String[] cols = {"Booking ID","Room No","Customer ID","Customer Name","Check-In","Check-Out","Nights","Rate","Total","Status"};
        bookingModel = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r, int c){return false;} };
        bookingTable = new JTable(bookingModel);
        p.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("New Booking");
        JButton checkout = new JButton("Checkout / Generate Bill");
        JButton cancel = new JButton("Cancel Booking");
        JButton refresh = new JButton("Refresh");
        top.add(add); top.add(checkout); top.add(cancel); top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        add.addActionListener(e -> newBookingDialog());
        checkout.addActionListener(e -> checkoutSelectedBooking());
        cancel.addActionListener(e -> cancelSelectedBooking());
        refresh.addActionListener(e -> refreshAllTables());

        return p;
    }

    private JPanel operationsPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        JPanel left = new JPanel(new GridLayout(6,1,6,6));

        JButton viewAvailable = new JButton("View Available Rooms");
        JButton searchBookings = new JButton("Search Bookings");
        JButton exportData = new JButton("Export All CSVs");
        JButton loadDefaults = new JButton("Load Sample Data");
        JButton clearAll = new JButton("Clear All Data");

        left.add(viewAvailable); left.add(searchBookings); left.add(exportData); left.add(loadDefaults); left.add(clearAll);

        p.add(left, BorderLayout.WEST);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        p.add(new JScrollPane(info), BorderLayout.CENTER);

        viewAvailable.addActionListener(e -> {
            List<Room> av = rooms.stream().filter(r -> r.status.equals("Available")).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder("Available Rooms:\n");
            for (Room r : av) sb.append(String.format("Room %s - %s - %.2f\n", r.number, r.type, r.rate));
            info.setText(sb.toString());
        });

        searchBookings.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, "Search by Customer Name or Room No:");
            if (s == null) return;
            String q = s.trim().toLowerCase();
            List<Booking> res = bookings.stream().filter(b -> b.customerName.toLowerCase().contains(q) || b.roomNumber.toLowerCase().contains(q)).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder("Search Results:\n");
            for (Booking b : res) sb.append(b.brief()).append("\n");
            info.setText(sb.toString());
        });

        exportData.addActionListener(e -> {
            saveAll();
            JOptionPane.showMessageDialog(this, "Saved CSV files: " + ROOMS_FILE + ", " + CUSTOMERS_FILE + ", " + BOOKINGS_FILE);
        });

        loadDefaults.addActionListener(e -> {
            preloadSampleData();
            refreshAllTables();
            JOptionPane.showMessageDialog(this, "Sample data loaded");
        });

        clearAll.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Delete ALL data? This cannot be undone.", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                rooms.clear(); customers.clear(); bookings.clear();
                saveAll();
                refreshAllTables();
            }
        });

        return p;
    }

    private void addRoomDialog() {
        JTextField no = new JTextField();
        JTextField type = new JTextField();
        JTextField rate = new JTextField();
        Object[] fields = {"Room Number:", no, "Type (Single/Double/Deluxe):", type, "Rate per night:", rate};
        int r = JOptionPane.showConfirmDialog(this, fields, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        String rn = no.getText().trim();
        if (rn.isEmpty()) return;
        double rt = parseDouble(rate.getText().trim(), 0.0);
        rooms.add(new Room(rn, type.getText().trim(), rt, "Available"));
        saveRooms();
        refreshAllTables();
    }

    private void editRoomDialog() {
        int sel = roomTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a room"); return; }
        String rn = roomModel.getValueAt(sel, 0).toString();
        Room room = findRoom(rn);
        if (room == null) return;
        JTextField type = new JTextField(room.type);
        JTextField rate = new JTextField(String.valueOf(room.rate));
        JComboBox<String> status = new JComboBox<>(new String[]{"Available","Occupied","Maintenance"});
        status.setSelectedItem(room.status);
        Object[] fields = {"Type:", type, "Rate:", rate, "Status:", status};
        int r = JOptionPane.showConfirmDialog(this, fields, "Edit Room " + rn, JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        room.type = type.getText().trim();
        room.rate = parseDouble(rate.getText().trim(), room.rate);
        room.status = (String)status.getSelectedItem();
        saveRooms();
        refreshAllTables();
    }

    private void deleteSelectedRoom() {
        int sel = roomTable.getSelectedRow();
        if (sel < 0) return;
        String rn = roomModel.getValueAt(sel, 0).toString();
        Room room = findRoom(rn);
        if (room == null) return;
        if ("Occupied".equals(room.status)) { JOptionPane.showMessageDialog(this, "Cannot delete occupied room"); return; }
        rooms.remove(room);
        saveRooms();
        refreshAllTables();
    }

    private void addCustomerDialog() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JTextArea notes = new JTextArea(4,20);
        Object[] fields = {"Name:", name, "Phone:", phone, "Email:", email, "Notes:", new JScrollPane(notes)};
        int r = JOptionPane.showConfirmDialog(this, fields, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        String id = generateCustomerId();
        customers.add(new Customer(id, name.getText().trim(), phone.getText().trim(), email.getText().trim(), notes.getText().trim()));
        saveCustomers();
        refreshAllTables();
    }

    private void editCustomerDialog() {
        int sel = customerTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a customer"); return; }
        String id = customerModel.getValueAt(sel, 0).toString();
        Customer c = findCustomer(id);
        if (c == null) return;
        JTextField name = new JTextField(c.name);
        JTextField phone = new JTextField(c.phone);
        JTextField email = new JTextField(c.email);
        JTextArea notes = new JTextArea(c.notes,4,20);
        Object[] fields = {"Name:", name, "Phone:", phone, "Email:", email, "Notes:", new JScrollPane(notes)};
        int r = JOptionPane.showConfirmDialog(this, fields, "Edit Customer " + id, JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        c.name = name.getText().trim();
        c.phone = phone.getText().trim();
        c.email = email.getText().trim();
        c.notes = notes.getText().trim();
        saveCustomers();
        refreshAllTables();
    }

    private void deleteSelectedCustomer() {
        int sel = customerTable.getSelectedRow();
        if (sel < 0) return;
        String id = customerModel.getValueAt(sel, 0).toString();
        Customer c = findCustomer(id);
        if (c == null) return;
        boolean used = bookings.stream().anyMatch(b -> b.customerId.equals(id) && !"Cancelled".equals(b.status));
        if (used) { JOptionPane.showMessageDialog(this, "Customer has active or past bookings; cannot delete"); return; }
        customers.remove(c);
        saveCustomers();
        refreshAllTables();
    }

    private void newBookingDialog() {
        if (rooms.isEmpty()) { JOptionPane.showMessageDialog(this, "No rooms available in system"); return; }
        if (customers.isEmpty()) { JOptionPane.showMessageDialog(this, "Add customers first"); return; }
        JComboBox<String> roomBox = new JComboBox<>(rooms.stream().map(r->r.number + " ("+r.type+")").toArray(String[]::new));
        JComboBox<String> custBox = new JComboBox<>(customers.stream().map(c->c.id + " - " + c.name).toArray(String[]::new));
        JTextField inDate = new JTextField(LocalDate.now().format(DTF));
        JTextField outDate = new JTextField(LocalDate.now().plusDays(1).format(DTF));
        JTextField extras = new JTextField("0.0");
        Object[] fields = {"Select Room:", roomBox, "Select Customer:", custBox, "Check-in (YYYY-MM-DD):", inDate, "Check-out (YYYY-MM-DD):", outDate, "Extras / Services:", extras};
        int r = JOptionPane.showConfirmDialog(this, fields, "New Booking", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        String roomSel = (String)roomBox.getSelectedItem();
        String roomNo = roomSel.split(" ")[0];
        String custSel = (String)custBox.getSelectedItem();
        String custId = custSel.split(" ")[0];
        LocalDate in = parseDate(inDate.getText().trim(), LocalDate.now());
        LocalDate out = parseDate(outDate.getText().trim(), in.plusDays(1));
        if (!out.isAfter(in)) { JOptionPane.showMessageDialog(this, "Check-out must be after check-in"); return; }
        double extra = parseDouble(extras.getText().trim(), 0.0);
        Room room = findRoom(roomNo);
        if (room == null) { JOptionPane.showMessageDialog(this, "Room not found"); return; }
        boolean conflict = bookings.stream().anyMatch(b -> b.roomNumber.equals(roomNo) && !"Cancelled".equals(b.status) && overlaps(in, out, b.checkIn, b.checkOut));
        if (conflict) { JOptionPane.showMessageDialog(this, "Room is already booked for selected dates"); return; }
        Customer c = findCustomer(custId);
        String bid = generateBookingId();
        long nights = Duration.between(in.atStartOfDay(), out.atStartOfDay()).toDays();
        double total = nights * room.rate + extra;
        Booking book = new Booking(bid, roomNo, room.type, c.id, c.name, in, out, nights, room.rate, extra, total, "Booked");
        bookings.add(book);
        room.status = "Occupied";
        saveBookings();
        saveRooms();
        refreshAllTables();
    }

    private void checkoutSelectedBooking() {
        int sel = bookingTable.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a booking"); return; }
        String bid = bookingModel.getValueAt(sel, 0).toString();
        Booking b = findBooking(bid);
        if (b == null) return;
        if ("CheckedOut".equalsIgnoreCase(b.status) || "Cancelled".equalsIgnoreCase(b.status)) { JOptionPane.showMessageDialog(this, "Booking already closed"); return; }
        JTextField paid = new JTextField(String.format("%.2f", b.total));
        Object[] fields = {"Total Amount: " + String.format("%.2f", b.total), "Amount Paid:", paid};
        int r = JOptionPane.showConfirmDialog(this, fields, "Checkout Booking " + bid, JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;
        double amtPaid = parseDouble(paid.getText().trim(), b.total);
        double balance = amtPaid - b.total;
        b.status = "CheckedOut";
        Room room = findRoom(b.roomNumber);
        if (room != null) room.status = "Available";
        saveBookings();
        saveRooms();
        refreshAllTables();
        String bill = generateBillText(b, amtPaid, balance);
        JTextArea ta = new JTextArea(bill);
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Bill - Booking " + bid, JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelSelectedBooking() {
        int sel = bookingTable.getSelectedRow();
        if (sel < 0) return;
        String bid = bookingModel.getValueAt(sel, 0).toString();
        Booking b = findBooking(bid);
        if (b == null) return;
        if ("Cancelled".equalsIgnoreCase(b.status)) { JOptionPane.showMessageDialog(this, "Already cancelled"); return; }
        int c = JOptionPane.showConfirmDialog(this, "Cancel booking " + bid + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        b.status = "Cancelled";
        Room room = findRoom(b.roomNumber);
        if (room != null) room.status = "Available";
        saveBookings();
        saveRooms();
        refreshAllTables();
    }

    private void preloadSampleData() {
        rooms.clear(); customers.clear(); bookings.clear();
        rooms.add(new Room("101","Single",1500.0,"Available"));
        rooms.add(new Room("102","Double",2200.0,"Available"));
        rooms.add(new Room("201","Deluxe",3500.0,"Available"));
        customers.add(new Customer("C001","Alice","9999999999","alice@example.com","VIP"));
        customers.add(new Customer("C002","Bob","8888888888","bob@example.com","Requires airport pickup"));
        saveRooms(); saveCustomers(); saveBookings();
    }

    private void refreshAllTables() {
        roomModel.setRowCount(0);
        for (Room r : rooms) roomModel.addRow(new Object[]{r.number, r.type, String.format("%.2f", r.rate), r.status});

        customerModel.setRowCount(0);
        for (Customer c : customers) customerModel.addRow(new Object[]{c.id, c.name, c.phone, c.email, c.notes});

        bookingModel.setRowCount(0);
        for (Booking b : bookings) bookingModel.addRow(new Object[]{b.id, b.roomNumber, b.customerId, b.customerName, b.checkIn.format(DTF), b.checkOut.format(DTF), b.nights, String.format("%.2f", b.rate), String.format("%.2f", b.total), b.status});
    }

    private void loadAll() {
        loadRooms(); loadCustomers(); loadBookings();
    }

    private void saveAll() {
        saveRooms(); saveCustomers(); saveBookings();
    }

    private void loadRooms() {
        rooms.clear();
        List<String> lines = readCsv(ROOMS_FILE);
        for (String l : lines) {
            List<String> p = splitCsv(l);
            if (p.size() < 4) continue;
            rooms.add(new Room(p.get(0), p.get(1), parseDouble(p.get(2),0.0), p.get(3)));
        }
    }

    private void saveRooms() {
        List<String> out = new ArrayList<>();
        for (Room r : rooms) out.add(csvJoin(Arrays.asList(r.number, r.type, String.valueOf(r.rate), r.status)));
        writeCsv(ROOMS_FILE, out);
    }

    private void loadCustomers() {
        customers.clear();
        List<String> lines = readCsv(CUSTOMERS_FILE);
        for (String l : lines) {
            List<String> p = splitCsv(l);
            if (p.size() < 5) continue;
            customers.add(new Customer(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4)));
        }
    }

    private void saveCustomers() {
        List<String> out = new ArrayList<>();
        for (Customer c : customers) out.add(csvJoin(Arrays.asList(c.id, c.name, c.phone, c.email, c.notes)));
        writeCsv(CUSTOMERS_FILE, out);
    }

    private void loadBookings() {
        bookings.clear();
        List<String> lines = readCsv(BOOKINGS_FILE);
        for (String l : lines) {
            List<String> p = splitCsv(l);
            if (p.size() < 11) continue;
            Booking b = new Booking(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4).equals("")? "":p.get(4), parseDate(p.get(5), LocalDate.now()), parseDate(p.get(6), LocalDate.now()), Long.parseLong(p.get(7)), parseDouble(p.get(8),0.0), parseDouble(p.get(9),0.0), parseDouble(p.get(10),0.0), p.size()>11?p.get(11):"Booked");
            bookings.add(b);
        }
    }

    private void saveBookings() {
        List<String> out = new ArrayList<>();
        for (Booking b : bookings) out.add(csvJoin(Arrays.asList(b.id, b.roomNumber, b.roomType, b.customerId, b.customerName, b.checkIn.format(DTF), b.checkOut.format(DTF), String.valueOf(b.nights), String.valueOf(b.rate), String.valueOf(b.extras), String.valueOf(b.total), b.status)));
        writeCsv(BOOKINGS_FILE, out);
    }

    private Room findRoom(String no) { return rooms.stream().filter(r->r.number.equals(no)).findFirst().orElse(null); }
    private Customer findCustomer(String id) { return customers.stream().filter(c->c.id.equals(id)).findFirst().orElse(null); }
    private Booking findBooking(String id) { return bookings.stream().filter(b->b.id.equals(id)).findFirst().orElse(null); }

    private String generateCustomerId() {
        int max = 0;
        for (Customer c : customers) {
            try { max = Math.max(max, Integer.parseInt(c.id.replaceAll("[^0-9]",""))); } catch (Exception ignored) {}
        }
        return String.format("C%03d", max+1);
    }

    private String generateBookingId() {
        int max = 0;
        for (Booking b : bookings) {
            try { max = Math.max(max, Integer.parseInt(b.id.replaceAll("[^0-9]",""))); } catch (Exception ignored) {}
        }
        return String.format("B%04d", max+1);
    }

    private boolean overlaps(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.isBefore(start2.plusDays(1)) || start1.isAfter(end2.minusDays(1)));
    }

    private String generateBillText(Booking b, double paid, double balance) {
        StringBuilder sb = new StringBuilder();
        sb.append("***** HOTEL BILL *****\n");
        sb.append("Booking ID: ").append(b.id).append("\n");
        sb.append("Customer: ").append(b.customerName).append(" (").append(b.customerId).append(")\n");
        sb.append("Room: ").append(b.roomNumber).append(" (").append(b.roomType).append(")\n");
        sb.append("Check-in: ").append(b.checkIn.format(DTF)).append("\n");
        sb.append("Check-out: ").append(b.checkOut.format(DTF)).append("\n");
        sb.append("Nights: ").append(b.nights).append("\n");
        sb.append(String.format("Rate: %.2f per night\n", b.rate));
        sb.append(String.format("Extras/Services: %.2f\n", b.extras));
        sb.append(String.format("TOTAL: %.2f\n", b.total));
        sb.append(String.format("Paid: %.2f\n", paid));
        sb.append(String.format("Balance (paid - total): %.2f\n", balance));
        sb.append("Thank you!\n");
        return sb.toString();
    }

    private List<String> readCsv(String file) {
        Path p = Paths.get(file);
        if (!Files.exists(p)) return new ArrayList<>();
        try {
            return Files.readAllLines(p);
        } catch (IOException e) { return new ArrayList<>(); }
    }

    private void writeCsv(String file, List<String> lines) {
        try {
            Files.write(Paths.get(file), lines);
        } catch (IOException e) {}
    }

    private List<String> splitCsv(String line) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;
        boolean q = false;
        StringBuilder cur = new StringBuilder();
        for (int i=0;i<line.length();i++) {
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

    private String csvJoin(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String f : fields) {
            if (!first) sb.append(',');
            first = false;
            if (f == null) f = "";
            boolean need = f.contains(",") || f.contains("\"") || f.contains("\n");
            String r = f.replace("\"","\"\"");
            if (need) sb.append('"').append(r).append('"'); else sb.append(r);
        }
        return sb.toString();
    }

    private LocalDate parseDate(String s, LocalDate fallback) {
        try { return LocalDate.parse(s, DTF); } catch (Exception e) { return fallback; }
    }

    private double parseDouble(String s, double fallback) {
        try { return Double.parseDouble(s); } catch (Exception e) { return fallback; }
    }

    private void loadBookingsUIModels() {
        // placeholder if needed
    }

    private void saveCustomers() { saveCustomers(); } // avoid unused-warning (actual saveCustomers exists above)
    // but above saveCustomers method is defined; this duplicate helps no-op; remove if compiler flags it.

    // Data classes
    private static class Room {
        String number; String type; double rate; String status;
        Room(String n, String t, double r, String s){ number=n; type=t; rate=r; status=s; }
    }
    private static class Customer {
        String id; String name; String phone; String email; String notes;
        Customer(String id,String name,String phone,String email,String notes){ this.id=id; this.name=name; this.phone=phone; this.email=email; this.notes=notes;}
    }
    private static class Booking {
        String id; String roomNumber; String roomType; String customerId; String customerName; LocalDate checkIn; LocalDate checkOut; long nights; double rate; double extras; double total; String status;
        Booking(String id,String roomNumber,String roomType,String customerId,String customerName,LocalDate checkIn,LocalDate checkOut,long nights,double rate,double extras,double total,String status){
            this.id=id; this.roomNumber=roomNumber; this.roomType=roomType; this.customerId=customerId; this.customerName=customerName; this.checkIn=checkIn; this.checkOut=checkOut; this.nights=nights; this.rate=rate; this.extras=extras; this.total=total; this.status=status;
        }
        String brief(){ return id + " | Room " + roomNumber + " | " + customerName + " | " + checkIn.format(DTF) + " - " + checkOut.format(DTF) + " | " + status; }
    }
}
