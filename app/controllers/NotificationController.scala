package controllers

import com.github.nscala_time.time.Imports._
import models.Notifications.notificationWrites
import net.mtgto.garoon.notification.NotificationService
import play.api.libs.json.Json
import play.api.mvc.Controller
import scala.util.{Failure, Success}

object NotificationController extends Controller with BaseController {
  def getNotifications(start: Long) = Authenticated { request =>
    val notificationService = new NotificationService(garoonClient, request.user)
    notificationService.getNotificationVersions(new DateTime(start * 1000)).flatMap { notificationItems =>
      notificationService.getNotifications(notificationItems.map(n => (n.moduleId, n.item)))
    } match {
      case Success(notifications) => Ok(Json.toJson(notifications))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }
}
