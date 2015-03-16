package models

import net.mtgto.garoon.user.UserId
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes

object Users {
  implicit val userWrites = new Writes[ExtendedUser] {
    def writes(u: ExtendedUser): JsValue = {
      Json.obj(
        "id" -> u.identity.value,
        "name" -> u.name,
        "login_name" -> u.loginName,
        "organizations" -> u.organizations,
        "primary_organization" -> u.primaryOrganization
      )
    }
  }
}
