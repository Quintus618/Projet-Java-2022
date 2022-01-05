package Controller;
/*
STACKOVERFLOW
(meilleure piste, pas trouvé mieux pour plusieurs timers réinitialisables)
TODO 
-Create a subclass, override timeout() to send your heartbeat message.
Every time a message is sent, call the ResettableTimer#reset() method to reset
the heartbeat timer.
      -This is really elegant, thanks. It does have the downside of tying up a thread,
    but it's otherwise efficient. I wonder how Object.wait() is implemented under the
    covers, and if it's doing some processing during a timer tick just as I suggested
    with the .scheduleAtFixedRate() method? – 
      -Re-reading your question, I see you didn't want one thread per timer.
    You want more than one resettable heartbeat timer? Interesting.
    If so, you could reimplement the run() loop to determine which timer would next expire
    (perhaps using a PriorityQueue) and lock.wait(next_expiry_ms). Each timer would,
    of course, need its own Runnable to execute on timeout(). So you'd split this into
    multiple ResettableTimer objects and one ResettableTimer.Manager. A little more work,
    but should result in a clean, reusable utility class. 
*/

    public abstract class ResettableTimer implements Runnable {

    private Object lock = new Object();
    private long timeout_ms;
    private long last;

    public ResettableTimer(long timeout_ms) {
        this.timeout_ms = timeout_ms;
    }

    public void reset() {
        synchronized (lock) {
            last = System.currentTimeMillis();
            lock.notify();
        }
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (;;) {
                    lock.wait(timeout_ms);
                    long delta = System.currentTimeMillis() - last;
                    if (delta >= timeout_ms) {
                        timeout();
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    protected abstract void timeout();
}
