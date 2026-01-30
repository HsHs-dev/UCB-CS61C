/*
 * Decompiled with CFR 0.152.
 */
public class MMU
extends VMS {
    public int[] PT_STATE;
    public int[] PT_NUM;
    public static int counter;
    int gimmePageNum = -1;
    private int vmPages;
    private int pmPages;
    private int tlbEntries;
    private int vAddr;
    private int pAddr;
    private int pmHitIndex;
    private int pmSwapIndex;
    private boolean pageState;
    private boolean pageTableState;
    private int curPTI;
    private int lastPTI;

    public int getVAddr() {
        return this.vAddr;
    }

    public int getPAddr() {
        return this.pAddr;
    }

    public void setPageState(boolean bl) {
        this.pageState = bl;
    }

    public boolean getPageState() {
        return this.pageState;
    }

    public boolean getPtState() {
        return this.pageTableState;
    }

    public int getCurPTI() {
        return this.curPTI;
    }

    public int getLastPTI() {
        return this.lastPTI;
    }

    public MMU(int n, int n2, int n3) {
        this.vmPages = n;
        this.pmPages = n2;
        this.tlbEntries = n3;
        VMS.TLB_TAG = new int[n3];
        VMS.TLB_DATA = new int[n3];
        VMS.TLB = new int[n3];
        VMS.TLB_LRU = 0;
        int n4 = 0;
        while (n4 < n3) {
            VMS.TLB_TAG[n4] = -1;
            VMS.TLB_DATA[n4] = -1;
            VMS.TLB[n4] = 0;
            ++n4;
        }
        this.PT_STATE = new int[n];
        this.PT_NUM = new int[n];
        n4 = 0;
        while (n4 < n) {
            this.PT_STATE[n4] = 0;
            this.PT_NUM[n4] = -1;
            ++n4;
        }
        VMS.PM_LRU = 0;
        this.PM_IN_TLB = new int[n2];
        VMS.PM_IN_PT = new int[n2];
        VMS.PM = new int[n2];
        n4 = 0;
        while (n4 < n2) {
            this.PM_IN_TLB[n4] = -1;
            VMS.PM_IN_PT[n4] = -1;
            VMS.PM[n4] = 0;
            ++n4;
        }
    }

    public void reset() {
        VMS.TLB_LRU = 0;
        VMS.PM_LRU = 0;
        counter = 0;
        int n = 0;
        while (n < VMS.TLB_TAG.length) {
            VMS.TLB_TAG[n] = -1;
            VMS.TLB_DATA[n] = -1;
            VMS.TLB[n] = 0;
            ++n;
        }
        n = 0;
        while (n < this.PT_STATE.length) {
            this.PT_STATE[n] = 0;
            this.PT_NUM[n] = -1;
            ++n;
        }
        n = 0;
        while (n < VMS.PM_IN_PT.length) {
            this.PM_IN_TLB[n] = -1;
            VMS.PM_IN_PT[n] = -1;
            VMS.PM[n] = 0;
            ++n;
        }
    }

    public int virtualToPhysical(int n) {
        int n2 = VMS.PMgetLRU();
        int n3 = -1;
        boolean bl = false;
        if (VMS.getTlbState()) {
            VMS.setLastTlbHitIndex(this.getTlbHitIndex());
        }
        if (this.getPtState()) {
            this.lastPTI = this.getCurPTI();
        }
        int n4 = 0;
        while (n4 < this.tlbEntries) {
            n2 = this.TLBgetEntry(n4, n);
            if (n2 != -1) {
                bl = true;
                n3 = n4;
                break;
            }
            ++n4;
        }
        VMS.setSwitchProcess(false);
        if (counter % VMS.getNum_Process_Exec() == 0) {
            VMS.setSwitchProcess(true);
        }
        ++counter;
        if (bl) {
            VMS.setTlbState(true);
            VMS.setTlbHitIndex(n3);
            VMS.setCurMM(n2);
            VMS.setMMvPageFrame(n);
            VMS.theCanvas.display();
            VMS.TLBincrLRU(n3);
            VMS.PMincrLRU(n2);
            return 0;
        }
        VMS.setTlbState(false);
        VMS.theCanvas.display();
        VMS.incr_TLB_Miss();
        n2 = this.PTgetEntry(n, n2);
        if (n2 != -1) {
            this.pageTableState = true;
            this.pAddr = n2;
            this.vAddr = n;
            this.curPTI = n;
            VMS.setCurMM(n2);
            VMS.setMMvPageFrame(n);
            VMS.PMincrLRU(n2);
            VMS.theCanvas.display();
            n3 = this.TLBsetEntry(n, n2);
            VMS.TLBincrLRU(n3);
            VMS.theCanvas.display();
            return 1;
        }
        this.pageTableState = false;
        VMS.setTlbState(false);
        VMS.setCurMM(VMS.PMgetLRU());
        this.curPTI = n;
        VMS.incr_PT_Miss();
        return 2;
    }

    public void swapIn(int n) {
        VMS.theCanvas.display();
        int n2 = VMS.PMgetLRU();
        int n3 = VMS.TLBgetLRU();
        int n4 = this.TLBsetEntry(n, n2);
        VMS.setTlbHitIndex(n3);
        VMS.TLBincrLRU(n3);
        VMS.theCanvas.display();
        this.pageTableState = false;
        this.curPTI = n;
        this.PTsetEntry(n, n2);
        VMS.PMincrLRU(n2);
        VMS.setCurMM(n2);
        VMS.setMMvPageFrame(n);
        VMS.theCanvas.display();
    }

    private void PTsetEntry(int n, int n2) {
        this.PT_STATE[n] = 1;
        this.PT_NUM[n] = n2;
    }

    private int PTgetEntry(int n, int n2) {
        n2 = this.PT_NUM[n];
        if (this.PT_STATE[n] == 1) {
            return n2;
        }
        return -1;
    }

    public void dumpPT(int n, boolean bl) {
        int n2 = 0;
        while (n2 < this.vmPages) {
            if (this.PT_NUM[n2] == n && this.PT_STATE[n2] == 1) {
                this.PT_NUM[n2] = -1;
                this.PT_STATE[n2] = 0;
                break;
            }
            ++n2;
        }
        if (bl) {
            this.pageTableState = false;
            this.curPTI = n2;
            VMS.setUpdatePT(true);
            VMS.theCanvas.updatePTonly();
        }
    }
}
