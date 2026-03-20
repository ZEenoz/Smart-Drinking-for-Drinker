import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. Model Layer (OOP Concepts)
// ==========================================

// Enum สำหรับหมวดหมู่ (ช่วยในการจัดกลุ่มในหน้า Custom)
enum Category {
    BASE("Base/Liquid"),
    POWDER("Powder"),
    MILK("Milk/Cream"),
    SYRUP("Syrup/Sweetener"),
    TOPPING("Topping");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

// Class วัตถุดิบ
class Ingredient {
    private String name;
    private double price;
    private Category category;

    public Ingredient(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}

// Class เครื่องดื่ม (ใช้เก็บ State ของออเดอร์ปัจจุบัน)
class Beverage {
    private List<Ingredient> ingredients;

    public Beverage() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient item) {
        ingredients.add(item);
    }

    public void clear() {
        ingredients.clear();
    }

    // คำนวณราคาแบบ Real-time
    public double getTotalPrice() {
        double total = 0;
        for (Ingredient i : ingredients) {
            total += i.getPrice();
        }
        return total;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}

// ==========================================
// 2. Data Layer (Mock Database)
// ==========================================
class IngredientRepository {
    public static List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        // Base
        list.add(new Ingredient("Hot Water", 5, Category.BASE));
        list.add(new Ingredient("Cold Water", 5, Category.BASE));
        list.add(new Ingredient("Soda Water", 15, Category.BASE));
        list.add(new Ingredient("Espresso Shot", 20, Category.BASE));

        // Powder
        list.add(new Ingredient("Cocoa Powder", 15, Category.POWDER));
        list.add(new Ingredient("Matcha Powder", 15, Category.POWDER));
        list.add(new Ingredient("Thai Tea Powder", 15, Category.POWDER));

        // Milk
        list.add(new Ingredient("Fresh Milk", 10, Category.MILK));
        list.add(new Ingredient("Oat Milk", 20, Category.MILK));
        list.add(new Ingredient("Condensed Milk", 5, Category.MILK));

        // Syrup
        list.add(new Ingredient("Sugar", 0, Category.SYRUP));
        list.add(new Ingredient("Caramel Syrup", 10, Category.SYRUP));
        list.add(new Ingredient("Honey", 15, Category.SYRUP));

        // Topping
        list.add(new Ingredient("Bubble (Boba)", 10, Category.TOPPING));
        list.add(new Ingredient("Jelly", 10, Category.TOPPING));
        list.add(new Ingredient("Whipped Cream", 15, Category.TOPPING));

        return list;
    }

    // กรองวัตถุดิบตามหมวดหมู่
    public static List<Ingredient> getByCategory(Category cat) {
        List<Ingredient> filtered = new ArrayList<>();
        for (Ingredient i : getAllIngredients()) {
            if (i.getCategory() == cat)
                filtered.add(i);
        }
        return filtered;
    }
}

// ==========================================
// 3. UI Layer (Modernized Swing JFrame)
// ==========================================
public class SmartKioskApp extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Beverage currentBeverage;

    // UI Components for Receipt
    private JTextArea receiptArea;
    private JLabel totalPriceLabel;

