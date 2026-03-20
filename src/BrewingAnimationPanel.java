import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

// ==========================================
// Brewing Animation Panel (TaoBin-style)
// ==========================================
public class BrewingAnimationPanel extends JPanel {

    // --- Animation Steps ---
    enum StepType {
        CUP_APPEAR, // แก้วเลื่อนขึ้นมา
        POUR_LIQUID, // เทของเหลว
        ADD_POWDER, // ใส่ผง
        POUR_MILK, // เทนม
        ADD_SYRUP, // เติมน้ำเชื่อม
        ADD_TOPPING, // ใส่ topping
        ADD_FRUIT, // ใส่ผลไม้
        POUR_ALCOHOL, // เทแอลกอฮอล์
        FINISH // เสร็จสิ้น
    }

    static class AnimStep {
        StepType type;
        String label;
        Color color;
        float duration; // seconds

        AnimStep(StepType type, String label, Color color, float duration) {
            this.type = type;
            this.label = label;
            this.color = color;
            this.duration = duration;
        }
    }

    // --- Particle System ---
    static class Particle {
        float x, y, vx, vy, size;
        Color color;
        float life;

        Particle(float x, float y, float vx, float vy, float size, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.size = size;
            this.color = color;
            this.life = 1.0f;
        }
    }

    // --- State ---
    private List<AnimStep> steps;
    private int currentStepIndex;
    private float stepProgress; // 0.0 → 1.0
    private Timer animTimer;
    private Runnable onComplete;

    // Visual state
    private float cupY; // cup y position (animated)
    private float liquidLevel; // 0.0 → 1.0 how full the cup is
    private Color currentLiquidColor; // blended liquid color
    private List<Color> liquidLayers; // all liquid colors added so far
    private List<Particle> particles;
    private List<Particle> sparkles;
    private String statusText;
    private float globalTime; // total elapsed time

    // Theme Colors
    private final Color BG_COLOR = new Color(30, 25, 22); // Dark background
    private final Color BG_GRADIENT = new Color(55, 40, 35); // Gradient end
    private final Color TEXT_COLOR = new Color(255, 248, 240); // Warm white
    private final Color CUP_COLOR = new Color(240, 235, 228); // Cup body
    private final Color CUP_SHADOW = new Color(200, 190, 180); // Cup shadow
    private final Color PROGRESS_BG = new Color(60, 50, 45); // Progress bar bg
    private final Color PROGRESS_FG = new Color(180, 130, 80); // Progress bar fg

    private Random rand = new Random();

