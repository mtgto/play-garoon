package models

import net.mtgto.garoon.schedule.{EventDateTime, Facility, User, Event}
import play.api.libs.json.{JsValue, Json, Writes}
import org.joda.time.DateTime

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
        "when" -> e.when
      )
    }
  }
}
