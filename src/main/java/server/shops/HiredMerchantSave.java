package server.shops;

import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiredMerchantSave {

    public static final int NumSavingThreads = 5;
    private static final TimingThread[] Threads = new TimingThread[5];
    private static final AtomicInteger Distribute;

    public static void QueueShopForSave(HiredMerchant hm) {
        int Current = Distribute.getAndIncrement() % 5;
        Threads[Current].getRunnable().Queue(hm);
    }

    public static void Execute(Object ToNotify) {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].getRunnable().SetToNotify(ToNotify);
        }
        for (int i = 0; i < Threads.length; i++) {
            Threads[i].start();
        }
    }

    static {
        for (int i = 0; i < Threads.length; i++) {
            Threads[i] = new TimingThread(new HiredMerchantSaveRunnable());
        }

        Distribute = new AtomicInteger(0);
    }

    private static class TimingThread extends Thread {

        private final HiredMerchantSave.HiredMerchantSaveRunnable ext;

        public TimingThread(HiredMerchantSave.HiredMerchantSaveRunnable r) {
            super();
            this.ext = r;
        }

        public HiredMerchantSave.HiredMerchantSaveRunnable getRunnable() {
            return this.ext;
        }
    }

    private static class HiredMerchantSaveRunnable
            implements Runnable {

        private static AtomicInteger RunningThreadID = new AtomicInteger(0);
        private int ThreadID = RunningThreadID.incrementAndGet();
        private long TimeTaken = 0L;
        private int ShopsSaved = 0;
        private Object ToNotify;
        private ArrayBlockingQueue<HiredMerchant> Queue = new ArrayBlockingQueue(500);

        public void run() {
            try {
                while (!this.Queue.isEmpty()) {
                    HiredMerchant next = (HiredMerchant) this.Queue.take();
                    long Start = System.currentTimeMillis();
                    next.closeShop(true, false);
                    this.TimeTaken += System.currentTimeMillis() - Start;
                    this.ShopsSaved += 1;
                }
                System.out.println("[保存雇佣商店数据 线程 " + this.ThreadID + "] 共保存: " + this.ShopsSaved + " | 耗时: " + this.TimeTaken + " 毫秒.");
                synchronized (this.ToNotify) {
                    this.ToNotify.notify();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(HiredMerchantSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void Queue(HiredMerchant hm) {
            this.Queue.add(hm);
        }

        private void SetToNotify(Object o) {
            if (this.ToNotify == null) {
                this.ToNotify = o;
            }
        }
    }
}