    // --- Color Map for Ingredients ---
    private static final Map<String, Color> INGREDIENT_COLORS = new HashMap<>();
    static {
        // BASE
        INGREDIENT_COLORS.put("Hot Water", new Color(173, 216, 230));
        INGREDIENT_COLORS.put("Cold Water", new Color(150, 200, 230));
        INGREDIENT_COLORS.put("Soda Water", new Color(200, 230, 240));
        INGREDIENT_COLORS.put("Espresso Shot", new Color(59, 36, 27));
        INGREDIENT_COLORS.put("Ice", new Color(200, 230, 255));
        INGREDIENT_COLORS.put("Lime Juice", new Color(180, 220, 80));
        INGREDIENT_COLORS.put("Red Bull", new Color(255, 200, 50));
        INGREDIENT_COLORS.put("M-150", new Color(200, 160, 30));
        INGREDIENT_COLORS.put("Orange Juice", new Color(255, 165, 0));
        INGREDIENT_COLORS.put("Black Tea", new Color(100, 60, 30));
        INGREDIENT_COLORS.put("Oolong Tea", new Color(160, 120, 60));
        INGREDIENT_COLORS.put("Coconut Water", new Color(230, 240, 235));
        INGREDIENT_COLORS.put("Bitter Gourd Juice", new Color(100, 130, 50));

        // POWDER
        INGREDIENT_COLORS.put("Cocoa Powder", new Color(90, 47, 25));
        INGREDIENT_COLORS.put("Matcha Powder", new Color(100, 140, 80));
        INGREDIENT_COLORS.put("Thai Tea Powder", new Color(204, 119, 34));
        INGREDIENT_COLORS.put("Charcoal Powder", new Color(40, 40, 40));
        INGREDIENT_COLORS.put("Whey Protein", new Color(245, 235, 200));
        INGREDIENT_COLORS.put("MSG", new Color(255, 255, 250));
        INGREDIENT_COLORS.put("Chili Flakes", new Color(200, 50, 30));
        INGREDIENT_COLORS.put("Wasabi Powder", new Color(130, 180, 70));

        // MILK
        INGREDIENT_COLORS.put("Fresh Milk", new Color(255, 253, 245));
        INGREDIENT_COLORS.put("Oat Milk", new Color(240, 230, 200));
        INGREDIENT_COLORS.put("Condensed Milk", new Color(255, 248, 220));
        INGREDIENT_COLORS.put("Yogurt", new Color(255, 250, 240));
        INGREDIENT_COLORS.put("Almond Milk", new Color(235, 220, 190));

        // SYRUP
        INGREDIENT_COLORS.put("Sugar", new Color(255, 255, 240));
        INGREDIENT_COLORS.put("Syrup", new Color(200, 170, 100));
        INGREDIENT_COLORS.put("Caramel Syrup", new Color(190, 130, 50));
        INGREDIENT_COLORS.put("Honey", new Color(218, 165, 32));
        INGREDIENT_COLORS.put("White Chocolate", new Color(255, 245, 225));
        INGREDIENT_COLORS.put("Strawberry Syrup", new Color(220, 60, 80));
        INGREDIENT_COLORS.put("Vanilla Syrup", new Color(245, 230, 190));
        INGREDIENT_COLORS.put("Mint Syrup", new Color(130, 220, 170));
        INGREDIENT_COLORS.put("Peach Syrup", new Color(255, 200, 150));
        INGREDIENT_COLORS.put("Blue Curacao", new Color(30, 100, 220));
        INGREDIENT_COLORS.put("Fish Sauce", new Color(120, 80, 40));
        INGREDIENT_COLORS.put("Cough Syrup", new Color(60, 20, 30));
        INGREDIENT_COLORS.put("Mala Sauce", new Color(180, 30, 20));

        // TOPPING
        INGREDIENT_COLORS.put("Bubble (Boba)", new Color(30, 20, 15));
        INGREDIENT_COLORS.put("Jelly", new Color(200, 180, 140));
        INGREDIENT_COLORS.put("Whipped Cream", new Color(255, 255, 250));
        INGREDIENT_COLORS.put("Maeng Rak", new Color(20, 20, 20));
        INGREDIENT_COLORS.put("Mint Leaves", new Color(60, 160, 80));
        INGREDIENT_COLORS.put("Raw Egg", new Color(255, 220, 80));
        INGREDIENT_COLORS.put("Coriander", new Color(80, 150, 60));
        INGREDIENT_COLORS.put("Popping Candy", new Color(255, 100, 150));
        INGREDIENT_COLORS.put("Gold Leaf", new Color(255, 215, 0));

        // FRUIT
        INGREDIENT_COLORS.put("Strawberry", new Color(220, 50, 60));
        INGREDIENT_COLORS.put("Avocado", new Color(120, 160, 60));
        INGREDIENT_COLORS.put("Blueberry", new Color(70, 50, 130));
        INGREDIENT_COLORS.put("Coconut", new Color(255, 250, 240));
        INGREDIENT_COLORS.put("Mango", new Color(255, 180, 40));
        INGREDIENT_COLORS.put("Durian", new Color(230, 210, 100));

        // ALCOHOL
        INGREDIENT_COLORS.put("Rum", new Color(150, 80, 30));
        INGREDIENT_COLORS.put("Vodka", new Color(230, 240, 255));
        INGREDIENT_COLORS.put("Whisky", new Color(180, 120, 50));
        INGREDIENT_COLORS.put("Tequila", new Color(255, 245, 200));
        INGREDIENT_COLORS.put("Gin", new Color(210, 230, 240));
        INGREDIENT_COLORS.put("Soju", new Color(230, 240, 250));
    }

    public BrewingAnimationPanel() {
        setBackground(BG_COLOR);
        steps = new ArrayList<>();
        particles = new ArrayList<>();
        sparkles = new ArrayList<>();
        liquidLayers = new ArrayList<>();
        currentLiquidColor = new Color(200, 200, 200, 0); // transparent
        statusText = "";
    }

