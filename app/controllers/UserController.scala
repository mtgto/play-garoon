package controllers

import models.Users.userWrites
import net.mtgto.garoon.user.{UserId, UserRepository}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.util.{Failure, Success}

object UserController extends Controller with BaseController {
  private[this] val userRepository: UserRepository = new UserRepository(garoonClient)

  def getUser(id: Int) = Action {
    userRepository.resolve(UserId(id.toString)) match {
      case Success(user) => Ok(Json.toJson(user))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }

  def getUsersBy(loginNamesString: String) = Action {
    val loginNames = loginNamesString.split(',').toSeq
    userRepository.findByLoginNames(loginNames) match {
      case Success(users) => Ok(Json.toJson(users))
      case Failure(e) => NotFound(Json.toJson(""))
    }
  }
}
