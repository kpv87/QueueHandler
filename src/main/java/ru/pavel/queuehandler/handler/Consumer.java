package ru.pavel.queuehandler.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pavel.queuehandler.service.QueueService;

@Component
public class Consumer implements Runnable{

    private QueueService queueService;

    @Autowired
    public Consumer(QueueService queueService){
        this.queueService = queueService;
    }

    @Override
    public void run() {
        queueService.takeQueueFirstGroupeFromMap().stream().forEach(item -> System.out.println(Thread.currentThread().getName()+" "+item));
    }
}