    // Build animation steps from the beverage ingredients
    public void startBrewing(List<Ingredient> ingredients, Runnable onComplete) {
        this.onComplete = onComplete;
        steps.clear();
        particles.clear();
        sparkles.clear();
        liquidLayers.clear();
        currentStepIndex = 0;
        stepProgress = 0;
        cupY = 1.5f; // off-screen below
        liquidLevel = 0;
        globalTime = 0;

        // Step 1: Cup appears
        steps.add(new AnimStep(StepType.CUP_APPEAR, "Preparing cup...", CUP_COLOR, 1.2f));

        // Build steps from ingredients in order
        for (Ingredient ing : ingredients) {
            Color c = INGREDIENT_COLORS.getOrDefault(ing.getName(), new Color(180, 150, 120));
            Category cat = ing.getCategory();

            switch (cat) {
                case BASE:
                    steps.add(new AnimStep(StepType.POUR_LIQUID, "Pouring " + ing.getName() + "...", c, 1.5f));
                    break;
                case POWDER:
                    steps.add(new AnimStep(StepType.ADD_POWDER, "Adding " + ing.getName() + "...", c, 1.3f));
                    break;
                case MILK:
                    steps.add(new AnimStep(StepType.POUR_MILK, "Pouring " + ing.getName() + "...", c, 1.4f));
                    break;
                case SYRUP:
                    steps.add(new AnimStep(StepType.ADD_SYRUP, "Drizzling " + ing.getName() + "...", c, 1.0f));
                    break;
                case TOPPING:
                    steps.add(new AnimStep(StepType.ADD_TOPPING, "Adding " + ing.getName() + "...", c, 1.3f));
                    break;
                case FRUIT:
                    steps.add(new AnimStep(StepType.ADD_FRUIT, "Adding " + ing.getName() + "...", c, 1.3f));
                    break;
                case ALCOHOL:
                    steps.add(new AnimStep(StepType.POUR_ALCOHOL, "Pouring " + ing.getName() + "...", c, 1.2f));
                    break;
            }
        }

        // Final step
        steps.add(new AnimStep(StepType.FINISH, "Please take your drink.", new Color(255, 215, 0), 2.5f));

        // Start animation timer (~33ms = 30 FPS)
        if (animTimer != null)
            animTimer.stop();
        animTimer = new Timer(33, e -> tick());
        animTimer.start();
    }

    private void tick() {
        if (currentStepIndex >= steps.size()) {
            animTimer.stop();
            // small delay then callback
            Timer delay = new Timer(500, e2 -> {
                ((Timer) e2.getSource()).stop();
                if (onComplete != null)
                    onComplete.run();
            });
            delay.setRepeats(false);
            delay.start();
            return;
        }

        AnimStep step = steps.get(currentStepIndex);
        float dt = 0.033f;
        globalTime += dt;
        stepProgress += dt / step.duration;
        statusText = step.label;

        if (stepProgress >= 1.0f) {
            stepProgress = 1.0f;
            applyStepFinal(step);
            currentStepIndex++;
            stepProgress = 0;
        } else {
            applyStepProgress(step, stepProgress);
        }

        // Update particles
        updateParticles(dt);

        repaint();
    }

    private void applyStepProgress(AnimStep step, float p) {
        float eased = easeOutCubic(p);
        switch (step.type) {
            case CUP_APPEAR:
                cupY = 1.5f - eased * 0.5f; // slide up from 1.5 to 1.0
                break;
            case POUR_LIQUID:
            case POUR_MILK:
            case POUR_ALCOHOL:
                // Increase liquid level
                float addAmount = 0.12f;
                liquidLevel = Math.min(1.0f, liquidLevel + addAmount * 0.033f / step.duration);
                // Add pour particles
                if (rand.nextFloat() < 0.6f) {
                    addPourParticle(step.color);
                }
                blendLiquidColor(step.color, 0.02f);
                break;
            case ADD_POWDER:
                // Powder particles falling
                if (rand.nextFloat() < 0.7f) {
                    addPowderParticle(step.color);
                }
                liquidLevel = Math.min(1.0f, liquidLevel + 0.03f * 0.033f / step.duration);
                blendLiquidColor(step.color, 0.015f);
                break;
            case ADD_SYRUP:
                // Thin stream
                if (rand.nextFloat() < 0.5f) {
                    addSyrupParticle(step.color);
                }
                blendLiquidColor(step.color, 0.01f);
                break;
            case ADD_TOPPING:
            case ADD_FRUIT:
                // Occasional large particles dropping
                if (rand.nextFloat() < 0.15f) {
                    addToppingParticle(step.color);
                }
                break;
            case FINISH:
                // Sparkles
                if (rand.nextFloat() < 0.4f) {
                    addSparkle();
                }
                break;
        }
    }

