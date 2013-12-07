package controllers

import java.net.URI
import net.mtgto.garoon.GaroonClient
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext
import play.api.Play
import play.api.mvc.Controller

trait BaseController {
  self: Controller =>

  protected[this] def getConfiguration(name: String): String = Play.current.configuration.getString(name).get

  protected[this] implicit val context = SyncEntityIOContext

  protected[this] val garoonClient: GaroonClient = new GaroonClient(
    getConfiguration("garoon.username"),
    getConfiguration("garoon.password"),
    new URI(getConfiguration("garoon.uri"))
  )
}
