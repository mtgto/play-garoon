package controllers

import java.nio.charset.StandardCharsets
import java.util.Base64
import net.mtgto.garoon.{Password, Cookie, SessionCookie, Authentication}
import play.api.libs.json.Json
import play.api.mvc.RequestHeader
import play.api.mvc.Results.Unauthorized
import play.api.mvc.Security.AuthenticatedBuilder

object Authenticated extends AuthenticatedBuilder[Authentication](
  Authenticate.getAuthenticationFromRequest, req => Unauthorized(Json.toJson(Map("message" -> "Bad credentials")))) {

}

private[this] object Authenticate {
  private[this] val basicAuthorizationHeaderRegex = """Basic ([a-zA-Z\d=]+)""".r

  private[this] val basicAuthorizationRegex = """([^:]+):(.+)""".r

  def getAuthenticationFromRequest(request: RequestHeader): Option[Authentication] = {
    request.cookies.get("CBSESSID") match {
      case Some(cookie) => Some(SessionCookie(Cookie(cookie.value)))
      case _ => request.headers.get("Authorization") match {
        case Some(basicAuthorizationHeaderRegex(base64string)) =>
          new String(Base64.getDecoder.decode(base64string), StandardCharsets.UTF_8) match {
            case basicAuthorizationRegex(username, password) => Some(Password(username, password))
            case _ => None
          }
        case _ => None
      }
    }
  }
}
