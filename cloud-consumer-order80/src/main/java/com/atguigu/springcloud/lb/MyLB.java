package com.atguigu.springcloud.lb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MyLB implements LoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current+1;

        }while (!this.atomicInteger.compareAndSet(current,next));
        log.info("****************第几次访问 next:  "+next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceinstances) {

        int index = getAndIncrement() % serviceinstances.size();
        return serviceinstances.get(index);
    }
}
