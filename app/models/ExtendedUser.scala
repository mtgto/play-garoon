package models

import net.mtgto.garoon.user.User
import net.mtgto.garoon.user.UserId
import scala.xml.Node
import net.mtgto.garoon.Authentication
import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import net.mtgto.garoon.GaroonClient
import scala.util.Try
import scala.xml.XML
import org.sisioh.dddbase.core.lifecycle.EntityNotFoundException

trait ExtendedUser extends User {
  val organizations: Array[String]
  val primaryOrganization: String
}

object ExtendedUser {
  private[this] case class DefaultUser(
      identity: UserId, name: String, loginName: String,
      organizations: Array[String], primaryOrganization: String)
      extends ExtendedUser

  def apply(node: Node): ExtendedUser = {
    val identity = UserId((node \ "@key").text)
    val name = (node \ "@name").text
    val loginName = (node \ "@login_name").text
    val organizations = (node \ "organization").map(n => (n \ "@id").text)
    val primaryOrganization = (node \ "@primary_organization").text
    DefaultUser(identity, name, loginName, organizations.toArray, primaryOrganization)
  }
}

class ExtendedUserRepository(client: GaroonClient, auth: Authentication) extends SyncEntityReader[UserId, ExtendedUser] {
  def resolve(identity: UserId)(implicit ctx: EntityIOContext[Try]): Try[ExtendedUser] = {
    val actionName = "BaseGetUsersById"
    val parameters = client.factory.createOMElement("parameters", null)
    val eventNode = client.factory.createOMElement("user_id", null)
    eventNode.setText(identity.value)
    parameters.addChild(eventNode)

    val result = client.sendReceive(actionName, "/cbpapi/base/api", parameters)(auth, None)
    result.map { element =>
      val node = XML.loadString(element.toString)
      (node \ "returns" \ "user").map(ExtendedUser(_)).headOption.getOrElse(throw new EntityNotFoundException)
    }
  }

  def containsByIdentity(identity: UserId)(implicit ctx: EntityIOContext[Try]): Try[Boolean] =
    resolve(identity).map(_ => true)

  def findByLoginNames(loginNames: Seq[String]): Try[Seq[ExtendedUser]] = {
    val actionName = "BaseGetUsersByLoginName"
    val parameters = client.factory.createOMElement("parameters", null)
    loginNames.foreach { loginName =>
      val loginNameNode = client.factory.createOMElement("login_name", null)
      loginNameNode.setText(loginName)
      parameters.addChild(loginNameNode)
    }

    val result = client.sendReceive(actionName, "/cbpapi/base/api", parameters)(auth, None)
    result.map { element =>
      val node = XML.loadString(element.toString)
      (node \ "returns" \ "user").map(ExtendedUser(_))
    }
  }

  def getLoggedInUserId: Try[UserId] = {
    val actionName = "UtilGetLoginUserId"
    val parameters = client.factory.createOMElement("parameters", null)
    val result = client.sendReceive(actionName, "/util_api/util/api", parameters)(auth, None)
    result.map { element =>
      val node = XML.loadString(element.toString)
      UserId((node \ "returns" \ "user_id").text)
    }
  }
}
