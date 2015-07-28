package fine.project.parser

/**
 * @author Vroman
 */
import scala.io.Source
object T {
  def main(args: Array[String]): Unit = {

    val filename = "D:/test.js"
    val b = StringBuilder.newBuilder
    for (line <- Source.fromFile(filename).getLines()) {
      b append line
    }
    val str = b.toString()
    val z = JsonParser.parse(str)
    println(z)
  }
}