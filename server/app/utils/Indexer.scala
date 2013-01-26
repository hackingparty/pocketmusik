package utils

import java.io.File

object Indexer{
  
  def index( path: String ) = {
    models.Directory.delete( p => true )
    val file = new File(path)
    if( file.exists ) indexFile(path, file)
  }

  def indexFile( path: String, file: File ): String = {
    val fullPath = "%s/%s".format(path, file.getName)

    file.isDirectory match {
      case false => Hash(fullPath)
      case true if !file.listFiles.isEmpty => {
        val directory = models.Directory(fullPath, Hash(
          file.listFiles
              .map({ f => indexFile(fullPath,f) })
            .sorted
            .foldLeft("")({ (acc, str) => acc + str })
        ))

        models.Directory.save(directory)
        directory.hash
      }
      case _ => ""
    }
  }
}