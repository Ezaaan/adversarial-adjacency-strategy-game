import java.util.Timer;
import java.util.TimerTask;

public class TimeOut extends TimerTask {
    private Thread thread;
    private Timer timer;

    public TimeOut(Thread thread, Timer timer) {
        this.thread = thread;
        this.timer = timer;
    }

    @Override
    public void run() {
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
            timer.cancel();
        }
    }
}
