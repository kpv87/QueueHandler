package ru.pavel.queuehandler.service;

import org.springframework.stereotype.Service;
import ru.pavel.queuehandler.entity.Item;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Service
public interface QueueService {
    void addItemToGroup(Item item);
    BlockingQueue<Item> takeQueueFirstGroupeFromMap();
    Map<Integer, BlockingQueue<Item>> getQueueGroupMap();
}
