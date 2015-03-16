package controllers

import models.Organizations.organizationWrites
import net.mtgto.garoon.user.{OrganizationId, OrganizationRepository}
import play.api.libs.json.Json
import play.api.mvc.Controller
import scala.util.Failure
import scala.util.Success

object OrganizationController extends Controller with BaseController {
  def getOrganization(id: Int) = Authenticated { request =>
    val organizationRepository = new OrganizationRepository(garoonClient, request.user)
    organizationRepository.resolve(OrganizationId(id.toString)) match {
      case Success(user) => Ok(Json.toJson(user))
      case Failure(e) => NotFound(Json.obj("message" -> "組織が見つかりませんでした"))
    }
  }
}