    private void applyStepFinal(AnimStep step) {
        switch (step.type) {
            case CUP_APPEAR:
                cupY = 1.0f;
                break;
            case POUR_LIQUID:
            case POUR_MILK:
            case POUR_ALCOHOL:
                blendLiquidColor(step.color, 0.3f);
                break;
            case ADD_POWDER:
                blendLiquidColor(step.color, 0.25f);
                break;
            case ADD_SYRUP:
                blendLiquidColor(step.color, 0.15f);
                break;
            default:
                break;
        }
    }

    // --- Particle Generators ---
    private void addPourParticle(Color c) {
        int w = getWidth();
        int h = getHeight();
        float cx = w * 0.5f;
        float topY = h * 0.1f;
        particles.add(new Particle(
                cx + rand.nextFloat() * 10 - 5,
                topY + rand.nextFloat() * 30,
                rand.nextFloat() * 2 - 1,
                3 + rand.nextFloat() * 4,
                3 + rand.nextFloat() * 3,
                varyColor(c, 20)));
    }

    private void addPowderParticle(Color c) {
        int w = getWidth();
        int h = getHeight();
        float cx = w * 0.5f;
        particles.add(new Particle(
                cx + rand.nextFloat() * 60 - 30,
                h * 0.15f + rand.nextFloat() * 20,
                rand.nextFloat() * 3 - 1.5f,
                2 + rand.nextFloat() * 3,
                2 + rand.nextFloat() * 2,
                varyColor(c, 30)));
    }

    private void addSyrupParticle(Color c) {
        int w = getWidth();
        float cx = w * 0.5f;
        float topY = getHeight() * 0.12f;
        particles.add(new Particle(
                cx + rand.nextFloat() * 6 - 3,
                topY,
                rand.nextFloat() * 0.5f - 0.25f,
                4 + rand.nextFloat() * 3,
                2 + rand.nextFloat() * 2,
                varyColor(c, 15)));
    }

    private void addToppingParticle(Color c) {
        int w = getWidth();
        float cx = w * 0.5f;
        particles.add(new Particle(
                cx + rand.nextFloat() * 40 - 20,
                getHeight() * 0.1f,
                rand.nextFloat() * 2 - 1,
                1.5f + rand.nextFloat() * 2,
                6 + rand.nextFloat() * 6,
                varyColor(c, 25)));
    }

    private void addSparkle() {
        int w = getWidth();
        int h = getHeight();
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        sparkles.add(new Particle(
                cx + rand.nextFloat() * 200 - 100,
                cy + rand.nextFloat() * 200 - 100,
                rand.nextFloat() * 2 - 1,
                -1 - rand.nextFloat() * 2,
                2 + rand.nextFloat() * 4,
                new Color(255, 215 + rand.nextInt(40), rand.nextInt(50), 200)));
    }

    private void updateParticles(float dt) {
        // Update and remove dead particles
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.vy += 0.15f; // gravity
            p.x += p.vx;
            p.y += p.vy;
            p.life -= dt * 0.8f;
            if (p.life <= 0 || p.y > getHeight())
                it.remove();
        }

