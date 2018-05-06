package ru.pavel.queuehandler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pavel.queuehandler.entity.Item;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class QueueServiceImpl implements QueueService {
    static final Logger LOG = LoggerFactory.getLogger(QueueServiceImpl.class);

    private ConcurrentMap<Integer, BlockingQueue<Item>> queuesGroupMap;
    private Integer groupSize;

    public QueueServiceImpl(ConcurrentMap<Integer, BlockingQueue<Item>> queuesGroupMap,Integer groupSize) {
        this.queuesGroupMap = queuesGroupMap;
        this.groupSize = groupSize;
    }

    @Override
    public void addItemToGroup(Item item) {
        try {
            synchronized (this) {
                //TODO change to computeIfAbsent method
                if (queuesGroupMap.containsKey(item.getGroupId())) {
                    queuesGroupMap.get(item.getGroupId()).put(item);
                } else {
                    //TODO remove new generate instance from Spring
                    BlockingQueue<Item> queuesGroup = new ArrayBlockingQueue(groupSize);
                    queuesGroup.put(item);
                    queuesGroupMap.put(item.getGroupId(),queuesGroup);
                }
            }
        } catch (InterruptedException e) {
            LOG.error("InterruptedException when addItemToGroup: "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public BlockingQueue<Item> takeQueueFirstGroupeFromMap() {
        Iterator<Map.Entry<Integer, BlockingQueue<Item>>> iteratorGroup = queuesGroupMap.entrySet().iterator();
        if (iteratorGroup.hasNext()) {
            return queuesGroupMap.remove(iteratorGroup.next().getKey());
        }
        return null;
    }


    @Override
    public Map<Integer, BlockingQueue<Item>> getQueueGroupMap() {
        return queuesGroupMap;
    }
}
