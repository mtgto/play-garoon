package controllers

import com.github.nscala_time.time.Imports._
import models.Events.eventWrites
import models.CalendarEvents.calendarEventWrites
import net.mtgto.garoon.Id
import net.mtgto.garoon.schedule.{CalendarEventService, EventId, EventRepository}
import play.api.libs.json.Json
import play.api.mvc.Controller
import scala.util.{Failure, Success}

object ScheduleController extends Controller with BaseController {
  def getEvent(eventId: Int) = Authenticated { request =>
    val eventRepository: EventRepository = new EventRepository(garoonClient, request.user)
    eventRepository.resolve(EventId(eventId.toString)) match {
      case Success(event) => Ok(Json.toJson(event))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }

  def getEventsByUserId(userId: Int, start: Long, end: Long) = Authenticated { request =>
    val eventRepository: EventRepository = new EventRepository(garoonClient, request.user)
    val interval = new DateTime(start * 1000) to new DateTime(end * 1000)
    eventRepository.findByUserId(Id(userId.toString), interval) match {
      case Success(event) => Ok(Json.toJson(event))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }

  def getCalendarEvents = Authenticated { request =>
    val calendarEventService: CalendarEventService = new CalendarEventService(garoonClient, request.user)
    calendarEventService.get match {
      case Success(events) => Ok(Json.toJson(events))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }
}
