package controllers

import com.github.nscala_time.time.Imports._
import models.Notifications.notificationWrites
import net.mtgto.garoon.RequestToken
import net.mtgto.garoon.notification.{ModuleId, NotificationService}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.Controller
import scala.util.{Failure, Success}

object NotificationController extends Controller with BaseController {
  implicit val notificationIdReads: Reads[(ModuleId, String)] = (
    (JsPath \ "module_id").read[String].map(ModuleId.apply) and
      (JsPath \ "item").read[String]
    )((_, _))

  def getNotifications(start: Long) = Authenticated { request =>
    val notificationService = new NotificationService(garoonClient, request.user)
    notificationService.getNotificationVersions(new DateTime(start * 1000)).flatMap { notificationItems =>
      notificationService.getNotifications(notificationItems.map(n => (n.moduleId, n.item)))
    } match {
      case Success(notifications) => Ok(Json.toJson(notifications))
      case Failure(e) => NotFound(Json.obj("message" -> "通知が取得できませんでした"))
    }
  }

  def checkNotifications(requestToken: String) = Authenticated { request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[Seq[(ModuleId, String)]] match {
          case s: JsSuccess[Seq[(ModuleId, String)]] =>
            val notificationService = new NotificationService(garoonClient, request.user)
            notificationService.checkNotifications(s.get)(RequestToken(requestToken)) match {
              case Success(notifications) => Ok(Json.toJson(notifications))
              case Failure(e) => NotFound(Json.toJson(""))
            }
          case e: JsError => BadRequest(Json.obj("message" -> "リクエストが正しくありません"))
        }
      case None => BadRequest(Json.obj("message" -> "JSONでリクエストする必要があります"))
    }
  }
}
