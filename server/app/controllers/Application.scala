package controllers

import play.api._
import play.api.libs.json._
import play.api.libs.json.util._
import play.api.libs.json.Writes._
import play.api.mvc._

import play.api.Play.current

import java.io.File

case class FileInfo(path: String, name: String, isDirectory: Boolean){

  lazy val fullPath = "%s/%S".format(path, name)

  lazy val hash: String = {
    isDirectory match {
      case true => utils.Hash(fullPath)
      case false => models.Directory.hash(fullPath).getOrElse(utils.Hash(fullPath))
    }
  }

  lazy val toJson: JsValue = Json.obj(
    "path" -> path,
    "name" -> name,
    "hash" -> hash,
    "isDirectory" -> isDirectory
  ) 
}

object FileInfo{
  def apply( path: String, file: File ) = {
    new FileInfo( path, file.getName, file.isDirectory)
  }
}

object Application extends Controller {
    lazy val rootPath = Play.configuration.getString("root.path").get
    val headers = ( "Access-Control-Allow-Origin" -> "*" )

    def home = Action {
      Ok(views.html.index("Your new application is ready."))
    }

    def index = Action {
      utils.Indexer.index(rootPath)
      Ok( "\n   " + models.Directory.findAll().mkString("\n   ") )
    }

    def listRootFile = Action { request =>
      listFiles(".")
    }

    def listFile( path: String) = Action { request =>
      listFiles(path)
    }

    private def listFiles( path: String, listHidden : Boolean = false): SimpleResult[_] = {
      val directory = new File(rootPath, path)
      val response = (directory.exists, directory.isDirectory) match{
        case (true, true) => Ok( Json.toJson( listFiles(path, directory, listHidden).map(_.toJson) ) )
        case _ => NotFound
      }
      response.withHeaders(headers)     
    }

    private def listFiles( path: String, directory: File, listHidden : Boolean ) : Seq[FileInfo] = {
      directory.listFiles.filter({ f =>
        ( listHidden || !f.getName.startsWith(".") ) && ( !f.isDirectory || !f.listFiles.isEmpty )
      }).map( FileInfo(path, _) )
    }
  
    def servFile( path: String ) = Action { request =>
      val fileToServe = new File(rootPath, path)
      val response = (fileToServe.exists, fileToServe.isDirectory) match {
        case (true, false) => Ok.sendFile(fileToServe, inline = true)
        case _ => NotFound
      }
      response.withHeaders(headers)
  }
}
