import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    TOPPING("Topping"),
    FRUIT("Fruit"),
    ALCOHOL("Alcohol");

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

        // --- BASE (น้ำพื้นฐาน) ---
        list.add(new Ingredient("Hot Water", 5, Category.BASE));
        list.add(new Ingredient("Cold Water", 5, Category.BASE));
        list.add(new Ingredient("Soda Water", 15, Category.BASE));
        list.add(new Ingredient("Espresso Shot", 20, Category.BASE));
        list.add(new Ingredient("Ice", 5, Category.BASE));
        list.add(new Ingredient("Lime Juice", 5, Category.BASE));
        list.add(new Ingredient("Red Bull", 20, Category.BASE));
        list.add(new Ingredient("M-150", 15, Category.BASE));
        list.add(new Ingredient("Orange Juice", 15, Category.BASE));
        list.add(new Ingredient("Black Tea", 10, Category.BASE));
        list.add(new Ingredient("Oolong Tea", 15, Category.BASE));
        list.add(new Ingredient("Coconut Water", 20, Category.BASE));
        list.add(new Ingredient("Bitter Gourd Juice", 15, Category.BASE));

        // --- POWDER (ผงชง) ---
        list.add(new Ingredient("Cocoa Powder", 10, Category.POWDER));
        list.add(new Ingredient("Matcha Powder", 15, Category.POWDER));
        list.add(new Ingredient("Thai Tea Powder", 10, Category.POWDER));
        list.add(new Ingredient("Charcoal Powder", 10, Category.POWDER));
        list.add(new Ingredient("Whey Protein", 40, Category.POWDER));
        list.add(new Ingredient("MSG", 5, Category.POWDER));
        list.add(new Ingredient("Chili Flakes", 5, Category.POWDER));
        list.add(new Ingredient("Wasabi Powder", 15, Category.POWDER));

        // --- MILK (นม) ---
        list.add(new Ingredient("Fresh Milk", 10, Category.MILK));
        list.add(new Ingredient("Oat Milk", 20, Category.MILK));
        list.add(new Ingredient("Condensed Milk", 5, Category.MILK));
        list.add(new Ingredient("Yogurt", 15, Category.MILK));
        list.add(new Ingredient("Almond Milk", 25, Category.MILK));

        // --- SYRUP (น้ำเชื่อม/ซอส) ---
        list.add(new Ingredient("Sugar", 0, Category.SYRUP));
        list.add(new Ingredient("Syrup", 0, Category.SYRUP));
        list.add(new Ingredient("Caramel Syrup", 10, Category.SYRUP));
        list.add(new Ingredient("Honey", 15, Category.SYRUP));
        list.add(new Ingredient("White Chocolate", 10, Category.SYRUP));
        list.add(new Ingredient("Strawberry Syrup", 10, Category.SYRUP));
        list.add(new Ingredient("Vanilla Syrup", 10, Category.SYRUP));
        list.add(new Ingredient("Mint Syrup", 10, Category.SYRUP));
        list.add(new Ingredient("Peach Syrup", 15, Category.SYRUP));
        list.add(new Ingredient("Blue Curacao", 15, Category.SYRUP));
        list.add(new Ingredient("Fish Sauce", 5, Category.SYRUP));
        list.add(new Ingredient("Cough Syrup", 25, Category.SYRUP));
        list.add(new Ingredient("Mala Sauce", 15, Category.SYRUP));

        // --- TOPPING (ของตกแต่ง) ---
        list.add(new Ingredient("Bubble (Boba)", 10, Category.TOPPING));
        list.add(new Ingredient("Jelly", 10, Category.TOPPING));
        list.add(new Ingredient("Whipped Cream", 15, Category.TOPPING));
        list.add(new Ingredient("Maeng Rak", 5, Category.TOPPING));
        list.add(new Ingredient("Mint Leaves", 5, Category.TOPPING));
        list.add(new Ingredient("Raw Egg", 10, Category.TOPPING));
        list.add(new Ingredient("Coriander", 5, Category.TOPPING));
        list.add(new Ingredient("Popping Candy", 15, Category.TOPPING));
        list.add(new Ingredient("Gold Leaf", 30, Category.TOPPING));

        // --- FRUIT (ผลไม้) ---
        list.add(new Ingredient("Strawberry", 10, Category.FRUIT));
        list.add(new Ingredient("Avocado", 15, Category.FRUIT));
        list.add(new Ingredient("Blueberry", 15, Category.FRUIT));
        list.add(new Ingredient("Coconut", 15, Category.FRUIT));
        list.add(new Ingredient("Mango", 15, Category.FRUIT));
        list.add(new Ingredient("Durian", 40, Category.FRUIT));

        // --- ALCOHOL (แอลกอฮอล์) ---
        list.add(new Ingredient("Rum", 30, Category.ALCOHOL));
        list.add(new Ingredient("Vodka", 30, Category.ALCOHOL));
        list.add(new Ingredient("Whisky", 20, Category.ALCOHOL));
        list.add(new Ingredient("Tequila", 35, Category.ALCOHOL));
        list.add(new Ingredient("Gin", 30, Category.ALCOHOL));
        list.add(new Ingredient("Soju", 25, Category.ALCOHOL));

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

    // Root layout for switching between main view and full-screen animation
    private JPanel rootPanel;
    private CardLayout rootLayout;
    private BrewingAnimationPanel animPanel;

    // UI Components for Receipt
    private JTextArea receiptArea;
    private JLabel totalPriceLabel;

    // --- Modern Theme Colors ---
    private final Color COLOR_BG = new Color(245, 243, 240); // Warm Off-white
    private final Color COLOR_PRIMARY = new Color(62, 39, 35); // Deep Coffee Brown
    private final Color COLOR_SECONDARY = new Color(121, 85, 72); // Rich Mocha
    private final Color COLOR_ACCENT = new Color(215, 204, 200); // Latte Tan
    private final Color COLOR_CARD_BORDER = new Color(188, 170, 164); // Visible warm border
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

        // Root layout: switches between main view and full-screen animation
        rootLayout = new CardLayout();
        rootPanel = new JPanel(rootLayout);
        rootPanel.add(splitPane, "MAIN");

        animPanel = new BrewingAnimationPanel();
        rootPanel.add(animPanel, "ANIMATION");

        add(rootPanel);
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

        JLabel title = new JLabel("Recommend Menu");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_PRIMARY);
        header.add(title);

        // Tabbed Pane for 4 groups
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        // === กลุ่ม 1: Popular Menu (15 เมนู) ===
        JPanel popularGrid = createMenuGrid();
        addStandardMenuButton(popularGrid, "Espresso", 25.0, new String[] { "Espresso Shot", "Hot Water" });
        addStandardMenuButton(popularGrid, "Americano", 30.0, new String[] { "Espresso Shot", "Cold Water", "Ice" });
        addStandardMenuButton(popularGrid, "Latte", 35.0,
                new String[] { "Espresso Shot", "Fresh Milk", "Condensed Milk" });
        addStandardMenuButton(popularGrid, "Caramel Macchiato", 55.0,
                new String[] { "Espresso Shot", "Fresh Milk", "Vanilla Syrup", "Caramel Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Oat Milk Latte", 45.0,
                new String[] { "Espresso Shot", "Oat Milk", "Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Cocoa", 30.0,
                new String[] { "Cocoa Powder", "Hot Water", "Fresh Milk", "Condensed Milk" });
        addStandardMenuButton(popularGrid, "Thai Tea", 25.0,
                new String[] { "Thai Tea Powder", "Hot Water", "Fresh Milk", "Sugar" });
        addStandardMenuButton(popularGrid, "Matcha Latte", 35.0,
                new String[] { "Matcha Powder", "Hot Water", "Fresh Milk", "Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Peach Tea", 30.0, new String[] { "Black Tea", "Peach Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Italian Soda", 20.0, new String[] { "Soda Water", "Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Honey Lime Soda", 40.0,
                new String[] { "Soda Water", "Lime Juice", "Honey", "Ice" });
        addStandardMenuButton(popularGrid, "Strawberry Smoothie", 25.0,
                new String[] { "Strawberry", "Strawberry Syrup", "Syrup", "Ice" });
        addStandardMenuButton(popularGrid, "Avocado Milk", 35.0,
                new String[] { "Avocado", "Fresh Milk", "Condensed Milk", "Ice" });
        addStandardMenuButton(popularGrid, "Mango Smoothie", 35.0,
                new String[] { "Mango", "Fresh Milk", "Condensed Milk", "Ice" });
        addStandardMenuButton(popularGrid, "Coconut Water Refresh", 40.0,
                new String[] { "Coconut Water", "Coconut", "Ice" });
        tabbedPane.addTab("Popular Menu", createScrollableTab(popularGrid));

        // === กลุ่ม 2: Alcohol & Party (10 เมนู) ===
        JPanel alcoholGrid = createMenuGrid();
        addStandardMenuButton(alcoholGrid, "Rum Mojito", 60.0,
                new String[] { "Rum", "Soda Water", "Lime Juice", "Mint Leaves", "Syrup", "Ice" });
        addStandardMenuButton(alcoholGrid, "Oolong Whisky", 40.0, new String[] { "Whisky", "Oolong Tea", "Ice" });
        addStandardMenuButton(alcoholGrid, "Vodka Red Bull", 55.0, new String[] { "Vodka", "Red Bull", "Ice" });
        addStandardMenuButton(alcoholGrid, "Screwdriver", 50.0, new String[] { "Vodka", "Orange Juice", "Ice" });
        addStandardMenuButton(alcoholGrid, "Tequila Sunrise", 65.0,
                new String[] { "Tequila", "Orange Juice", "Strawberry Syrup", "Ice" });
        addStandardMenuButton(alcoholGrid, "Gin Tonic (DIY)", 55.0,
                new String[] { "Gin", "Soda Water", "Lime Juice", "Ice" });
        addStandardMenuButton(alcoholGrid, "Soju Yogurt", 45.0, new String[] { "Soju", "Yogurt", "Syrup", "Ice" });
        addStandardMenuButton(alcoholGrid, "Black Russian", 55.0,
                new String[] { "Vodka", "Espresso Shot", "Syrup", "Ice" });
        addStandardMenuButton(alcoholGrid, "Blue Ocean Margarita", 60.0,
                new String[] { "Tequila", "Blue Curacao", "Lime Juice", "Ice" });
        addStandardMenuButton(alcoholGrid, "Durian Bomb", 80.0,
                new String[] { "Soju", "Durian", "Fresh Milk", "Ice" });
        tabbedPane.addTab("Alcohol & Party", createScrollableTab(alcoholGrid));

        // === กลุ่ม 3: Geek / Tech / Engineer (15 เมนู) ===
        JPanel geekGrid = createMenuGrid();
        addStandardMenuButton(geekGrid, "Engine Oil", 45.0,
                new String[] { "Espresso Shot", "Charcoal Powder", "Raw Egg", "Ice" });
        addStandardMenuButton(geekGrid, "Deadline Juice", 100.0,
                new String[] { "Espresso Shot", "Espresso Shot", "M-150", "Whey Protein", "Ice" });
        addStandardMenuButton(geekGrid, "Alar Akbar", 40.0,
                new String[] { "Soda Water", "Lime Juice", "Popping Candy", "Chili Flakes" });
        addStandardMenuButton(geekGrid, "Night Potion", 35.0,
                new String[] { "Cold Water", "Blue Curacao", "Bubble (Boba)", "Ice" });
        addStandardMenuButton(geekGrid, "Nam Tom", 65.0, new String[] { "M-150", "Vodka", "Wasabi Powder", "Ice" });
        addStandardMenuButton(geekGrid, "Cringe Potion", 70.0,
                new String[] { "Tequila", "Mala Sauce", "Soda Water", "Lime Juice" });
        addStandardMenuButton(geekGrid, "Autonomous Harvest", 90.0,
                new String[] { "Durian", "Mango", "Coconut", "Yogurt", "Ice" });
        addStandardMenuButton(geekGrid, "Fresh Doesn't Fresh", 60.0,
                new String[] { "Black Tea", "Honey", "Gold Leaf", "Ice" });
        addStandardMenuButton(geekGrid, "PrePro Dreaming", 20.0, new String[] { "Black Tea", "Cold Water", "Ice" });
        addStandardMenuButton(geekGrid, "NullPointerException", 15.0, new String[] { "Cold Water", "Ice", "Ice" });
        addStandardMenuButton(geekGrid, "Blue Screen", 65.0,
                new String[] { "Blue Curacao", "Vodka", "Soda Water", "Ice" });
        addStandardMenuButton(geekGrid, "Liquid Cooling", 45.0, new String[] { "Espresso Shot", "Red Bull", "Ice" });
        addStandardMenuButton(geekGrid, "Shrek Potion", 40.0,
                new String[] { "Matcha Powder", "Strawberry Syrup", "Fresh Milk", "Ice" });
        addStandardMenuButton(geekGrid, "Infinite Loop", 120.0,
                new String[] { "Vodka", "Whisky", "Rum", "Tequila", "Ice" });
        addStandardMenuButton(geekGrid, "Whey Protein", 60.0,
                new String[] { "Matcha Powder", "Espresso Shot", "Oat Milk", "Ice" });
        tabbedPane.addTab("Geek / Tech", createScrollableTab(geekGrid));

        // === กลุ่ม 4: Meme / Cursed / Gamer (10 เมนู) ===
        JPanel memeGrid = createMenuGrid();
        addStandardMenuButton(memeGrid, "Trust Issues", 35.0,
                new String[] { "Fresh Milk", "Lime Juice", "Fish Sauce", "Soda Water" });
        addStandardMenuButton(memeGrid, "Respawn Potion", 60.0,
                new String[] { "Cough Syrup", "Strawberry Syrup", "Soda Water", "Jelly" });
        addStandardMenuButton(memeGrid, "Survival Kit", 65.0, new String[] { "M-150", "Rum", "Honey", "Ice" });
        addStandardMenuButton(memeGrid, "Umami Espresso", 30.0, new String[] { "Espresso Shot", "MSG", "Hot Water" });
        addStandardMenuButton(memeGrid, "Diabetes in a Cup", 55.0, new String[] { "Condensed Milk", "Sugar",
                "Caramel Syrup", "Honey", "White Chocolate", "Whipped Cream" });
        addStandardMenuButton(memeGrid, "Premium Diet Water", 15.0, new String[] { "Cold Water", "Ice", "Ice" });
        addStandardMenuButton(memeGrid, "Bitter Truth", 50.0,
                new String[] { "Bitter Gourd Juice", "Matcha Powder", "Espresso Shot" });
        addStandardMenuButton(memeGrid, "Thai Street Food", 30.0,
                new String[] { "Thai Tea Powder", "Fresh Milk", "Coriander", "Ice" });
        addStandardMenuButton(memeGrid, "The Morning After", 60.0, new String[] { "Raw Egg", "Red Bull", "Vodka" });
        addStandardMenuButton(memeGrid, "Recovery Potion", 30.0,
                new String[] { "Hot Water", "Honey", "Lime Juice", "Mint Leaves" });
        tabbedPane.addTab("Meme / Cursed", createScrollableTab(memeGrid));

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    // Helper: create a FlowLayout grid for menu items
    private JPanel createMenuGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 15, 15));
        grid.setBackground(COLOR_BG);
        grid.setBorder(new EmptyBorder(15, 10, 15, 10));
        return grid;
    }

    // Helper: wrap a grid panel in a scrollable pane
    private JScrollPane createScrollableTab(JPanel gridPanel) {
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private void addStandardMenuButton(JPanel panel, String menuName, double menuPrice, String[] ingredientNames) {
        JButton btn = createStyledButton(
                "<html><center><br><b style='font-size:14px'>" + menuName
                        + "</b><br><br><span style='font-size:13px; color:#795548'>" + (int) menuPrice
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
                // Switch to full-screen animation
                List<Ingredient> orderCopy = new ArrayList<>(currentBeverage.getIngredients());
                rootLayout.show(rootPanel, "ANIMATION");
                animPanel.startBrewing(orderCopy, () -> {
                    // Animation complete callback
                    SwingUtilities.invokeLater(() -> {
                        resetOrder();
                        cardLayout.show(cardPanel, "HOME");
                        rootLayout.show(rootPanel, "MAIN");
                    });
                });
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