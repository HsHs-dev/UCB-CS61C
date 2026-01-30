/*
 * Decompiled with CFR 0.152.
 */
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;

class StatisticPanel
extends Panel {
    StatisticCanvas SCanvas;
    Button about;
    Frame window;

    StatisticPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.pink);
        this.window = new AboutAFrame();
        this.window.resize(500, 350);
        this.SCanvas = new StatisticCanvas();
        this.about = new Button("  ABout  ");
        this.add("North", new Label("      STATISTICS       "));
        this.add("Center", this.SCanvas);
        this.add("South", this.about);
    }

    public void references() {
        ++StatisticCanvas.num_references;
        this.SCanvas.action();
    }

    public void TLB_Misses() {
        ++StatisticCanvas.num_TLB_Misses;
        this.SCanvas.action();
    }

    public void Page_Fault() {
        ++StatisticCanvas.num_Page_Fault;
        this.SCanvas.action();
    }

    public boolean action(Event event, Object object) {
        if ("  ABout  ".equals(object)) {
            this.aboutAuthor();
            return true;
        }
        return false;
    }

    public void Reset() {
        StatisticCanvas.num_Page_Fault = 0;
        StatisticCanvas.num_references = 0;
        StatisticCanvas.num_TLB_Misses = 0;
        this.SCanvas.action();
    }

    public void paint(Graphics graphics) {
    }

    public void aboutAuthor() {
        this.window.show();
    }
}
