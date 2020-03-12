package com.ximuyi.game.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import akka.actor.Cancellable;
import com.ximuyi.core.api.IScheduleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyScheduler<T extends MyScheduler.ISchedule> {

    private static final Logger logger = LoggerFactory.getLogger(MyScheduler.class);

    private final Map<Long, Args.Two<T, Cancellable>> schedules;

    private final IScheduleManager manager;

    public MyScheduler(IScheduleManager manager) {
        this.manager = manager;
        this.schedules = new ConcurrentHashMap<>();
    }

    public void schedule(long uniqueId, T runnable, long millisecondDelay, long duration){
        Cancellable cancellable = manager.schedule(
                millisecondDelay, duration, TimeUnit.MILLISECONDS, ()-> {
                    try {
                        long current = System.currentTimeMillis();
                        runnable.onTick();
                        long cost = System.currentTimeMillis() - current;
                        if (cost >= duration){
                            logger.error("task[{}] uniqueId[{}] cost:{}(ms)", runnable.getClass().getSimpleName(), uniqueId, cost);
                        }
                    }
                    catch (Throwable t){
                        logger.error("task[{}] uniqueId[{}] error", runnable.getClass().getSimpleName(), uniqueId, t);
                    }
                });
        Args.Two<T, Cancellable> entry = schedules.putIfAbsent(uniqueId, Args.create(runnable, cancellable));
        if (entry != null){
            cancel(entry);
            logger.error("cancel old task[{}] uniqueId[{}] ", runnable.getClass().getSimpleName(), uniqueId);
        }
    }

    public T cancel(long uniqueId){
        Args.Two<T, Cancellable> entry = schedules.remove(uniqueId);
        return entry == null ? null : cancel(entry);
    }

    public T get(long uniqueId){
        Args.Two<T, Cancellable> entry = schedules.remove(uniqueId);
        return entry == null ? null : entry.arg0;
    }

    private T cancel(Args.Two<T, Cancellable> entry){
        entry.arg1.cancel();
        return entry.arg0;
    }


    public interface ISchedule{
        void onTick();
    }
}
