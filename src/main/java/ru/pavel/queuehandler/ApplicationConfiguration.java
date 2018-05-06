package ru.pavel.queuehandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pavel.queuehandler.entity.Item;
import ru.pavel.queuehandler.entity.RandomInteger;
import ru.pavel.queuehandler.handler.Consumer;
import ru.pavel.queuehandler.generator.Producer;
import ru.pavel.queuehandler.service.QueueService;
import ru.pavel.queuehandler.service.QueueServiceImpl;
import ru.pavel.queuehandler.utils.ConcurrentUtils;


@SpringBootApplication
@Configuration
@PropertySource("classpath:config.properties")
@ComponentScan("ru.pavel.queuehandler")
public class ApplicationConfiguration implements CommandLineRunner {
    static final Logger LOG = LoggerFactory.getLogger(ApplicationConfiguration.class);
    private AtomicInteger atomicIndexId = new AtomicInteger(0);

    @Value("${queue.group.capacity}")
    private int QUEUE_GROUP_CAPACITY;
    @Value("${queue.group.count}")
    private int GROUP_COUNT;
    @Value("${thread.count}")
    private int THREAD_COUNT;
    @Value("${max.random.wait.seconds}")
    private int MAX_RANDOM_WAIT_SECONDS;

    @Bean
    public QueueService groupingService() {
        return new QueueServiceImpl(new ConcurrentHashMap<>(),QUEUE_GROUP_CAPACITY);
    }

    @Bean
    public Producer producer() {
        return new Producer(groupingService());
    }

    @Bean
    @Scope("prototype")
    public Item item() {
        return new Item(atomicIndexId.incrementAndGet(),new Random().nextInt(GROUP_COUNT));
    }

    @Bean
    @Scope("prototype")
    public RandomInteger randomInteger(){
        return new RandomInteger(new Random().nextInt(MAX_RANDOM_WAIT_SECONDS));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }

    @Autowired
    private QueueService queueService;
    @Autowired
    private Producer producer;
    @Autowired
    private Consumer consumer;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Start app");
        //Producer
        new Thread(producer).start();

        ConcurrentUtils.sleep(10);

        //Consumers
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        ConcurrentUtils.sleep(10);
        while (true) {
            executor.submit(consumer);
                //executor.shutdown();
            if (queueService.getQueueGroupMap().isEmpty()) {
                Thread.sleep(5000);
                System.out.println((Thread.activeCount()));
                System.out.println("QueueGroupMap is empty");
            }
        }
    }
}


