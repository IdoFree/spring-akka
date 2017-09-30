package com.example.springakka.actor.tickersell;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.springakka.entity.Event;
import com.example.springakka.repository.EventDao;

/**
 * Created by Ido on 2017/9/26.
 */
public class TickerSeller extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static class Buy {
        public Buy(int size, String event) {
            this.size = size;
            this.event = event;
        }

        private int size;
        private String event;
    }



    public static class SoldCompleted {
    }

    private String event;


    public static Props props(String event) {
        return Props.create(TickerSeller.class, event);
    }


    private Receive diabled;
    private Receive enabled;
    private EventDao eventDao;

    public TickerSeller(String event) {
        this.event = event;
        diabled = receiveBuilder()
                .match(Buy.class, s -> {
                    getSender().tell("not available", getSelf());
                })
                .build();
        enabled = receiveBuilder()
                .match(BoxOffice.CreateEvent.class, ce -> {
                    Event evt = eventDao.findByName(ce.name);
                    if (evt == null) {
                        evt = new Event();
                        evt.setName(ce.name);
                        evt.setTicketCount(ce.size);
                        eventDao.save(evt);
                    } else {
                        evt.setTicketCount(ce.size + evt.getTicketCount());
                        eventDao.update(evt);

                    }

                    getSender().tell(ce.size + " tickets created ", getSelf());


                })
                .match(Buy.class, s -> {
//
                    Event evt = eventDao.findByName(s.event);
                    if (evt == null) {
                        getSender().tell("no such event :" + s.event, getSelf());

                    } else {
                        if (evt.getTicketCount() == 0) {
                            //if no more ticker , stop to service
                            log.warning("no more ticker ");
                            getSender().tell("no more ticker ", getSelf());
                            getContext().become(diabled);
                            return;
                        }

                        if (s.size > evt.getTicketCount()) {
                            getSender().tell(String.format("not enough tickets, only %d available ", evt.getTicketCount()), getSelf());
                            return;
                        }
                        evt.setTicketCount(evt.getTicketCount() - s.size);
                        eventDao.update(evt);

                    }
                    getSender().tell(s.size + " tickets bought ", getSelf());


                })
                .match(BoxOffice.CheckAvailableTicket.class, act -> {
                    Event e = eventDao.findByName(act.getEvent());

                    getSender().tell(e.getTicketCount(), getSelf());
                })
                .build();
        eventDao = new EventDao();

    }


    @Override
    public Receive createReceive() {
        return enabled;
    }
}
