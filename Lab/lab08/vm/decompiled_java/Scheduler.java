/*
 * Decompiled with CFR 0.152.
 */
public class Scheduler
extends SimulatorDisplayUtils
implements Runnable {
    private int pid;
    public static Thread schdThread;
    public static int slider_num_one;
    public static int slider_num_two;
    private volatile boolean isRunning = false;
    public MMU[] theMMU = new MMU[4];

    public int getPID() {
        return this.pid;
    }

    public Scheduler() {
        int n = 0;
        while (n < 4) {
            this.theMMU[n] = new MMU(32, 16, 4);
            ++n;
        }
        this.pid = 0;
    }

    public void start() {
        if (schdThread == null) {
            isRunning = true;
            schdThread = new Thread(this);
            schdThread.start();
        } else if (!isRunning) {
            isRunning = true;
        }
    }

    public void run() {
        this.pid = 0;
        VMS.setLastRunningProc(this.pid);
        VMS.setRunningProc(this.pid);
        int n = Scheduler.RandomNumGenerate(2) * 2 + 4;
        this.theMMU[this.pid].setPageState(false);
        while (isRunning) {
            VMS.setNum_Process_Exec(n);
            VMS.setSwitchProcess(true);
            int n2 = 0;
            while (n2 < n) {
                this.Process(this.pid);
                try {
                    Thread.sleep(slider_num_one);
                }
                catch (InterruptedException interruptedException) {}
                ++n2;
            }
            VMS.setLastRunningProc(this.pid);
            this.pid = (this.pid + 1) % 4;
            VMS.setRunningProc(this.pid);
            this.theMMU[this.pid].setPageState(false);
            MMU.counter = 0;
            VMS.TLBresetLRU();
            n = Scheduler.RandomNumGenerate(2) * 2 + 4;
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void resetSim() {
        isRunning = false;
        schdThread = null;
        VMS.restartSim();
        int n = 0;
        while (n < 4) {
            this.theMMU[n].reset();
            MMU.counter = 0;
            ++n;
        }
    }

    public void Process(int n) {
        int n2 = n;
        int n3 = -1;
        VMS.gimmePageNum = Scheduler.RandomNumGenerate(32);
        VMS.incr_Reference();
        int n4 = this.theMMU[n].virtualToPhysical(VMS.gimmePageNum);
        if (n4 == 2) {
            n3 = VMS.getCurMM();
            int n5 = VMS.getPMinPT(n3);
            boolean bl = n5 == n2;
            if (n5 != -1) {
                this.theMMU[n5].dumpPT(n3, bl);
            }
            this.theMMU[n].swapIn(VMS.gimmePageNum);
            VMS.PM_IN_PT[n3] = n;
            VMS.theCanvas.display();
        }
    }

    public static int RandomNumGenerate(int n) {
        double d = Math.random();
        int n2 = (int)(d * 1000.0);
        return n2 % n;
    }

    static {
        slider_num_one = 1000;
        slider_num_two = 500;
    }
}
