/*
 * Decompiled with CFR 0.152.
 */
import java.applet.Applet;
import java.awt.BorderLayout;

public class VMS
extends Applet {
    public static MyCanvas theCanvas;
    public Thread schdThread;
    public static Scheduler theScheduler;
    SimulatorDisplayUtils vmsDispUtils;
    private static StatisticPanel statisticResult;
    private static int runningProcess;
    private static int lastRunningProcess;
    private static int vPageFrame;
    private static int tlbHitIndex;
    private static int lastTlbHitIndex;
    private static boolean tlbState;
    private static int curMM;
    private static int lastMM;
    private static int num_Process_Exec;
    private static boolean switchProcess;
    private static boolean updatePT;
    public static int[] TLB_TAG;
    public static int[] TLB_DATA;
    public static int[] TLB;
    public static int TLB_LRU;
    public static int PM_LRU;
    public static int[] PM;
    public int[] PM_IN_TLB;
    public static int[] PM_IN_PT;
    static int gimmePageNum;

    protected static void setUpdatePT(boolean bl) {
        updatePT = bl;
    }

    protected static boolean updatePageTable() {
        return updatePT;
    }

    protected static void setSwitchProcess(boolean bl) {
        switchProcess = bl;
    }

    protected static boolean getSwitchProcess() {
        return switchProcess;
    }

    protected static void setCurMM(int n) {
        curMM = n;
    }

    protected static int getCurMM() {
        return curMM;
    }

    protected static void setLastMM(int n) {
        lastMM = n;
    }

    protected static int getLastMM() {
        return lastMM;
    }

    protected static void setNum_Process_Exec(int n) {
        num_Process_Exec = n;
    }

    protected static int getNum_Process_Exec() {
        return num_Process_Exec;
    }

    protected static void setTlbState(boolean bl) {
        tlbState = bl;
    }

    protected static boolean getTlbState() {
        return tlbState;
    }

    protected static void setTlbHitIndex(int n) {
        tlbHitIndex = n;
    }

    protected static void setLastTlbHitIndex(int n) {
        lastTlbHitIndex = n;
    }

    protected int getTlbHitIndex() {
        return tlbHitIndex;
    }

    protected static int getLastTlbHitIndex() {
        return lastTlbHitIndex;
    }

    protected static void setRunningProc(int n) {
        runningProcess = n;
    }

    protected static void setLastRunningProc(int n) {
        lastRunningProcess = n;
    }

    protected static int getRunningProc() {
        return runningProcess;
    }

    protected static int getLastRunningProc() {
        return lastRunningProcess;
    }

    protected static void setMMvPageFrame(int n) {
        vPageFrame = n;
    }

    protected static int getMMvPageFrame() {
        return vPageFrame;
    }

    protected static int getPMinPT(int n) {
        return PM_IN_PT[n];
    }

    protected static int TLBgetLRU() {
        int n = TLB[0];
        int n2 = 1;
        while (n2 < 4) {
            if (n > TLB[n2]) {
                n = TLB[n2];
            }
            ++n2;
        }
        n2 = 0;
        while (n != TLB[n2]) {
            ++n2;
        }
        return n2;
    }

    protected static int PMgetLRU() {
        int n = PM[0];
        int n2 = 1;
        while (n2 < 16) {
            if (n > PM[n2]) {
                n = PM[n2];
            }
            ++n2;
        }
        n2 = 0;
        while (n != PM[n2]) {
            ++n2;
        }
        return n2;
    }

    protected int TLBgetData(int n) {
        return TLB_DATA[n];
    }

    protected int TLBgetEntry(int n, int n2) {
        if (TLB_TAG[n] == n2) {
            return TLB_DATA[n];
        }
        return -1;
    }

    protected int TLBsetEntry(int n, int n2) {
        int n3 = VMS.TLBgetLRU();
        VMS.TLB_TAG[n3] = n;
        VMS.TLB_DATA[n3] = n2;
        return n3;
    }

    protected static void TLBincrLRU(int n) {
        VMS.TLB[n] = ++TLB_LRU;
    }

    protected static void PMincrLRU(int n) {
        VMS.PM[n] = ++PM_LRU;
    }

    protected static void PMresetLRU(int n) {
        VMS.PM[n] = 0;
    }

    protected static void incr_Reference() {
        statisticResult.references();
    }

    protected static void incr_TLB_Miss() {
        statisticResult.TLB_Misses();
    }

    protected static void incr_PT_Miss() {
        statisticResult.Page_Fault();
    }

    public static void TLBresetLRU() {
        TLB_LRU = 0;
        int n = 0;
        while (n < 4) {
            VMS.TLB_TAG[n] = -1;
            VMS.TLB_DATA[n] = -1;
            VMS.TLB[n] = 0;
            ++n;
        }
        System.out.println("_____________________________");
    }

    public void init() {
        int n = 552;
        int n2 = 279;
        this.setLayout(new BorderLayout());
        statisticResult = new StatisticPanel();
        theCanvas = new MyCanvas(n, n2);
        this.vmsDispUtils = theCanvas.createDispUtils();
        theScheduler = new Scheduler();
        CNEControls cNEControls = new CNEControls(theCanvas, theScheduler);
        Controls controls = new Controls(theCanvas, theScheduler, cNEControls, statisticResult);
        this.add("Center", theCanvas);
        this.add("South", controls);
        this.add("East", statisticResult);
        runningProcess = 0;
        gimmePageNum = -1;
    }

    public static void restartSim() {
        runningProcess = 0;
        gimmePageNum = -1;
        VMS.theCanvas.drawAll = -1;
        theCanvas.repaint();
    }

    public static void switchProcess() {
        VMS.theCanvas.drawAll = 0;
        theCanvas.repaint();
    }

    public SimulatorDisplayUtils returnDispUtils() {
        return this.vmsDispUtils;
    }

    public void paint() {
    }
}
