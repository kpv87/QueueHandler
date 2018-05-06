package ru.pavel.queuehandler.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.pavel.queuehandler.entity.Item;
import ru.pavel.queuehandler.entity.RandomInteger;
import ru.pavel.queuehandler.service.QueueService;
import ru.pavel.queuehandler.utils.ConcurrentUtils;

@Component
public class Producer implements Runnable {

    @Autowired private ApplicationContext context;
    private QueueService queueService;

    @Autowired
    public Producer(QueueService queueService) {
        this.queueService = queueService;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < context.getBean(RandomInteger.class).getRandom(); i++) {
                Item item = context.getBean(Item.class);
                queueService.addItemToGroup(item);
                System.out.println("Produced: " + item + " " + Thread.currentThread().getName());
            }
            ConcurrentUtils.sleep(5);
        }
    }
}