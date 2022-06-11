package app.domain.model.list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import app.domain.model.CenterEvent;
import app.domain.model.SNSUser;
import app.domain.shared.CenterEventType;

public class CenterEventList implements Iterable<CenterEvent> {
  private List<CenterEvent> events;

  public CenterEventList() {
    this.events = new ArrayList<CenterEvent>();
  }

  @Override
  public Iterator<CenterEvent> iterator() {
    return events.iterator();
  }

  public CenterEvent create(Calendar date, CenterEventType eventType, SNSUser snsUser) {
    CenterEvent centerEvent = new CenterEvent(date, eventType, snsUser);
    return centerEvent;
  }

  public void save(CenterEvent event) {
    this.events.add(event);
  }

  public CenterEventList getEventListForDay(Calendar day) {
    CenterEventList eventsInDay = new CenterEventList();

    for (CenterEvent event : this.events)
      if (event.isInDay(day)) eventsInDay.save(event);

    System.out.println(eventsInDay);
    return eventsInDay;
  }

  public int size() {
    return this.events.size();
  }
}