    // --- Modern Theme Colors ---
    private final Color COLOR_BG = new Color(245, 243, 240); // Warm Off-white
    private final Color COLOR_PRIMARY = new Color(62, 39, 35); // Deep Coffee Brown
    private final Color COLOR_SECONDARY = new Color(121, 85, 72); // Rich Mocha
    private final Color COLOR_ACCENT = new Color(215, 204, 200); // Latte Tan
    private final Color COLOR_CARD_BORDER = new Color(188, 170, 164); // Visible warm border
    private final Color COLOR_CARD_SHADOW = new Color(0, 0, 0, 25); // Subtle shadow
    private final Color COLOR_SUCCESS = new Color(46, 125, 50); // Rich Green
    private final Color COLOR_DANGER = new Color(198, 40, 40); // Deep Red
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 15);
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_RECEIPT = new Font("Consolas", Font.PLAIN, 15);

    public SmartKioskApp() {
        // 1. Set System Look and Feel for modern native UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Smart Coffee Kiosk");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);

        currentBeverage = new Beverage();

        // Main Layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG);

        // Add Screens
        cardPanel.add(createHomeScreen(), "HOME");
        cardPanel.add(createStandardMenuScreen(), "STANDARD");
        cardPanel.add(createCustomMixScreen(), "CUSTOM");

        // Split Layout: 65% Menu, 35% Receipt
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cardPanel, createReceiptPanel());
        splitPane.setDividerLocation(650);
        splitPane.setDividerSize(0); // Hide the divider line
        splitPane.setBorder(null);

        add(splitPane);
    }

    // --- Modern Button Styler ---
    private JButton createStyledButton(String text, Color bg, Color fg, int radius) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Base background color
                g2.setColor(getBackground());
                if (radius > 0) {
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                } else {
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }

                // Hover effect
                if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 0, 0, 25));
                    if (radius > 0) {
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                    } else {
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                }

                // Press effect
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 0, 0, 40));
                    if (radius > 0) {
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                    } else {
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                }

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Rounded border buttons handle their own border drawing
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.repaint();
            }
        });

        return btn;
    }

    // --- Card-style Panel with Border & Shadow ---
    private Border createCardBorder(Color borderColor, int thickness, int padding) {
        Border line = BorderFactory.createLineBorder(borderColor, thickness);
        Border inner = new EmptyBorder(padding, padding, padding, padding);
        return BorderFactory.createCompoundBorder(line, inner);
    }

    // Shadow border that draws a subtle drop shadow around a component
    private Border createShadowBorder(Color borderColor, int thickness, int padding) {
        Border outerShadow = new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw shadow layers
                for (int i = 3; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 8 * (4 - i)));
                    g2.drawRoundRect(x + i, y + i, w - 1 - i, h - 1 - i, 8, 8);
                }
                // Draw main border
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(thickness));
                g2.drawRoundRect(x, y, w - 1, h - 1, 8, 8);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(4, 4, 6, 6);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
        Border inner = new EmptyBorder(padding, padding, padding, padding);
        return BorderFactory.createCompoundBorder(outerShadow, inner);
    }

    // --- Screen 1: Home Page ---
    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 30));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(150, 100, 150, 100));

        JButton btnStandard = createStyledButton("Standard Menu", COLOR_PRIMARY, Color.WHITE, 30);
        btnStandard.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnStandard.addActionListener(e -> {
            resetOrder();
            cardLayout.show(cardPanel, "STANDARD");
        });

        JButton btnCustom = createStyledButton("Custom Mix", COLOR_SECONDARY, Color.WHITE, 30);
        btnCustom.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnCustom.addActionListener(e -> {
            resetOrder();
            cardLayout.show(cardPanel, "CUSTOM");
        });

        panel.add(btnStandard);
        panel.add(btnCustom);
        return panel;
    }

    // --- Screen 2: Standard Menu ---
    private JPanel createStandardMenuScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setBackground(COLOR_BG);

        JButton backBtn = createStyledButton("<< Back", COLOR_ACCENT, Color.DARK_GRAY, 0);
        header.add(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "HOME"));

        JLabel title = new JLabel("Popular Coffee Recipes");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_PRIMARY);
        header.add(title);

        // Menu Buttons Grid
        JPanel menuGrid = new JPanel(new GridLayout(2, 3, 18, 18));
        menuGrid.setBackground(COLOR_BG);
        menuGrid.setBorder(new EmptyBorder(15, 5, 20, 5));

        addStandardMenuButton(menuGrid, "Espresso", 25, new String[] { "Espresso Shot", "Hot Water" });
        addStandardMenuButton(menuGrid, "Latte", 35, new String[] { "Espresso Shot", "Fresh Milk", "Condensed Milk" });
        addStandardMenuButton(menuGrid, "Cocoa", 35,
                new String[] { "Cocoa Powder", "Hot Water", "Fresh Milk", "Condensed Milk" });
        addStandardMenuButton(menuGrid, "Thai Tea", 35,
                new String[] { "Thai Tea Powder", "Hot Water", "Fresh Milk", "Condensed Milk" });
        addStandardMenuButton(menuGrid, "Italian Soda", 25, new String[] { "Soda Water", "Syrup" });

        // Add-on Section
        JPanel addonPanel = new JPanel(new BorderLayout());
        addonPanel.setBackground(COLOR_BG);

        JLabel addOnTitle = new JLabel(" Extra Toppings ");
        addOnTitle.setFont(FONT_HEADER);
        addOnTitle.setForeground(COLOR_SECONDARY);
        addonPanel.add(addOnTitle, BorderLayout.NORTH);

        JPanel addonGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        addonGrid.setBackground(COLOR_BG);

        for (Ingredient ing : IngredientRepository.getByCategory(Category.TOPPING)) {
            JButton btn = createStyledButton("+ " + ing.getName() + " (" + (int) ing.getPrice() + "฿)", Color.WHITE,
                    COLOR_PRIMARY, 0);
            btn.setBorder(createCardBorder(COLOR_CARD_BORDER, 2, 8));
            btn.setBorderPainted(true);
            btn.addActionListener(e -> {
                currentBeverage.addIngredient(ing);
                updateReceipt();
            });
            addonGrid.add(btn);
        }
        addonPanel.add(addonGrid, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(menuGrid, BorderLayout.CENTER);
        panel.add(addonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addStandardMenuButton(JPanel panel, String menuName, double basePrice, String[] ingredientNames) {
        JButton btn = createStyledButton(
                "<html><center><br><b style='font-size:15px'>" + menuName
                        + "</b><br><br><span style='font-size:13px; color:#795548'>" + basePrice
                        + " THB</span><br><br></center></html>",
                Color.WHITE, COLOR_PRIMARY, 0);

        // Visible card-style border with shadow
        btn.setBorder(createShadowBorder(COLOR_CARD_BORDER, 2, 8));
        btn.setBorderPainted(true);

        btn.addActionListener(e -> {
            resetOrder();
            List<Ingredient> all = IngredientRepository.getAllIngredients();
            for (String name : ingredientNames) {
                for (Ingredient ing : all) {
                    if (ing.getName().equals(name)) {
                        currentBeverage.addIngredient(ing);
                        break;
                    }
                }
            }
            updateReceipt();
        });
        panel.add(btn);
    }

    // --- Screen 3: Custom Mix ---
    private JPanel createCustomMixScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setBackground(COLOR_BG);
        JButton backBtn = createStyledButton("<< Back", COLOR_ACCENT, Color.DARK_GRAY, 0);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "HOME"));
        header.add(backBtn);

        JLabel title = new JLabel("Build Your Drink");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_PRIMARY);
        header.add(title);

        // Tabs for Categories
        UIManager.put("TabbedPane.selected", COLOR_PRIMARY);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        for (Category cat : Category.values()) {
            JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
            catPanel.setBackground(Color.WHITE);
            catPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            List<Ingredient> ingredients = IngredientRepository.getByCategory(cat);

            for (Ingredient ing : ingredients) {
                // Use non-HTML JButton to avoid Swing HTML word-wrap limitations
                JButton btn = createStyledButton("", COLOR_BG, COLOR_PRIMARY, 0);
                btn.setLayout(new BorderLayout());

                JLabel nameLabel = new JLabel(ing.getName(), SwingConstants.CENTER);
                nameLabel.setFont(FONT_BUTTON);
                nameLabel.setForeground(COLOR_PRIMARY);
                nameLabel.setOpaque(false);

                JLabel priceLabel = new JLabel("+" + (int) ing.getPrice() + "฿", SwingConstants.CENTER);
                priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                priceLabel.setForeground(new Color(67, 160, 71));
                priceLabel.setOpaque(false);

                JPanel labelPanel = new JPanel(new GridLayout(2, 1, 0, 2));
                labelPanel.setOpaque(false);
                labelPanel.add(nameLabel);
                labelPanel.add(priceLabel);
                btn.add(labelPanel, BorderLayout.CENTER);

                btn.setPreferredSize(new Dimension(145, 85));
                btn.setBorder(createCardBorder(COLOR_CARD_BORDER, 2, 6));
                btn.setBorderPainted(true);

                btn.addActionListener(e -> {
                    currentBeverage.addIngredient(ing);
                    updateReceipt();
                });
                catPanel.add(btn);
            }

            JScrollPane scrollPane = new JScrollPane(catPanel);
            scrollPane.setBorder(null);
            tabbedPane.addTab("  " + cat.getLabel() + "  ", scrollPane);
        }

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;

    }

    // --- Right Panel: Receipt ---
    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        // Left border acts as separator + padding
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 2, 0, 0, COLOR_CARD_BORDER),
                new EmptyBorder(30, 25, 30, 25)));

        // Receipt Title
        JLabel title = new JLabel("Order Summary", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(COLOR_PRIMARY);

        // Receipt Area (Looks like a real ticket)
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(FONT_RECEIPT);
        receiptArea.setBackground(new Color(254, 254, 250)); // Very light yellow paper
        receiptArea.setForeground(Color.DARK_GRAY);
        receiptArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane receiptScroll = new JScrollPane(receiptArea);
        receiptScroll.setBorder(createCardBorder(COLOR_CARD_BORDER, 1, 0));

        totalPriceLabel = new JLabel("Total: 0.00 ฿", SwingConstants.RIGHT);
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        totalPriceLabel.setForeground(COLOR_PRIMARY);

        // Action Buttons
        JButton payBtn = createStyledButton("Confirm & Pay", COLOR_SUCCESS, Color.WHITE, 0);
        payBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        payBtn.addActionListener(e -> {
            if (currentBeverage.getTotalPrice() > 0) {
                JOptionPane.showMessageDialog(this, "Payment Successful!\nEnjoy your drink", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                resetOrder();
                cardLayout.show(cardPanel, "HOME");
            } else {
                JOptionPane.showMessageDialog(this, "Please select items first.", "Empty Order",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton clearBtn = createStyledButton("Clear", COLOR_DANGER, Color.WHITE, 0);
        clearBtn.addActionListener(e -> resetOrder());

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(totalPriceLabel);

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(clearBtn);
        buttonRow.add(payBtn);

        bottomPanel.add(buttonRow);

        panel.add(title, BorderLayout.NORTH);
        panel.add(receiptScroll, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updateReceipt(); // Initialize empty text
        return panel;
    }

    // --- Logic Methods ---
    private void updateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");
        sb.append("      SMART COFFEE KIOSK      \n");
        sb.append("==============================\n\n");

        List<Ingredient> list = currentBeverage.getIngredients();

        if (list.isEmpty()) {
            sb.append("\n    -- Your Cart is Empty --  \n");
        } else {
            for (Ingredient i : list) {
                String name = i.getName();
                if (name.length() > 20)
                    name = name.substring(0, 17) + "...";
                sb.append(String.format("%-22s %5.0f ฿\n", name, i.getPrice()));
            }
        }

        sb.append("\n------------------------------\n");
        sb.append(String.format("ITEMS: %d\n", list.size()));

        receiptArea.setText(sb.toString());
        totalPriceLabel.setText(String.format("Total: %.2f ฿", currentBeverage.getTotalPrice()));
    }

    private void resetOrder() {
        currentBeverage.clear();
        updateReceipt();
    }

    public static void main(String[] args) {
        // Run application
        SwingUtilities.invokeLater(() -> {
            new SmartKioskApp().setVisible(true);
        });
    }
}