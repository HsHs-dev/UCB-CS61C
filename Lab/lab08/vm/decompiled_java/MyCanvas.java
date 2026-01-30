/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

class MyCanvas
extends Panel {
    static int width;
    static int height;
    public static SimulatorDisplayUtils theDispUtils;
    Graphics offScreenGraphics;
    Image offScreenImage;
    public int drawAll;

    public MyCanvas(int n, int n2) {
        width = n;
        height = n2;
        this.drawAll = -1;
    }

    public SimulatorDisplayUtils createDispUtils() {
        theDispUtils = new SimulatorDisplayUtils();
        return theDispUtils;
    }

    public void switchProcess() {
        this.drawAll = 0;
        this.repaint();
    }

    public void display() {
        this.drawAll = 1;
        this.repaint();
    }

    public void updatePTonly() {
        Graphics graphics = this.getGraphics();
        this.drawAll = 0;
        this.paint(graphics);
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }

    public void paint(Graphics graphics) {
        if (offScreenImage == null) {
            offScreenImage = createImage(width, height);
            offScreenGraphics = offScreenImage.getGraphics();
        }
        offScreenGraphics.setColor(Color.white);
        offScreenGraphics.fillRect(0, 0, width, height);
        theDispUtils.displayLayout(this.drawAll, offScreenGraphics);
        graphics.drawImage(offScreenImage, 0, 0, this);
    }
}
