package models

import net.mtgto.garoon.schedule.{EventDateTime, Facility, User, Event}
import play.api.libs.json.{JsValue, Json, Writes}

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

  implicit val eventDateTimeWrites = new Writes[EventDateTime] {
    def writes(t: EventDateTime): JsValue = {
      Json.obj(
        "start" -> t.start.getMillis / 1000,
        "end" -> t.end.map(_.getMillis / 1000),
        "facility_code" -> t.facilityCode.map(_.toString)
      )
    }
  }

  implicit val eventWrites = new Writes[Event] {
    def writes(e: Event): JsValue = {
      Json.obj(
        "id" -> e.identity.value,
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
