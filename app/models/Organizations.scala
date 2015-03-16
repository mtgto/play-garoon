package models

import net.mtgto.garoon.user.Organization
import play.api.libs.json.{Json, JsValue, Writes}

object Organizations {
  implicit val organizationWrites = new Writes[Organization] {
    def writes(o: Organization): JsValue = {
      Json.obj(
        "id"                  -> o.identity.value,
        "name"                -> o.name,
        "description"         -> o.description,
        "parent_organization" -> o.parentOrganizationId.value,
        "members"             -> o.members.map(_.value)
      )
    }
  }
}
