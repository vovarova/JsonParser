package fine.project.parser
import scala.collection.mutable.MutableList

/**
 * @author Vroman
 *
 */
trait Component {
  def toString(): String
}

class Attribute(n: String, c: Component) extends Component {
  def name = n
  def element = c
  override def toString(): String = {
    s""""$name" : ${element.toString()}"""
  }
}
class ObjectComponent() extends Component {
  val getAttributes = new MutableList[Attribute]()

  override def toString(): String = {
    val b = StringBuilder.newBuilder
    b.append("{")
    getAttributes.foreach(x => { b.append(s"${x.toString()} ,") })
    if(getAttributes.size>0){
      b.setLength(b.length - 2)
    }
    b.append("}").toString()
  }

}

class StringPrimitiveObject(v: String) extends Component {
  override def toString(): String = {
    s""""$v""""
  }

}
class IntPrimitiveObject(v: Int) extends Component {
  override def toString(): String = {
    v.toString
  }
}
class BooleanPrimitiveObject(v: Boolean) extends Component {
  override def toString(): String = {
    v.toString
  }
}

class ArrayObject() extends Component {
  val getElements = new MutableList[Component]()
  override def toString(): String = {
    val b = StringBuilder.newBuilder
    b.append("[")
    getElements.foreach(x => { b.append(s"${x.toString()} ,") })
    if(getElements.size>0){
      b.setLength(b.length - 2)
    }
    b.append("]").toString()
  }
}