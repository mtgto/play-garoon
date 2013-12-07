package models

import play.api.libs.json.{JsValue, Json, Writes}
import net.mtgto.garoon.user.User

object Users {
  implicit val userWrites = new Writes[User] {
    def writes(u: User): JsValue = {
      Json.obj(
        "id" -> u.identity.value,
        "name" -> u.name,
        "login_name" -> u.loginName
      )
    }
  }
}
