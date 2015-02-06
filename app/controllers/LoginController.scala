package controllers

import net.mtgto.garoon.{SessionCookie, LoginService, Password}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import scala.util.{Failure, Success}

object LoginController extends Controller with BaseController {
  val passwordReads: Reads[Password] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String]
    )(Password.apply _)

  def login = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[Password](passwordReads) match {
          case s: JsSuccess[Password] =>
            val loginService = new LoginService(garoonClient)
            loginService.login(s.get.username, s.get.password) match {
              case Success(cookie) => Ok(Json.obj("cookie" -> cookie.value))
              case Failure(error) => Unauthorized(Json.obj("message" -> "ログインに失敗しました"))
            }
          case e: JsError => BadRequest(Json.obj("message" -> "リクエストが正しくありません"))
        }
      case None => BadRequest(Json.obj("message" -> "JSONでリクエストする必要があります"))
    }
  }

  def getRequestToken = Authenticated { request =>
    request.user match {
      case sessionCookie: SessionCookie =>
        val loginService = new LoginService(garoonClient)
        loginService.getRequestToken(sessionCookie.cookie) match {
          case Success(token) => Ok(Json.obj("request_token" -> token.value))
          case Failure(e) => InternalServerError(Json.obj("message" -> "リクエストトークンの発行に失敗しました"))
        }
      case _ =>
        println(request.user)
        BadRequest(Json.obj("message" -> "ログインセッションが必要です"))
    }
  }
}
