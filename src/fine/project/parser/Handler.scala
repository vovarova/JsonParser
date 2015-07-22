package fine.project.parser

import scala.collection.mutable.Set
import scala.util.control.Breaks._

/**
 * @author Vroman
 */
object HandlerManger {
  private def handlers = Set(ObjectHandler,StringPrimitiveHanlder)

  def registerHandler(h: Handler) {
//    handlers += h

  }

  def getHandler(str: String, position: Int): Handler = {
    val t = handlers.filter { _.handle(str, position) }
    t.head
  }

}

// {} object ,[] array , "" | 1 | true primitive
abstract class Handler() {
  HandlerManger.registerHandler(this)
  def handle(str: String, pos: Int): Boolean
  def process(str: String, pos: Int): (Component, Int)

}

object ObjectHandler extends Handler {
  def handle(str: String, pos: Int): Boolean = {
    val elem=Utils.getNextElement(str, pos)._1
    elem.toString().equalsIgnoreCase(Separators.OPEN_CURLY_BRACKET)

  }
  def process(str: String, pos: Int): (Component, Int) = {

    var (nextElement, currentPosition) = Utils.getNextElement(str, pos + 1)
    //while(!nextElement.equals(Separators.QUOTATION_MARK)){

    val oComp = new ObjectComponent

    while (!nextElement.toString().equals(Separators.CLOSE_CURLY_BRACKET)) {
      val attrName = Utils.getStringValue(str, currentPosition)
      val devider = Utils.getNextElement(str, attrName._2)
      assert(devider._1.toString().equals(":"))

      currentPosition = devider._2 + 1

      val handler = HandlerManger.getHandler(str, currentPosition)
      val elem = handler.process(str, currentPosition)
      currentPosition = elem._2
      oComp.getAttributes += new Attribute(attrName._1, elem._1)
      var nE = Utils.getNextElement(str, currentPosition)
      if (nE._1.toString().equals(",")) {
        nE = Utils.getNextElement(str, currentPosition)
      }
      nextElement = nE._1
      currentPosition = nE._2
    }
    assert(nextElement.toString().equals(Separators.CLOSE_CURLY_BRACKET))
    (oComp, currentPosition + 1)
  }

}

object Utils {
  def getNextPosition(str: String, curentPosition: Int): Int = {
    if (isEnd(str, curentPosition)) {
      return curentPosition
    }
    var nextPosition = curentPosition
    while (str.charAt(nextPosition).isWhitespace) {
      nextPosition += 1
      if (isEnd(str, curentPosition)) {
        nextPosition = str.length() + 1
        break
      }
    }
    nextPosition
  }

  def isEnd(str: String, curentPosition: Int): Boolean = {
    curentPosition > str.length() + 1
  }
  def getNextElement(str: String, curentPosition: Int): (Char, Int) = {
    val position = getNextPosition(str, curentPosition)
    (str.charAt(position), position)

  }

  def getStringValue(str: String, pos: Int): (String, Int) = {
    val builder = StringBuilder.newBuilder
    var (nextElement, currentPosition) = Utils.getNextElement(str, pos)
    assert(nextElement.toString().equals(Separators.QUOTATION_MARK))
    val startElem = Utils.getNextElement(str, currentPosition + 1)
    nextElement = startElem._1
    currentPosition = startElem._2

    while (!nextElement.toString().equals(Separators.QUOTATION_MARK)) {
      builder.append(nextElement)
      currentPosition += 1
      if (Utils.isEnd(str, currentPosition)) {
        sys.error("No \"")
        break
      }
      nextElement = str.charAt(currentPosition)
    }
/*    if (!Utils.isEnd(str, currentPosition)) {
      currentPosition += 1
    }*/

    (builder.toString(), currentPosition)
  }

}
object Separators {
  val COMMA = ","
  val OPEN_CURLY_BRACKET = "{"
  val CLOSE_CURLY_BRACKET = "}"
  val OPEN_BRACKET = "("
  val CLOSE_BRACKET = ")"
  val OPEN_SQUERE_BRACKET = "("
  val CLOSE_SQUERE_BRACKET = ")"
  val QUOTATION_MARK = "\""
}

object StringPrimitiveHanlder extends Handler {
  def handle(str: String, pos: Int): Boolean = {
    Utils.getNextElement(str, pos)._1.toString().equals(Separators.QUOTATION_MARK)
  }

  def process(str: String, pos: Int): (Component, Int) = {
    val res = Utils.getStringValue(str, pos)
    (new PrimitiveObject(res._1), res._2)
  }
}

