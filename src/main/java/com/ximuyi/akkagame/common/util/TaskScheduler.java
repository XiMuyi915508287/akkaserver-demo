package com.ximuyi.akkagame.common.util;

import akka.actor.Cancellable;
import com.ximuyi.akkaserver.api.ITaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TaskScheduler<T extends TaskScheduler.ISchedule> {

    private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    private final Map<Long, DoubleEntry<T, Cancellable>> schedules;

    private final ITaskManager manager;
    public TaskScheduler(ITaskManager manager) {
        this.manager = manager;
        this.schedules = new ConcurrentHashMap<>();
    }

    public void schedule(long uniqueId, T task, long millisecondDelay, long duration){
        Cancellable cancellable = manager.schedule(
                millisecondDelay, duration, TimeUnit.MILLISECONDS, ()-> {
                    try {
                        long current = System.currentTimeMillis();
                        task.onTick();
                        long cost = System.currentTimeMillis() - current;
                        if (cost >= duration){
                            logger.error("task[{}] uniqueId[{}] cost:{}(ms)", task.getClass().getSimpleName(), uniqueId, cost);
                        }
                    }
                    catch (Throwable t){
                        logger.error("task[{}] uniqueId[{}] error", task.getClass().getSimpleName(), uniqueId, t);
                    }
                });
        DoubleEntry<T, Cancellable> entry = schedules.putIfAbsent(uniqueId, new DoubleEntry<>(task, cancellable));
        if (entry != null){
            cancel(entry);
            logger.error("cancel old task[{}] uniqueId[{}] ", task.getClass().getSimpleName(), uniqueId);
        }
    }

    public T cancel(long uniqueId){
        DoubleEntry<T, Cancellable> entry = schedules.remove(uniqueId);
        return entry == null ? null : cancel(entry);
    }

    public T get(long uniqueId){
        DoubleEntry<T, Cancellable> entry = schedules.remove(uniqueId);
        return entry == null ? null : entry.first;
    }

    private T cancel(DoubleEntry<T, Cancellable> entry){
        entry.second.cancel();
        return entry.first;
    }


    public interface ISchedule{
        void onTick();
    }
}
