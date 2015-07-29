package fine.project.parser

/**
 * @author Vroman
 */

import java.io.InputStream

import scala.io.Source
object T {
  def main(args: Array[String]): Unit = {

    val filename = "/test.json"

    val stream : InputStream = getClass.getResourceAsStream(filename)
    val b = StringBuilder.newBuilder
    for (line <- Source.fromInputStream(stream).getLines()) {
      b append line
    }
    val str = b.toString()
    val z = JsonParser.parse(str)
    println(z)
  }
}