package models

import cagette._

case class Directory( fullPath: String, hash: String) 

object Directory extends Cageot[Directory, String]()(Identifier(_.fullPath)){

  def hash( fullPath: String ): Option[String] = {
    findById(fullPath).map( _.hash )
  }
}