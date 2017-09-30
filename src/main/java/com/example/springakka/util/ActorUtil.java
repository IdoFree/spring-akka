package com.example.springakka.util;

import akka.actor.ActorRef;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ido on 2017/9/26.
 */
public class ActorUtil {
    private static final  ConcurrentHashMap<String, ActorRef> actorRefMap = new ConcurrentHashMap<>();

    public static ActorRef getActor(String name){
       return  actorRefMap.get(name);

    }

    public static void putActor(String name,ActorRef actorRef){
          actorRefMap.put(name,actorRef);

    }
}
