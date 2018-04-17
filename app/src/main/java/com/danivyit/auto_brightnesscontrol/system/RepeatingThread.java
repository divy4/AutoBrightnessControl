package com.danivyit.auto_brightnesscontrol.system;

import android.os.SystemClock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RepeatingThread extends Thread {

    private AtomicBoolean running;
    private AtomicInteger delay;

    /**
     * Creates a new RepeatingThread.
     * @param delay The amount of time (in seconds) to sleep in between each runTask() call.
     */
    public RepeatingThread(double delay) {
        this.running = new AtomicBoolean(true);
        this.delay = new AtomicInteger();
        setDelay(delay);
    }

    /**
     * Notifies the thread to stop as soon as it finishes it's current task.
     */
    public void queueStop() {
        running.set(false);
    }

    /**
     * Sets the delay between backlight updates
     * @param sec
     */
    public void setDelay(double sec) {
        // store ms
        delay.set((int)(sec * 1000));
    }

    /**
     * Starts when the thread starts.
     */
    @Override
    public void run() {
        while (running.get() == true) {
            runTask();
            SystemClock.sleep(delay.get());
        }
    }

    /**
     * Runs repeatably until queueStop() is called.
     */
    abstract protected void runTask();
}
