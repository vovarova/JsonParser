package fine.project.parser

/**
 * @author Vroman
 */
object T {
  def main(args: Array[String]): Unit = {
    val str = "{\"vova\":\"vova\",\"t\":{\"d\":\"d\"}}"
    val t = HandlerManger.getHandler(str, 0)
    t.process(str, 0)
    
  }
}