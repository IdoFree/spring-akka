package com.example.springakka.actor.tickersell;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.example.springakka.entity.Event;
import com.example.springakka.repository.EventDao;

import java.util.List;

/**
 * Created by Ido on 2017/9/26.
 */
public class BoxOffice extends AbstractActor{

    private EventDao eventDao;


    public static class Ticket{
        String event;
        int nr;

        public Ticket(String event, int nr) {
            this.event = event;
            this.nr = nr;
        }
    }
    public static class Tickets{
        List<Ticket> tickets;

        public Tickets(List<Ticket> tickets) {
            this.tickets = tickets;
        }

        public List<Ticket> getTickets() {
            return tickets;
        }
    }
    public static class EventCreated{

    }

    public static class CheckAvailableTicket{
        private String event;

        public String getEvent() {
            return event;
        }

        public CheckAvailableTicket(String event) {
            this.event = event;
        }
    }

    public static class BuyTicket{
        private String event;
        private int size;

        public BuyTicket(String event, int size) {
            this.event = event;
            this.size = size;
        }
    }

    public static class CreateEvent{
        String name;
        int size;

        public CreateEvent(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }

    public BoxOffice() {
        eventDao = new EventDao();
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);


    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(CreateEvent.class ,ce ->{
                    if(getContext().child(ce.name).isEmpty()){
                        log.info("-------to create seller");
                        ActorRef tickerSeller = getContext().actorOf(TickerSeller.props(ce.name),ce.name);

//                        getContext().watch(tickerSeller);

                        tickerSeller.tell(ce,getSelf());

                        getSender().tell(new EventCreated(),getSelf());

                    }
                } )
                .match(BuyTicket.class, b->{
                   if(! eventWorkExist(b.event)){


                       Event event = eventDao.findByName(b.event);
                       if(event == null){

                           log.warning("no event found");
                           getSender().tell("no event found",getSelf());
//                       throw new RuntimeException("no event found");
                           return ;
                       } else{
                           ActorRef tickerSeller = getContext().actorOf(TickerSeller.props(event.getName()),event.getName());
                           tickerSeller.forward(new TickerSeller.Buy(b.size,b.event),getContext());
                           return;
                       }

                   }
                    getContext().child(b.event).get().forward(new TickerSeller.Buy(b.size,b.event),getContext());

                })
                .match(TickerSeller.SoldCompleted.class, sc->{
                    log.info("sold completed");
                })
                .match(String.class, s->{
                    log.info("message back is :" + s);
                })
                .match(CheckAvailableTicket.class, cat->{
                    if( !eventWorkExist(cat.event)){


                        Event event = eventDao.findByName(cat.event);
                        if(event == null){
                            log.warning("no event found");
                            getSender().tell("no event found",getSelf());
                            return ;
                        } else{
                            ActorRef tickerSeller = getContext().actorOf(TickerSeller.props(event.getName()),event.getName());
                            tickerSeller.forward(new CheckAvailableTicket(cat.event),getContext());
                            return;
                        }
                    }

                    getContext().child(cat.event).get().forward(cat,getContext());
                })
                .build();
    }

    private boolean eventWorkExist(String event){
        return  !getContext().child(event).isEmpty();
    }
}
