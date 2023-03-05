package com.feng.springboot_test.demoTest;

import com.feng.config.RedissonConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class redisTest {

    @Resource

    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("aaa", "111");
        Assert.assertEquals("111", redisTemplate.opsForValue().get("aaa"));

        Object yupi = valueOperations.get("aaa");
        Assert.assertEquals("111", yupi);
    }


    @Test
    public void TryTest(){
        Lock lock = new ReentrantLock();

        TryLock tl = new TryLock();

        new Thread(){
            public void run(){

                tl.runThread(Thread.currentThread());
            }
        }.start();
        //匿名对象创建线程2，并重写run方法，启动线程
        new Thread(){
            public void run(){

                tl.runThread(Thread.currentThread());
            }
        }.start();
        Thread t = new Thread();
        //lock对象调用trylock()方法尝试获取锁
        if(lock.tryLock()){
            //获锁成功代码段
            System.out.println("线程"+t.getName()+"获取锁成功");
            try {
                //执行的代码
                Thread.sleep(5000);
            } catch (Exception e) {
                //异常处理内容，比如中断异常，需要恢复等
            } finally {
                //获取锁成功之后，一定记住加finally并unlock()方法,释放锁
                System.out.println("线程"+t.getName()+"释放锁");
                lock.unlock();
            }
        }else{
            //获锁失败代码段
            //具体获取锁失败的回复响应
            System.out.println("线程"+t.getName()+"获取锁失败");
        }
    }

    @Test
    public void Redistest() {
        RList<Object> rList = redissonClient.getList("test");
        rList.add("feng");
        System.out.println("rlist:"+rList.get(0));
        rList.remove(0);
    }
    @Test
    public void testWatchDog() {
        RLock lock = redissonClient.getLock("feng:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                Thread.sleep(300000);
                System.out.println("getLock: " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

    @Test
    public void testDemo(){

    }
}