        Iterator<Particle> it2 = sparkles.iterator();
        while (it2.hasNext()) {
            Particle p = it2.next();
            p.x += p.vx;
            p.y += p.vy;
            p.life -= dt * 0.4f;
            if (p.life <= 0)
                it2.remove();
        }
    }

    // --- Drawing ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth();
        int h = getHeight();

        // Background gradient
        GradientPaint bgGrad = new GradientPaint(0, 0, BG_COLOR, 0, h, BG_GRADIENT);
        g2.setPaint(bgGrad);
        g2.fillRect(0, 0, w, h);

        // Ambient glow behind cup
        float glowAlpha = 0.08f + 0.04f * (float) Math.sin(globalTime * 2);
        g2.setColor(new Color(255, 200, 120, (int) (glowAlpha * 255)));
        g2.fillOval(w / 2 - 120, (int) (h * 0.35f), 240, 240);

        // Draw cup
        drawCup(g2, w, h);

        // Draw particles
        for (Particle p : particles) {
            int alpha = Math.max(0, Math.min(255, (int) (p.life * 220)));
            g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
            g2.fillOval((int) (p.x - p.size / 2), (int) (p.y - p.size / 2), (int) p.size, (int) p.size);
        }

        // Draw sparkles (star shapes)
        for (Particle p : sparkles) {
            int alpha = Math.max(0, Math.min(255, (int) (p.life * 255)));
            g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
            drawStar(g2, (int) p.x, (int) p.y, (int) (p.size * 2), (int) p.size);
        }

        // Draw pour stream (if pouring)
        drawPourStream(g2, w, h);

        // Status text
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(statusText);
        // Text shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(statusText, w / 2 - textW / 2 + 1, (int) (h * 0.08f) + 1);
        g2.setColor(TEXT_COLOR);
        g2.drawString(statusText, w / 2 - textW / 2, (int) (h * 0.08f));

        // Progress bar
        drawProgressBar(g2, w, h);

        // Step counter
        if (currentStepIndex < steps.size()) {
            String stepText = "Step " + (currentStepIndex + 1) + " / " + steps.size();
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fm = g2.getFontMetrics();
            g2.setColor(new Color(180, 160, 140));
            g2.drawString(stepText, w / 2 - fm.stringWidth(stepText) / 2, h - 25);
        }

        // FINISH overlay
        if (currentStepIndex < steps.size() && steps.get(currentStepIndex).type == StepType.FINISH) {
            drawFinishOverlay(g2, w, h);
        }

        g2.dispose();
    }

    private void drawCup(Graphics2D g2, int w, int h) {
        // Cup dimensions
        int cupW = 130;
        int cupH = 180;
        int cx = w / 2;
        int baseY = (int) (h * cupY) - cupH / 2;

        // Cup shadow (ellipse below)
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(cx - cupW / 2 - 5, baseY + cupH - 5, cupW + 10, 20);

        // Cup body (trapezoid shape using GeneralPath)
        int topW = cupW;
        int botW = (int) (cupW * 0.75);
        int topX = cx - topW / 2;
        int botX = cx - botW / 2;

        GeneralPath cupPath = new GeneralPath();
        cupPath.moveTo(topX, baseY);
        cupPath.lineTo(topX + topW, baseY);
        cupPath.lineTo(botX + botW, baseY + cupH);
        cupPath.lineTo(botX, baseY + cupH);
        cupPath.closePath();

        // Cup gradient
        GradientPaint cupGrad = new GradientPaint(topX, baseY, CUP_COLOR, topX + topW, baseY, CUP_SHADOW);
        g2.setPaint(cupGrad);
        g2.fill(cupPath);

        // Cup border
        g2.setColor(new Color(180, 170, 160));
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(cupPath);

        // Liquid inside cup
        if (liquidLevel > 0.01f) {
            float level = Math.min(liquidLevel, 0.85f);
            int liqH = (int) (cupH * level);
            int liqY = baseY + cupH - liqH;

            // Calculate liquid width at liqY (interpolation)
            float t = (float) liqH / cupH;
            int liqTopW = (int) (botW + (topW - botW) * (1 - t) * 0.3f + botW * 0.7f * t);
            // More precise: at liqY
            float frac = (float) (liqY - baseY) / cupH;
            int wAtLiqTop = (int) (topW + (botW - topW) * frac);
            int wAtBot = botW;
            int xAtLiqTop = cx - wAtLiqTop / 2;
            int xAtBot = botX;

            GeneralPath liqPath = new GeneralPath();
            liqPath.moveTo(xAtLiqTop + 2, liqY);
            liqPath.lineTo(xAtLiqTop + wAtLiqTop - 2, liqY);
            liqPath.lineTo(xAtBot + wAtBot - 2, baseY + cupH - 2);
            liqPath.lineTo(xAtBot + 2, baseY + cupH - 2);
            liqPath.closePath();

            // Liquid gradient
            Color lc = currentLiquidColor;
            Color lcDark = lc.darker();
            GradientPaint liqGrad = new GradientPaint(cx - 30, liqY, lc, cx + 30, baseY + cupH, lcDark);
            g2.setPaint(liqGrad);
            g2.fill(liqPath);

            // Liquid surface highlight
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillOval(xAtLiqTop + 5, liqY - 3, wAtLiqTop - 10, 8);
        }

        // Cup rim highlight
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(topX + 2, baseY, topX + topW - 2, baseY);
    }

    private void drawPourStream(Graphics2D g2, int w, int h) {
        if (currentStepIndex >= steps.size())
            return;
        AnimStep step = steps.get(currentStepIndex);
        if (step.type != StepType.POUR_LIQUID && step.type != StepType.POUR_MILK
                && step.type != StepType.POUR_ALCOHOL && step.type != StepType.ADD_SYRUP)
            return;

        int cx = w / 2;
        int startY = 0;
        int endY = (int) (h * cupY) - 90;

        // Wavy stream
        g2.setStroke(new BasicStroke(step.type == StepType.ADD_SYRUP ? 3 : 6, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(step.color.getRed(), step.color.getGreen(), step.color.getBlue(), 180));

        GeneralPath stream = new GeneralPath();
        stream.moveTo(cx, startY);
        for (int y = startY; y < endY; y += 5) {
            float wave = (float) Math.sin(y * 0.05 + globalTime * 8) * 3;
            stream.lineTo(cx + wave, y);
        }
        g2.draw(stream);

        // Splash at cup entry
        for (int i = 0; i < 3; i++) {
            int sx = cx + rand.nextInt(20) - 10;
            int sy = endY + rand.nextInt(10);
            int sSize = 2 + rand.nextInt(3);
            g2.setColor(new Color(step.color.getRed(), step.color.getGreen(), step.color.getBlue(), 100));
            g2.fillOval(sx, sy, sSize, sSize);
        }
    }

    private void drawProgressBar(Graphics2D g2, int w, int h) {
        int barW = w - 100;
        int barH = 6;
        int barX = 50;
        int barY = h - 50;

        // Background
        g2.setColor(PROGRESS_BG);
        g2.fillRoundRect(barX, barY, barW, barH, barH, barH);

        // Calculate total progress
        float totalSteps = steps.size();
        float completedProgress = totalSteps > 0 ? (currentStepIndex + stepProgress) / totalSteps : 0;
        int fillW = (int) (barW * Math.min(completedProgress, 1.0f));

        // Fill
        GradientPaint pGrad = new GradientPaint(barX, barY, PROGRESS_FG,
                barX + fillW, barY, new Color(220, 170, 100));
        g2.setPaint(pGrad);
        g2.fillRoundRect(barX, barY, fillW, barH, barH, barH);

        // Glow on progress tip
        if (fillW > 5) {
            g2.setColor(new Color(255, 220, 150, 100));
            g2.fillOval(barX + fillW - 6, barY - 3, 12, 12);
        }
    }

    private void drawFinishOverlay(Graphics2D g2, int w, int h) {
        float p = easeOutCubic(stepProgress);

        // Centered large text
        int fontSize = (int) (40 + 20 * p);
        g2.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        String text = "Enjoy your drink!";
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(text);

        // Glow behind text
        int alpha = (int) (p * 120);
        g2.setColor(new Color(255, 200, 100, alpha));
        g2.fillOval(w / 2 - 100, (int) (h * 0.15f) - 40, 200, 80);

        // Text
        g2.setColor(new Color(255, 230, 180, (int) (p * 255)));
        g2.drawString(text, w / 2 - textW / 2, (int) (h * 0.15f) + 10);
    }

    private void drawStar(Graphics2D g2, int cx, int cy, int outer, int inner) {
        int points = 4;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        double angle = -Math.PI / 2 + globalTime * 3;

        for (int i = 0; i < points * 2; i++) {
            double r = (i % 2 == 0) ? outer : inner;
            xPoints[i] = (int) (cx + r * Math.cos(angle + i * Math.PI / points));
            yPoints[i] = (int) (cy + r * Math.sin(angle + i * Math.PI / points));
        }
        g2.fillPolygon(xPoints, yPoints, points * 2);
    }

    // --- Utility ---
    private void blendLiquidColor(Color newColor, float factor) {
        int r = (int) (currentLiquidColor.getRed() * (1 - factor) + newColor.getRed() * factor);
        int g = (int) (currentLiquidColor.getGreen() * (1 - factor) + newColor.getGreen() * factor);
        int b = (int) (currentLiquidColor.getBlue() * (1 - factor) + newColor.getBlue() * factor);
        int a = Math.min(255, currentLiquidColor.getAlpha() + 5);
        currentLiquidColor = new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)),
                Math.max(0, Math.min(255, a)));
    }

    private Color varyColor(Color c, int range) {
        return new Color(
                clamp(c.getRed() + rand.nextInt(range * 2) - range, 0, 255),
                clamp(c.getGreen() + rand.nextInt(range * 2) - range, 0, 255),
                clamp(c.getBlue() + rand.nextInt(range * 2) - range, 0, 255));
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private float easeOutCubic(float t) {
        return 1 - (1 - t) * (1 - t) * (1 - t);
    }

    public void stopAnimation() {
        if (animTimer != null)
            animTimer.stop();
    }
}
