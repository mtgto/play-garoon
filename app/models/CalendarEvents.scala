package models

import play.api.libs.json.{JsValue, Json, Writes}
import net.mtgto.garoon.schedule.CalendarEvent

object CalendarEvents {
  implicit val calendarEventWrites = new Writes[CalendarEvent] {
    def writes(e: CalendarEvent): JsValue = {
      Json.obj(
        "date" -> e.date.toString("yyyy-MM-dd"),
        "name" -> e.content,
        "type" -> e.eventType.toString
      )
    }
  }
}
