package com.example.springakka.controller;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.example.springakka.actor.tickersell.BoxOffice;
import com.example.springakka.util.ActorUtil;
import org.springframework.web.bind.annotation.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Ido on 2017/9/26.
 */
@RestController
@RequestMapping("/")
public class HomeController {
    ActorRef box = null;

    @ModelAttribute
    public void init(){
        box = ActorUtil.getActor("box-office");
    }
    @GetMapping("buy/{event}")
    public String buy(@PathVariable String event,int size) throws Exception {


        Timeout timeout = new Timeout(Duration.create(5, SECONDS));
        Future<Object> future = Patterns.ask(box, new BoxOffice.BuyTicket(event,size), timeout);

        String result = (String) Await.result(future, timeout.duration());
        return result;
    }

    @GetMapping("create/{event}")
    public String create(@PathVariable String event, int size) throws ExecutionException, InterruptedException {

        box.tell(new BoxOffice.CreateEvent(event,size),ActorRef.noSender());

        return size+"  tickets created";
    }


    @GetMapping("available/{event}")
    public String availableTicket(@PathVariable String event) throws Exception {

        Timeout timeout = new Timeout(Duration.create(5, SECONDS));
        Future<Object> future = Patterns.ask(box, new BoxOffice.CheckAvailableTicket(event), timeout);

        Integer count = (Integer) Await.result(future, timeout.duration());
        String result = count+ " ticket available";
        return result;
    }


    @GetMapping("area")
    public String area() throws Exception {
        System.out.println("enter");

        String result ="[{\n" +
                "    id: 1,\n" +
                "    town: '广州',\n" +
                "    streets: [{\n" +
                "        id: 3.,\n" +
                "        street: '海珠'\n" +
                "    },{\n" +
                "        id: 5.,\n" +
                "        street: '越秀'\n" +
                "    },{\n" +
                "        id: 6.,\n" +
                "        street: '天河'\n" +
                "    },{\n" +
                "        id: 7.,\n" +
                "        street: '番禺'\n" +
                "    },{\n" +
                "        id: 4.,\n" +
                "        street: '白云'\n" +
                "    }]\n" +
                "}, {\n" +
                "    id: 2,\n" +
                "    town: '深圳',\n" +
                "    streets: [{\n" +
                "        id: 8.,\n" +
                "        street: '南山'\n" +
                "    },{\n" +
                "        id: 9.,\n" +
                "        street: '大隆'\n" +
                "    },{\n" +
                "        id: 10.,\n" +
                "        street: '长隆'\n" +
                "    },{\n" +
                "        id: 11.,\n" +
                "        street: '火焰山'\n" +
                "    },{\n" +
                "        id: 12.,\n" +
                "        street: '水帘洞'\n" +
                "    },\n" +
                "    ]\n" +
                "},\n" +
                "\n" +
                "\n" +
                "]";
        return result;
    }



}
