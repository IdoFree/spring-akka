package com.example.springakka.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.springakka.actor.tickersell.BoxOffice;
import com.example.springakka.util.ActorUtil;
import com.example.springakka.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * Created by Ido on 2017/9/26.
 */
@Configuration
public class AkkaConfig implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AkkaConfig.class);

    @Override
    public void run(String... strings) throws Exception {

//        String resource = "mybits.xml";
        //init the mybatis session factory
//        initFactory(resource);

        ActorSystem system = ActorSystem.create("spring-akka");

        // Create an actor that handles cluster domain events

        ActorRef userRoot = system.actorOf(Props.create(BoxOffice.class),
                "box-office");
        ActorUtil.putActor("box-office",userRoot);
        HibernateUtil.getSessionFactory();

    }

    @PreDestroy
    public void destroy() {
        logger.info("clearing database connection resource  ");
        HibernateUtil.shutdown();
    }
}
