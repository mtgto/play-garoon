package models

import net.mtgto.garoon.schedule._
import play.api.libs.json.{JsValue, Json, Writes}
import org.joda.time.{Interval, LocalTime, DateTime}

object Events {
  implicit val eventUserWrites = new Writes[User] {
    def writes(u: User): JsValue = {
      Json.obj(
        "id" -> u.id.value,
        "name" -> u.name,
        "order" -> u.order.map(_.value)
      )
    }
  }

  implicit val eventFacilityWrites = new Writes[Facility] {
    def writes(f: Facility): JsValue = {
      Json.obj(
        "id" -> f.id.value,
        "name" -> f.name,
        "order" -> f.order.map(_.value)
      )
    }
  }

  implicit val dateTimeWrites = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = {
      Json.toJson(d.getMillis / 1000)
    }
  }

  implicit val eventDateTimeWrites = new Writes[EventDateTime] {
    def writes(t: EventDateTime): JsValue = {
      Json.obj(
        "start" -> t.start,
        "end" -> t.end,
        "facility_code" -> t.facilityCode.map(_.toString)
      )
    }
  }

  implicit val localTimeWrites = new Writes[LocalTime] {
    def writes(d: LocalTime): JsValue = {
      Json.toJson(d.toString)
    }
  }

  implicit val intervalWrites = new Writes[Interval] {
    def writes(i: Interval): JsValue = {
      Json.obj(
        "start" -> i.getStart,
        "end" -> i.getEnd
      )
    }
  }

  implicit val repeatInfoWrites = new Writes[RepeatInfo] {
    override def writes(r: RepeatInfo): JsValue = {
      Json.obj(
        "start_date" -> r.startDate,
        "end_date" -> r.endDate,
        "start_time" -> r.startTime,
        "end_time" -> r.endTime,
        "type" -> r.repeatEventType.toString,
        "day" -> r.day,
        "week" -> r.week,
        "exclusive" -> r.exclusiveDatetimes
      )
    }
  }

  implicit val eventWrites = new Writes[Event] {
    def writes(e: Event): JsValue = {
      Json.obj(
        "id" -> e.identity.value,
        "plan" -> e.plan,
        "title" -> e.detail,
        "event_type" -> e.eventType.toString,
        "public_type" -> e.publicType.map(_.toString),
        "members" -> e.members,
        "facilities" -> e.facilities,
        "when" -> e.when,
        "repeat_info" -> e.repeatInfo
      )
    }
  }
}
