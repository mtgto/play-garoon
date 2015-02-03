package models

import net.mtgto.garoon.notification.Notification
import play.api.libs.json.{Json, JsValue, JsPath, Writes}

object Notifications {
  implicit val notificationWrites: Writes[Notification] = new Writes[Notification] {
    def writes(notification: Notification): JsValue = {
      Json.obj(
        "item" -> notification.item,
        "module" -> notification.moduleId.value,
        "status" -> notification.status.toString,
        "is_checked" -> notification.isChecked,
        "read_date" -> notification.readDate,
        "receive_date" -> notification.receiveDate,
        "subject" -> notification.subject,
        "subject_url" -> notification.subjectUrl.map(_.toString),
        "subject_icon" -> notification.subjectIcon,
        "content" -> notification.content,
        "content_url" -> notification.contentUrl.map(_.toString),
        "content_icon" -> notification.contentIcon,
        "sender_name" -> notification.senderName,
        "sender_id" -> notification.senderId.map(_.value),
        "sender_url" -> notification.senderUrl.map(_.toString),
        "is_attached" -> notification.isAttached,
        "version" -> notification.version.map(_.value)
      )
    }
  }
}
