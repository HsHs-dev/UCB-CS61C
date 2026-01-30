/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;

class Controls
extends Panel {
    MyCanvas target;
    CNEControls secondCtrl;
    Scheduler theScheduler;
    StatisticPanel statisticResult;
    Button stopButton;
    Button startButton;
    Button resetButton;
    Button stepButton;
    Label l;

    public Controls(MyCanvas myCanvas, Scheduler scheduler, CNEControls cNEControls, StatisticPanel statisticPanel) {
        this.target = myCanvas;
        this.statisticResult = statisticPanel;
        this.theScheduler = scheduler;
        this.secondCtrl = cNEControls;
        this.setLayout(new FlowLayout());
        this.setBackground(Color.lightGray);
        myCanvas.setForeground(Color.black);
        this.l = new Label("                                  ");
        this.startButton = new Button("   Start  ");
        this.stopButton = new Button("   Stop   ");
        this.resetButton = new Button("   Reset  ");
        new Font("TimesRoman", 1, 20);
        this.stepButton = new Button("   Step   ");
        this.add(this.startButton);
        this.add(this.stopButton);
        this.add(this.resetButton);
        this.add(this.stepButton);
        this.add(this.secondCtrl);
    }

    public void paint(Graphics graphics) {
        Rectangle rectangle = this.bounds();
        graphics.setColor(Color.lightGray);
        graphics.drawRect(0, 0, rectangle.width, rectangle.height);
    }

    public boolean action(Event event, Object object) {
        if ("   Start  ".equals(object) || " Play ".equals(object)) {
            this.theScheduler.start();
            return true;
        }
        if ("   Stop   ".equals(object)) {
            this.theScheduler.stop();
            this.startButton.setLabel(" Play ");
            return true;
        }
        if ("   Reset  ".equals(object)) {
            this.startButton.setLabel("   Start  ");
            this.theScheduler.resetSim();
            this.secondCtrl.reset_Scrollbar();
            Scheduler.slider_num_one = 1000;
            Scheduler.slider_num_two = 500;
            this.statisticResult.Reset();
            return true;
        }
        if ("   Step   ".equals(object)) {
            this.theScheduler.start();
            Scheduler.slider_num_one = 1000;
            Scheduler.slider_num_two = 500;
            try {
                Thread.sleep(Scheduler.slider_num_two);
            }
            catch (InterruptedException interruptedException) {}
            this.theScheduler.stop();
            return true;
        }
        return false;
    }
}
