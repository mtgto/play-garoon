package controllers

import models.Users.userWrites
import net.mtgto.garoon.user.{UserId, UserRepository}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.util.{Failure, Success}

object UserController extends Controller with BaseController {
  def getUser(id: Int) = Authenticated { request =>
    val userRepository = new UserRepository(garoonClient, request.user)
    userRepository.resolve(UserId(id.toString)) match {
      case Success(user) => Ok(Json.toJson(user))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }

  def getUsersBy(loginNamesString: String) = Authenticated { request =>
    val userRepository = new UserRepository(garoonClient, request.user)
    val loginNames = loginNamesString.split(',').toSeq
    userRepository.findByLoginNames(loginNames) match {
      case Success(users) => Ok(Json.toJson(users))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }
}
