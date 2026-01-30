/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;

class StatisticCanvas
extends Panel {
    public static int num_references;
    public static int num_Page_Fault;
    public static int num_TLB_Misses;
    static String pageFault;
    String s_TBL;
    String total_Refer;
    String ratio_PageFault;
    String ratio_TLBMisses;
    float ratio_Page;
    float ratio_TLB;

    StatisticCanvas() {
        num_Page_Fault = 0;
        num_TLB_Misses = 0;
        num_references = 0;
        this.total_Refer = "No. of References:  " + num_references;
        pageFault = "Page Faults: " + num_Page_Fault;
        this.s_TBL = "TLB Misses: " + num_TLB_Misses;
        this.ratio_Page = 0.0f;
        this.ratio_TLB = 0.0f;
        this.action();
    }

    public void action() {
        this.total_Refer = "Total References:  " + num_references;
        pageFault = "Page Faults: " + num_Page_Fault;
        this.s_TBL = "TLB Misses: " + num_TLB_Misses;
        if (num_references != 0) {
            int n = (int)((float)num_Page_Fault / (float)num_references * 100.0f);
            this.ratio_Page = (float)n / 100.0f;
            int n2 = (int)((float)num_TLB_Misses / (float)num_references * 100.0f);
            this.ratio_TLB = (float)n2 / 100.0f;
        } else {
            this.ratio_Page = 0.0f;
            this.ratio_TLB = 0.0f;
        }
        this.ratio_PageFault = "Page Fault Ratio: " + this.ratio_Page;
        this.ratio_TLBMisses = "TLB Miss Ratio: " + this.ratio_TLB;
        this.repaint();
    }

    public void paint(Graphics graphics) {
        Font font = new Font("TimesRoman", 0, 12);
        graphics.setFont(font);
        this.setBackground(Color.pink);
        try {
            graphics.drawString(this.total_Refer, 3, 40);
            graphics.drawString(pageFault, 20, 60);
            graphics.drawString(this.s_TBL, 20, 80);
            graphics.drawString(this.ratio_PageFault, 3, 150);
            graphics.drawString(this.ratio_TLBMisses, 3, 170);
            return;
        }
        catch (NullPointerException nullPointerException) {
            System.out.println("paint()");
            return;
        }
    }
}
