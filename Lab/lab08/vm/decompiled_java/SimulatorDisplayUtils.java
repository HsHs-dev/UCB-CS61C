/*
 * Decompiled with CFR 0.152.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class SimulatorDisplayUtils
extends VMS {
    private int canvasWidth = 552;
    private int canvasHeight = 279;
    private int originX = 10;
    private int originY = 10;
    private int MMUoX;
    private int MMUoY;
    private int MMUwidth;
    private int MMUheight;
    private final int MMU_BOX_HEIGHT = 350;
    private Color C_SHADOW = new Color(200, 200, 200);
    private Color C_PROCESS_RUN = new Color(100, 210, 250);
    private Color C_PROCESS_SLEEP = new Color(215, 235, 250);
    private final int SHADOW_WIDTH = 4;
    private final int SHADOW_HEIGHT = 4;
    private final int TOTAL_PROCS = 4;
    private final int PROCESS_WIDTH = 40;
    private final int PROCESS_HEIGHT = 50;
    private int processOriginX = 40;
    private int processOriginY = 2;
    private int processSeparation;
    private final Color C_KERNEL_BG = new Color(200, 255, 196);
    private int kernelX;
    private int kernelY;
    private final int KERNEL_WIDTH = 80;
    private final int KERNEL_HEIGHT = 25;
    private final int TOTAL_TLB_ENTRIES = 4;
    private final int TLB_WIDTH = 100;
    private final int TLB_HEIGHT = 20;
    private int TLBoX;
    private int TLBoY;
    private final Color C_TLB_BG = new Color(240, 240, 180);
    private final int TOTAL_PT_ENTRIES = 32;
    private final int PT_WIDTH = 15;
    private final int PT_HEIGHT = 40;
    private int PToX;
    private int PToY;
    private final Color C_PT_BG = new Color(255, 240, 200);
    private final int TOTAL_MM_CELLS = 16;
    private final int MM_WIDTH = 25;
    private final int MM_HEIGHT = 25;
    private int MMoX;
    private int MMoY;
    private final Color C_MM_BG = new Color(255, 195, 180);
    private final int HD_WIDTH = 100;
    private final int HD_HEIGHT = 60;
    private int HDoX;
    private int HDoY;
    private final Color C_HD_BG = new Color(255, 240, 210);
    private final Color C_HD_LINES = new Color(140, 100, 80);
    private final Color C_CONN_PASSIVE = new Color(220, 220, 220);
    private final Color C_CONN_ACTIVE = new Color(255, 0, 0);
    private Color C_HIT = new Color(0, 255, 0);
    private Color C_MISS = new Color(255, 0, 0);
    private Graphics theG;
    private final String[] procStr = new String[]{"P1", "P2", "P3", "P4"};
    private final String[] numStr = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    StatisticPanel s_Statistic;
    int firstTime;

    public SimulatorDisplayUtils() {
        this.processSeparation = (this.canvasWidth - 80 - 160) / 3;
        this.kernelX = this.originX + this.canvasWidth / 2 - 40;
        this.kernelY = this.processOriginY + 50 + 25;
        this.MMUoX = this.originX + 15;
        this.MMUoY = this.processOriginY + 50 + 80;
        this.MMUwidth = this.canvasWidth - 20;
        this.MMUheight = this.MMUoY + 350;
        this.TLBoX = this.MMUoX + 20;
        this.TLBoY = this.MMUoY + 100;
        this.PToX = this.MMUoX + 20;
        this.PToY = this.TLBoY + 180;
        this.MMoX = this.MMUoX + 100 + 200;
        this.MMoY = this.MMUoY + 20;
        this.HDoX = this.MMUoX + 100 + 200;
        this.HDoY = this.MMUoY + 160;
    }

    public void changeProcess() {
        int n = VMS.theScheduler.getPID();
        if (n == 0) {
            this.C_PROCESS_RUN = new Color(0, 153, 255);
            this.C_HIT = new Color(0, 153, 255);
        }
        if (n == 1) {
            this.C_PROCESS_RUN = new Color(255, 255, 153);
            this.C_HIT = new Color(255, 255, 153);
        }
        if (n == 2) {
            this.C_PROCESS_RUN = new Color(0, 255, 153);
            this.C_HIT = new Color(0, 255, 153);
        }
        if (n == 3) {
            this.C_PROCESS_RUN = new Color(255, 51, 0);
            this.C_HIT = new Color(255, 51, 0);
        }
    }

    private void delay(long l) {
        long l2 = System.currentTimeMillis();
        while (System.currentTimeMillis() < l2 + l) {
        }
    }

    public void displayLayout(int n, Graphics graphics) {
        int n2;
        this.theG = graphics;
        if (n == 0) {
            this.displayPT(VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getCurPTI(), false);
        }
        if (n == -1) {
            this.drawShadow(this.MMUoX, this.MMUoY, this.MMUwidth, 350);
            this.theG.setColor(Color.black);
            this.theG.drawRect(this.MMUoX, this.MMUoY, this.MMUwidth, 350);
            ++this.firstTime;
            this.displayKernel();
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("  MMU  ", this.kernelX + 15, this.kernelY + 17);
            this.joinKernelToTLB(false);
            n2 = 0;
            while (n2 < 4) {
                this.displayProcess(n2, false);
                this.theG.setFont(new Font("TimesRoman", 1, 14));
                this.theG.drawString(this.procStr[n2], this.processOriginX + n2 * (40 + this.processSeparation) + 20 - 3, this.processOriginY + 50 + 20);
                ++n2;
            }
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("TLB", this.TLBoX + 35, this.TLBoY - 10);
            n2 = 0;
            while (n2 < 4) {
                this.displayTLB(n2, false);
                ++n2;
            }
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("Page Table", this.PToX + 200, this.PToY - 10);
            this.drawShadow(this.PToX, this.PToY, 480, 40);
            n2 = 0;
            while (n2 < 32) {
                this.displayPT(n2, false);
                ++n2;
            }
            n2 = 0;
            while (n2 < 32) {
                this.displayPTIndex(n2);
                ++n2;
            }
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("Memory", this.MMoX + 60, this.MMoY + 120);
            this.drawShadow(this.MMoX, this.MMoY, 100, 100);
            n2 = 0;
            while (n2 < 16) {
                this.displayMM(n2, false, -1);
                ++n2;
            }
            this.displayHD();
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.setColor(Color.black);
            this.theG.drawString("Hard Disk", this.HDoX + 20, this.HDoY + 30);
            this.joinProcessToKernel(true, VMS.getRunningProc());
            n2 = 0;
            while (n2 < 4) {
                if (n2 != VMS.getRunningProc()) {
                    this.joinProcessToKernel(false, n2);
                }
                ++n2;
            }
            this.joinMMToKernel(false);
            this.joinTLBToPT(false);
            this.joinPTToMM(false);
            this.joinTLBToMM(false);
            this.joinHDToMM(false);
            this.joinProcessToKernel(false, 0);
            this.joinProcessToKernel(false, 1);
            this.joinProcessToKernel(false, 2);
            this.joinProcessToKernel(false, 3);
            this.theG.setColor(Color.white);
            this.theG.fillRect(this.TLBoX + 40, this.TLBoY + 90, 100, 40);
            this.theG.fillRect(this.PToX + 350, this.PToY - 23, 150, 20);
            this.theG.setColor(Color.white);
            this.theG.fillRect(this.MMoX + 130, this.MMoY + 70, 100, 50);
        }
        if (n == 1) {
            if (VMS.getSwitchProcess()) {
                this.changeProcess();
                n2 = 0;
                while (n2 < 4) {
                    this.displayTLB(n2, false);
                    ++n2;
                }
                n2 = 0;
                while (n2 < 32) {
                    this.displayPT(n2, false);
                    ++n2;
                }
            }
            this.displayKernel();
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("  MMU  ", this.kernelX + 15, this.kernelY + 17);
            this.joinMMToKernel(true);
            this.displayProcess(VMS.getLastRunningProc(), false);
            this.joinProcessToKernel(false, VMS.getLastRunningProc());
            this.displayProcess(VMS.getRunningProc(), true);
            this.joinProcessToKernel(true, VMS.getRunningProc());
            this.joinHDToMM(false);
            this.joinKernelToTLB(true);
            if (VMS.getTlbState()) {
                this.theG.setColor(Color.white);
                this.theG.fillRect(this.PToX + 350, this.PToY - 23, 150, 20);
                this.theG.fillRect(this.MMoX + 130, this.MMoY + 70, 100, 50);
                int n3 = 0;
                while (n3 < 4) {
                    this.displayTLB(n3, false);
                    ++n3;
                }
                this.displayTLB(this.getTlbHitIndex(), true);
                this.joinTLBToPT(false);
                this.joinTLBToMM(true);
                this.joinPTToMM(false);
                this.displayHitMM(VMS.getCurMM(), VMS.getMMvPageFrame());
                this.theG.setColor(Color.white);
                this.theG.fillRect(this.TLBoX + 40, this.TLBoY + 90, 100, 40);
                this.theG.setColor(Color.blue);
                this.theG.setFont(new Font("TimesRoman", 1, 14));
                this.theG.drawString("TLB hit", this.TLBoX + 45, this.TLBoY + 110);
                this.theG.setFont(new Font("TimesRoman", 1, 12));
                return;
            }
            int n4 = 0;
            while (n4 < 4) {
                this.displayTLB(n4, false);
                ++n4;
            }
            this.joinTLBToPT(true);
            this.joinTLBToMM(false);
            this.theG.setColor(Color.white);
            this.theG.fillRect(this.TLBoX + 40, this.TLBoY + 90, 100, 40);
            this.theG.setColor(Color.red);
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString("TLB miss", this.TLBoX + 45, this.TLBoY + 110);
            if (VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getPtState()) {
                this.theG.setFont(new Font("TimesRoman", 1, 12));
                this.theG.setColor(Color.red);
                this.theG.drawString("         ", this.MMoX + 135, this.MMoY + 80);
                this.theG.drawString("           ", this.MMoX + 135, this.MMoY + 100);
                this.displayPT(VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getLastPTI(), false);
                this.displayPT(VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getCurPTI(), true);
                this.joinPTToMM(true);
                this.displayHitMM(VMS.getCurMM(), VMS.getMMvPageFrame());
                this.theG.setColor(Color.white);
                this.theG.fillRect(this.PToX + 350, this.PToY - 23, 150, 20);
                this.theG.setFont(new Font("TimesRoman", 1, 14));
                this.theG.setColor(Color.blue);
                this.theG.drawString("Page Table hit", this.PToX + 360, this.PToY - 10);
                this.theG.setColor(Color.white);
                this.theG.fillRect(this.MMoX + 130, this.MMoY + 70, 100, 50);
                return;
            }
            this.joinPTToMM(false);
            this.joinTLBToPT(false);
            this.joinKernelToTLB(false);
            this.joinHDToMM(true);
            this.theG.setColor(Color.white);
            this.theG.fillRect(this.PToX + 350, this.PToY - 23, 150, 20);
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.setColor(Color.red);
            this.theG.drawString("Page Fault", this.PToX + 360, this.PToY - 10);
            this.theG.setColor(Color.white);
            this.theG.fillRect(this.MMoX + 130, this.MMoY + 70, 100, 50);
            this.theG.setFont(new Font("TimesRoman", 1, 12));
            this.theG.setColor(Color.red);
            this.theG.drawString("Paging in", this.MMoX + 135, this.MMoY + 80);
            this.theG.drawString("Updating PT", this.MMoX + 135, this.MMoY + 100);
            this.displayMM(VMS.getCurMM(), true, VMS.getMMvPageFrame());
            this.displayPT(VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getLastPTI(), false);
            this.displayPT(VMS.theScheduler.theMMU[VMS.theScheduler.getPID()].getCurPTI(), false);
        }
    }

    protected void displayProcess(int n, boolean bl) {
        int n2 = this.processOriginX + n * (40 + this.processSeparation);
        int n3 = this.processOriginY;
        this.theG.setFont(new Font("TimesRoman", 1, 14));
        if (n == 0) {
            this.C_PROCESS_SLEEP = new Color(0, 153, 255);
        }
        if (n == 1) {
            this.C_PROCESS_SLEEP = new Color(255, 255, 153);
        }
        if (n == 2) {
            this.C_PROCESS_SLEEP = new Color(0, 255, 153);
        }
        if (n == 3) {
            this.C_PROCESS_SLEEP = new Color(255, 51, 0);
        }
        if (bl) {
            this.theG.setColor(this.C_PROCESS_RUN);
            this.theG.fillRect(n2, n3, 40, 50);
            this.drawShadow(n2, n3, 40, 50);
            this.theG.setColor(Color.black);
            this.theG.drawRect(n2, n3, 40, 50);
            this.theG.setColor(Color.black);
            this.theG.drawString("Exec ", n2 + 5, n3 + 15);
            this.theG.setFont(new Font("TimesRoman", 1, 14));
            this.theG.drawString(Integer.toString(VMS.gimmePageNum), n2 + 13, n3 + 35);
        } else {
            this.theG.setColor(this.C_PROCESS_SLEEP);
            this.theG.fillRect(n2, n3, 40, 50);
            this.drawShadow(n2, n3, 40, 50);
            this.theG.setColor(Color.black);
            this.theG.drawRect(n2, n3, 40, 50);
            this.theG.setFont(new Font("TimesRoman", 1, 10));
            this.theG.drawString("Sleep", n2 + 10, n3 + 30);
        }
        this.theG.setFont(new Font("TimesRoman", 1, 14));
        this.theG.setColor(Color.black);
    }

    public void displayKernel() {
        this.theG.setColor(this.C_KERNEL_BG);
        this.theG.fillRect(this.kernelX, this.kernelY, 80, 25);
        this.drawShadow(this.kernelX, this.kernelY, 80, 25);
        this.theG.setColor(Color.black);
        this.theG.drawRect(this.kernelX, this.kernelY, 80, 25);
    }

    public void displayTLB(int n, boolean bl) {
        int n2 = this.TLBoX;
        int n3 = this.TLBoY + n * 20;
        if (bl) {
            this.theG.setColor(this.C_HIT);
        } else {
            this.theG.setColor(this.C_TLB_BG);
        }
        this.theG.fillRect(n2, n3, 100, 20);
        if (n == 3) {
            this.drawShadow(n2, n3, 100, 20);
        }
        this.theG.setColor(this.C_SHADOW);
        this.theG.fillRect(n2 + 100 + 1, n3 + 4, 4, 20);
        this.theG.setColor(Color.black);
        this.theG.drawRect(n2, n3, 100, 20);
        this.theG.drawLine(n2 + 50, n3, n2 + 50, n3 + 20);
        this.theG.setColor(Color.black);
        this.theG.setFont(new Font("TimesRoman", 1, 14));
        VMS.theScheduler.getPID();
        if (VMS.TLB_TAG[n] == -1) {
            this.theG.drawString("E", n2 + 20, n3 + 15);
            this.theG.drawString("E", n2 + 70, n3 + 15);
            return;
        }
        VMS.theScheduler.getPID();
        this.theG.drawString(Integer.toString(VMS.TLB_TAG[n]), n2 + 20, n3 + 15);
        VMS.theScheduler.getPID();
        this.theG.drawString(Integer.toString(VMS.TLB_DATA[n]), n2 + 70, n3 + 15);
    }

    public void displayPTIndex(int n) {
        int n2 = this.PToX + n * 15;
        int n3 = this.PToY + 18;
        this.theG.setColor(Color.black);
        this.theG.setFont(new Font("TimesRoman", 0, 10));
        this.theG.drawString(Integer.toString(n), n2 + 4, n3 + 35);
    }

    public void displayPT(int n, boolean bl) {
        int n2 = VMS.theScheduler.getPID();
        int n3 = this.PToX + n * 15;
        int n4 = this.PToY;
        if (bl) {
            this.theG.setColor(this.C_HIT);
        } else {
            this.theG.setColor(this.C_PT_BG);
        }
        this.theG.fillRect(n3, n4, 15, 40);
        this.theG.setColor(Color.black);
        this.theG.drawRect(n3, n4, 15, 40);
        this.theG.setColor(Color.black);
        this.theG.setFont(new Font("TimesRoman", 0, 10));
        this.theG.drawString(Integer.toString(VMS.theScheduler.theMMU[n2].PT_STATE[n]), n3 + 4, n4 + 10);
        this.theG.drawLine(n3, n4 + 15, n3 + 15, n4 + 15);
        if (VMS.theScheduler.theMMU[n2].PT_NUM[n] == -1) {
            this.theG.drawString("E", n3 + 4, n4 + 35);
            return;
        }
        this.theG.drawString(Integer.toString(VMS.theScheduler.theMMU[n2].PT_NUM[n]), n3 + 4, n4 + 35);
    }

    public void displayHitMM(int n, int n2) {
        int n3 = this.MMoX + n % 4 * 25;
        int n4 = n == 0 ? this.MMoY : this.MMoY + n / 4 * 25;
        this.theG.setFont(new Font("TimesRoman", 0, 10));
        int n5 = 0;
        while (n5 < 4) {
            this.theG.setColor(Color.black);
            this.theG.drawRect(n3, n4, 25, 25);
            this.theG.setColor(this.C_HIT);
            this.theG.fillRect(n3, n4, 25, 25);
            this.theG.setColor(Color.white);
            this.theG.fillRect(n3, n4, 25, 25);
            this.delay(20L);
            this.theG.setColor(this.C_HIT);
            this.theG.fillRect(n3, n4, 25, 25);
            ++n5;
        }
        this.theG.setColor(Color.black);
        this.theG.drawRect(n3, n4, 25, 25);
        this.theG.drawString(Integer.toString(n), n3 + 4, n4 + 10);
        this.theG.setFont(new Font("TimesRoman", 0, 12));
        if (n2 < 0) {
            this.theG.drawString(" ", n3 + 8, n4 + 21);
            return;
        }
        this.theG.drawString(Integer.toString(n2), n3 + 8, n4 + 21);
    }

    public void displayMM(int n, boolean bl, int n2) {
        int n3 = this.MMoX + n % 4 * 25;
        int n4 = n == 0 ? this.MMoY : this.MMoY + n / 4 * 25;
        this.theG.setFont(new Font("TimesRoman", 0, 10));
        if (bl) {
            this.theG.setColor(this.C_HIT);
        } else {
            this.theG.setColor(this.C_MM_BG);
        }
        this.theG.fillRect(n3, n4, 25, 25);
        this.theG.setColor(Color.black);
        this.theG.drawRect(n3, n4, 25, 25);
        this.theG.drawString(Integer.toString(n), n3 + 4, n4 + 10);
        this.theG.setFont(new Font("TimesRoman", 0, 12));
        if (n2 < 0) {
            this.theG.drawString(" ", n3 + 8, n4 + 21);
            return;
        }
        this.theG.drawString(Integer.toString(n2), n3 + 8, n4 + 21);
    }

    public void displayHD() {
        int n = this.HDoX;
        int n2 = this.HDoY;
        this.theG.setColor(this.C_HD_BG);
        this.theG.fillRect(n, n2, 100, 60);
        this.theG.setColor(this.C_HD_LINES);
        this.drawShadow(n, n2, 100, 60);
        this.theG.setColor(Color.black);
        this.theG.drawRect(n, n2, 100, 60);
        this.theG.setColor(this.C_HD_LINES);
        this.theG.fillRect(n + 1, n2 + 60 - 20, 98, 2);
        this.theG.setColor(Color.white);
        this.theG.fillRect(n + 1, n2 + 60 - 20 + 3, 98, 2);
        this.theG.setColor(Color.green);
        this.theG.fillRect(n + 80, n2 + 60 - 20, 4, 4);
        this.theG.setColor(Color.red);
        this.theG.fillRect(n + 80 + 5, n2 + 60 - 20, 4, 4);
    }

    public void joinKernelToTLB(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(this.kernelX + 26 - 1, this.kernelY + 25 + 6, 3, 18);
        this.theG.fillRect(this.TLBoX + 25, this.kernelY + 25 + 6 + 18, this.kernelX + 26 - (this.TLBoX + 25) + 2, 3);
        this.theG.fillRect(this.TLBoX + 25, this.kernelY + 25 + 6 + 18, 3, this.TLBoY - (this.kernelY + 25 + 6 + 18) - 2);
    }

    public void joinTLBToPT(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(this.TLBoX + 25, this.TLBoY + 80, 3, this.PToY - (this.TLBoY + 80) - 2);
    }

    public void joinPTToMM(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(220, 245, 3, this.PToY - 245 - 2);
        this.theG.fillRect(220, 245, 104, 3);
    }

    public void joinTLBToMM(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(this.TLBoX + 100 + 2, 280, 37, 3);
        this.theG.fillRect(this.TLBoX + 100 + 2 + 35, 195, 3, 87);
        this.theG.fillRect(this.TLBoX + 100 + 2 + 35, 195, 142, 3);
    }

    public void joinMMToKernel(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(this.kernelX + 53 + 65, this.kernelY + 25 + 18 + 6, 3, 24);
        this.theG.fillRect(this.kernelX + 53, this.kernelY + 25 + 18 + 6, 65, 3);
        this.theG.fillRect(this.kernelX + 53, this.kernelY + 25 + 6, 3, 18);
    }

    public void joinHDToMM(boolean bl) {
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        this.theG.fillRect(this.HDoX + 50, this.HDoY - 2 - 35, 3, 35);
    }

    public void joinProcessToKernel(boolean bl, int n) {
        int n2;
        int n3;
        if (bl) {
            this.theG.setColor(this.C_CONN_ACTIVE);
        } else {
            this.theG.setColor(this.C_CONN_PASSIVE);
        }
        if (n == 0) {
            n3 = this.processOriginX + 40 + 4 + 1;
            n2 = this.processOriginY + 25;
            this.theG.fillRect(n3, n2, this.processSeparation / 2, 3);
            this.theG.fillRect(n3 + this.processSeparation / 2, n2, 3, this.kernelY + 12 - n2);
            this.theG.fillRect(n3 + this.processSeparation / 2, this.kernelY + 12, this.kernelX - (n3 + this.processSeparation / 2), 3);
        }
        if (n == 1) {
            n3 = this.processOriginX + 80 + this.processSeparation + 4 - 1;
            n2 = this.processOriginY + 25;
            this.theG.fillRect(n3, n2, this.kernelX + 26 - n3, 3);
            this.theG.fillRect(this.kernelX + 26, n2, 3, this.kernelY - n2);
        }
        if (n == 2) {
            n3 = this.processOriginX + n * (40 + this.processSeparation) - 1;
            n2 = this.processOriginY + 25;
            this.theG.fillRect(this.kernelX + 52, n2, n3 - (this.kernelX + 52), 3);
            this.theG.fillRect(this.kernelX + 52, n2, 3, this.kernelY - n2);
        }
        if (n == 3) {
            n3 = this.processOriginX + n * (40 + this.processSeparation) - 1;
            n2 = this.processOriginY + 25;
            this.theG.fillRect(n3 - this.processSeparation / 2, n2, this.processSeparation / 2, 3);
            this.theG.fillRect(n3 - this.processSeparation / 2, n2, 3, this.kernelY + 12 - n2);
            this.theG.fillRect(this.kernelX + 80 + 4 + 1, this.kernelY + 12, n3 - this.processSeparation / 2 - (this.kernelX + 80 + 2), 3);
        }
    }

    private void drawShadow(int n, int n2, int n3, int n4) {
        this.theG.setColor(this.C_SHADOW);
        this.theG.fillRect(n + n3 + 1, n2 + 1 + 4, 4, n4);
        this.theG.fillRect(n + 1 + 4, n2 + 1 + n4, n3, 4);
    }
}
