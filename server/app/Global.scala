
import play.api._

object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    Logger.debug( "Looking for files in the specified folder...")
    utils.Indexer.index(controllers.Application.rootPath)
    Logger.debug( "\n   " + models.Directory.findAll().mkString("\n   "))
  }
  
}
