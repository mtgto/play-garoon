package controllers

import models.OrganizationRepository
import models.Organizations.organizationWrites
import play.api.mvc.Controller
import models.OrganizationId
import scala.util.Failure
import scala.util.Success
import play.api.libs.json.Json

object OrganizationController extends Controller with BaseController {
  def getOrganization(id: Int) = Authenticated { request =>
    val organizationRepository = new OrganizationRepository(garoonClient, request.user)
    organizationRepository.resolve(OrganizationId(id.toString)) match {
      case Success(user) => Ok(Json.toJson(user))
      case Failure(e) => NotFound(Json.obj("message" -> "組織が見つかりませんでした"))
    }
  }
}
