package controllers

import models.Users.userWrites
import net.mtgto.garoon.user.{UserId, UserRepository}
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import play.api.libs.json.Json
import play.api.mvc.Controller
import scala.util.{Failure, Success}
import models.ExtendedUserRepository

object UserController extends Controller with BaseController {
  def getCurrentUser = Authenticated { request =>
    val userRepository = new ExtendedUserRepository(garoonClient, request.user)

    userRepository.getLoggedInUserId match {
      case Success(userId) =>
        userRepository.resolve(userId) match {
          case Success(user) => Ok(Json.toJson(user))
          case Failure(e) if e.isInstanceOf[EntityNotFoundException] =>
            NotFound(Json.obj("message" -> "ユーザーが見つかりませんでした"))
        }
      case Failure(e) =>
        Unauthorized(Json.obj("message" -> "ログインユーザーのIDが取得できませんでした"))
    }
  }

  def getUser(id: Int) = Authenticated { request =>
    val userRepository = new ExtendedUserRepository(garoonClient, request.user)
    userRepository.resolve(UserId(id.toString)) match {
      case Success(user) => Ok(Json.toJson(user))
      case Failure(e) => NotFound(Json.obj("message" -> "ユーザーが見つかりませんでした"))
    }
  }

  def getUsersBy(loginNamesString: String) = Authenticated { request =>
    val userRepository = new ExtendedUserRepository(garoonClient, request.user)
    val loginNames = loginNamesString.split(',').toSeq
    userRepository.findByLoginNames(loginNames) match {
      case Success(users) => Ok(Json.toJson(users))
      case Failure(e) => NotFound(Json.obj("message" -> "ユーザーが見つかりませんでした"))
    }
  }
}
