package com.chunkslab.realms.scheduler;

import com.chunkslab.realms.RealmsPlugin;
import com.chunkslab.realms.api.scheduler.CancellableTask;
import com.chunkslab.realms.api.scheduler.IScheduler;
import com.chunkslab.realms.api.util.LogUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A scheduler implementation responsible for scheduling and managing tasks in a multi-threaded environment.
 */
@RequiredArgsConstructor
public class Scheduler implements IScheduler {

    private final RealmsPlugin plugin;

    private SyncScheduler syncScheduler;
    private ScheduledThreadPoolExecutor schedule;

    public void enable() {
        this.syncScheduler = new BukkitSchedulerImpl(plugin);
        this.schedule = new ScheduledThreadPoolExecutor(1);
        this.schedule.setMaximumPoolSize(1);
        this.schedule.setKeepAliveTime(30, TimeUnit.SECONDS);
        this.schedule.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void reload() {
        try {
            this.schedule.setMaximumPoolSize(10);
            this.schedule.setCorePoolSize(10);
            this.schedule.setKeepAliveTime(30, TimeUnit.SECONDS);
        } catch (IllegalArgumentException e) {
            LogUtils.warn("Failed to create thread pool. Please lower the corePoolSize in config.yml.", e);
        }
    }

    public void disable() {
        if (this.schedule != null && !this.schedule.isShutdown())
            this.schedule.shutdown();
    }

    @Override
    public void runTaskSync(Runnable runnable, Location location) {
        this.syncScheduler.runSyncTask(runnable, location);
    }

    @Override
    public void runTaskSync(Runnable runnable, World world, int x, int z) {
        this.syncScheduler.runSyncTask(runnable, world, x, z);
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        try {
            this.schedule.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delayTicks, long periodTicks) {
        return this.syncScheduler.runTaskSyncTimer(runnable, location, delayTicks, periodTicks);
    }

    @Override
    public CancellableTask runTaskAsyncLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return new ScheduledTask(schedule.schedule(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, timeUnit));
    }

    @Override
    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delay, TimeUnit timeUnit) {
        return new ScheduledTask(schedule.schedule(() -> runTaskSync(runnable, location), delay, timeUnit));
    }

    @Override
    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delayTicks) {
        return this.syncScheduler.runTaskSyncLater(runnable, location, delayTicks);
    }

    @Override
    public CancellableTask runTaskAsyncTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        return new ScheduledTask(schedule.scheduleAtFixedRate(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, period, timeUnit));
    }

    public static class ScheduledTask implements CancellableTask {

        private final ScheduledFuture<?> scheduledFuture;

        public ScheduledTask(ScheduledFuture<?> scheduledFuture) {
            this.scheduledFuture = scheduledFuture;
        }

        @Override
        public void cancel() {
            this.scheduledFuture.cancel(false);
        }

        @Override
        public boolean isCancelled() {
            return this.scheduledFuture.isCancelled();
        }
    }
}