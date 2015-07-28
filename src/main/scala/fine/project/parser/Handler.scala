package fine.project.parser

import scala.collection.mutable.Set
import scala.util.control.Breaks._
import scala.util.control._
/**
 * @author Vroman
 */

object JsonParser {
  def parse(str: String): Component = {
    val h = HandlerManger.getHandler(str, 0)
    val jsonObject = h()
    val lastEl = Utils.getElementAfter(str, jsonObject)
    //assert(lastEl._1.isWhitespace & lastEl._2.equals(str.length() - 1), "Not everything parsed")
    jsonObject._1
  }
}

object HandlerManger {
  private val handlers = Set(ObjectHandler, StringPrimitiveHanlder,ArrayHandler,IntPrimitiveHanlder)

  def getHandler(str: String, position: Int): () => (Component, Int) = {
    val h = handlers.filter { _.handle(str, position) }.head
    () => {
      h.process(str, position)
    }
  }
}

// {} object ,[] array , "" | 1 | true primitives
trait Handler {
  def handle(str: String, pos: Int): Boolean
  def process(str: String, pos: Int): (Component, Int)
}
object ArrayHandler extends Handler {

  def handle(str: String, pos: Int): Boolean = {
    val elem = Utils.getNextElement(str, pos)._1
    Utils.equalsElements(elem, "[")
  }
  def process(str: String, pos: Int): (Component, Int) = {
    var currentElement: (Char, Int) = Utils.getNextElement(str, pos)
    Utils.equalsElements(currentElement._1, "[")
    currentElement = Utils.getElementAfter(str, currentElement)
    val arrayObject = new ArrayObject
    while (!Utils.equalsElements(currentElement._1, "]")) {
      val handler = HandlerManger.getHandler(str, currentElement._2)
      val elem = handler()
      arrayObject.getElements += elem._1
      currentElement = Utils.getElementAfter(str, elem)
      if (currentElement._1.toString().equals(",")) {
        currentElement = Utils.getElementAfter(str, currentElement)
      }
    }
    assert(Utils.equalsElements(currentElement._1, "]"))
    (arrayObject, currentElement._2)
  }

}


object ObjectHandler extends Handler {

  def handle(str: String, pos: Int): Boolean = {
    val elem = Utils.getNextElement(str, pos)._1
    Utils.equalsElements(elem, Separators.OPEN_CURLY_BRACKET)
  }
  def process(str: String, pos: Int): (Component, Int) = {
    var currentElement: (Char, Int) = Utils.getNextElement(str, pos)
    Utils.equalsElements(currentElement._1, Separators.OPEN_CURLY_BRACKET)    
    currentElement = Utils.getElementAfter(str, currentElement)
    val oComp = new ObjectComponent
    while (!Utils.equalsElements(currentElement._1, Separators.CLOSE_CURLY_BRACKET)) {
      val attrName: (String, Int) = Utils.getStringValue(str, currentElement)
      val devider = Utils.getElementAfter(str, attrName)
      assert(devider._1.toString().equals(Separators.COLON))
      val handler = HandlerManger.getHandler(str, Utils.getPointAfter(devider))
      val elem = handler()
      oComp.getAttributes += new Attribute(attrName._1, elem._1)
      currentElement = Utils.getElementAfter(str, elem)
      if (currentElement._1.toString().equals(",")) {
        currentElement = Utils.getElementAfter(str, currentElement)
      }
    }
    assert(Utils.equalsElements(currentElement._1, Separators.CLOSE_CURLY_BRACKET))
    (oComp, currentElement._2)
  }

}

object Utils {

  private val loop = new Breaks

  def getlastPosition(str: String): Int = {
    str.length() - 1
  }

  def getNextPosition(str: String, curentPosition: Int): Int = {
    if (isEnd(str, curentPosition)) {
      return str.length() - 1
    }
    var nextPosition = curentPosition

    loop.breakable {
      while (str.charAt(nextPosition).isWhitespace) {
        nextPosition += 1
        if (isEnd(str, nextPosition)) {
          nextPosition = getlastPosition(str)
          loop.break()
        }
      }
    }

    nextPosition
  }

  def equalsElements(el1: Any, el2: Any): Boolean = {
    el1.toString().equalsIgnoreCase(el2.toString())
  }

  def getElementAfter(str: String, el: (Any, Int)): (Char, Int) = {
    getNextElement(str, (el._2) + 1)
  }
  def getElementAfter(str: String, pos: Int): (Char, Int) = {
    getNextElement(str, pos + 1)
  }

  def getPointAfter(el: (Any, Int)): Int = {
    el._2 + 1
  }

  def isEnd(str: String, curentPosition: Int): Boolean = {
    curentPosition > getlastPosition(str)
  }
  def getNextElement(str: String, curentPosition: Int): (Char, Int) = {
    val position = getNextPosition(str, curentPosition)
    (str.charAt(position), position)

  }

  def getStringValue(str: String, elem: (Char, Int)): (String, Int) = {
    getStringValue(str, elem._2)
  }

  def getStringValue(str: String, pos: Int): (String, Int) = {
    val builder = StringBuilder.newBuilder
    var currentElement = Utils.getNextElement(str, pos)
    val firstQuotationMarkPosition = currentElement._2
    assert(Utils.equalsElements(currentElement._1, Separators.QUOTATION_MARK))
    loop.breakable {
      while ((!Utils.equalsElements(currentElement._1, Separators.QUOTATION_MARK)) || firstQuotationMarkPosition.equals(currentElement._2) ) {
        builder.append(currentElement._1)
        val nextPosition = currentElement._2 + 1
        if (Utils.isEnd(str, nextPosition)) {
          sys.error("No \"")
          loop.break()
        }
        currentElement = (str.charAt(nextPosition), nextPosition)
      }
    }
    builder.deleteCharAt(0)
    (builder.toString(), currentElement._2)
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
  val COLON = ":"
}

object StringPrimitiveHanlder extends Handler {
  def handle(str: String, pos: Int): Boolean = {
    Utils.getNextElement(str, pos)._1.toString().equals(Separators.QUOTATION_MARK)
  }

  def process(str: String, pos: Int): (Component, Int) = {
    val res = Utils.getStringValue(str, pos)
    (new StringPrimitiveObject(res._1), res._2)
  }
}
object IntPrimitiveHanlder extends Handler {
  private val loop = new Breaks
  def handle(str: String, pos: Int): Boolean = {
    Utils.getNextElement(str, pos)._1.isDigit
  }

  def process(str: String, pos: Int): (Component, Int) = {
    val builder = StringBuilder.newBuilder
    var currentElement = Utils.getNextElement(str, pos)
    loop.breakable {
      while (currentElement._1.isDigit || currentElement._1.toString.equals(".")) {
        builder.append(currentElement._1)
        val nextPosition = currentElement._2 + 1
        if (Utils.isEnd(str, nextPosition)) {
          sys.error("No \"")
          loop.break()
        }
        currentElement = (str.charAt(nextPosition), nextPosition)
      }
    }
    val res = builder.toString()
    (new IntPrimitiveObject(res.toInt), currentElement._2)
  }
}

