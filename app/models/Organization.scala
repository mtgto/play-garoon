package models

import scala.util.Try
import scala.xml.Node
import scala.xml.XML
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import net.mtgto.garoon.Authentication
import net.mtgto.garoon.GaroonClient
import net.mtgto.garoon.Id
import net.mtgto.garoon.user.UserId
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes
import org.sisioh.dddbase.core.model.Entity

trait Organization extends Entity[OrganizationId] {
  val identity: OrganizationId
  val name: String
  val description: String
  val parentOrganization: Option[String]
  val members: Array[UserId]
}

class OrganizationId(override val value: String) extends Id(value)

object OrganizationId {
  def apply(value: String): OrganizationId = new OrganizationId(value)
}

object Organization {
  private[this] case class DefaultOrganization(
      identity: OrganizationId, name: String, description: String,
      parentOrganization: Option[String], members: Array[UserId])
      extends Organization

  def apply(node: Node): Organization = {
    val identity = OrganizationId((node \ "@key").text)
    val name = (node \ "@name").text
    val description = (node \ "@description").text
    val parentOrganization = (node \ "@parent_organization").text
    val members = (node \ "members" \ "user").map(n => UserId((n \ "@id").text))
    DefaultOrganization(identity, name, description,
        Option(parentOrganization), members.toArray)
  }
}

class OrganizationRepository(client: GaroonClient, auth: Authentication) extends SyncEntityReader[OrganizationId, Organization] {
  def resolve(identity: OrganizationId)(implicit ctx: EntityIOContext[Try]): Try[Organization] = {
    val actionName = "BaseGetOrganizationsById"
    val parameters = client.factory.createOMElement("parameters", null)
    val eventNode = client.factory.createOMElement("organization_id", null)
    eventNode.setText(identity.value)
    parameters.addChild(eventNode)

    val result = client.sendReceive(actionName, "/cbpapi/base/api", parameters)(auth, None)
    result.map { element =>
      val node = XML.loadString(element.toString)
      (node \ "returns" \ "organization").map(Organization(_)).headOption.getOrElse(throw new EntityNotFoundException)
    }
  }

  def containsByIdentity(identity: OrganizationId)(implicit ctx: EntityIOContext[Try]): Try[Boolean] =
    resolve(identity).map(_ => true)
}

/**
 *  json writes
 */
object Organizations {
  implicit val organizationWrites = new Writes[Organization] {
    def writes(o: Organization): JsValue = {
      Json.obj(
        "id"                  -> o.identity.value,
        "name"                -> o.name,
        "description"         -> o.description,
        "parent_organization" -> o.parentOrganization,
        "members"             -> o.members.map(_.value)
      )
    }
  }
}
