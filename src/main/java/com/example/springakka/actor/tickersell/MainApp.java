package com.example.springakka.actor.tickersell;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by Ido on 2017/9/26.
 */
public class MainApp {
    public static void main(String[] args) throws Exception{
        ActorSystem system = ActorSystem.create("seller");

        // Create an actor that handles cluster domain events
        ActorRef box = system.actorOf(Props.create(BoxOffice.class),
                "box-office");
        box.tell(new BoxOffice.CreateEvent("test",10),ActorRef.noSender());
        box.tell(new BoxOffice.BuyTicket("test",2),ActorRef.noSender());
        box.tell(new BoxOffice.BuyTicket("test",10),ActorRef.noSender());
        Thread.sleep(5000);
        box.tell(new BoxOffice.BuyTicket("test",2),ActorRef.noSender());

        Thread.sleep(20000);
        system.terminate();
    }


}
