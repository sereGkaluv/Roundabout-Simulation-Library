package at.fhv.itm3.s2.roundabout.ui.util;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);

        return thread;
    }
}
