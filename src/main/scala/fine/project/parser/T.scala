package fine.project.parser

/**
 * @author Vroman
 */
object T {
  def main(args: Array[String]): Unit = {
    val str = "             {\"              vova\":\"1\",\"t\":\n\r    {\"d\":\"     d\"} ,\n\r \"d\":\"d\"}   "
    println(str)
    val z = JsonParser.parse(str)   
    println(z)
  }
}