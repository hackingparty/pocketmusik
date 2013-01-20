package controllers

import play.api._
import play.api.libs.json._
import play.api.libs.json.util._
import play.api.libs.json.Writes._
import play.api.mvc._

import play.api.Play.current

import java.io.File

case class FileInfo(path: String, name: String, isDirectory: Boolean){
	lazy val toJson: JsValue = Json.obj(
		"path" -> path,
		"name" -> name,
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

  	def index = Action {
   		Ok(views.html.index("Your new application is ready."))
  	}

  	def listRootFile = Action { request =>
  		listFiles(".")
  	}

  	def listFile( path: String) = Action { request =>
  		listFiles(path)
  	}

  	private def listFiles( path: String, listHidden : Boolean = false) = {
  		val folder = new File(rootPath, path)
    	val response = (folder.exists, folder.isDirectory) match{
    		case (true, true) => {
          if(listHidden)
            Ok( Json.toJson( folder.listFiles.map( FileInfo(path, _).toJson )) )
          else
            Ok( Json.toJson( folder.listFiles.filter({ f => !f.getName.startsWith(".")}).map( FileInfo(path, _).toJson )) )
    		}
    		case _ => NotFound
    	}
    	response.withHeaders(headers)  		
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
