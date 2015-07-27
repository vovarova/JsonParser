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
    s"$name : ${element.toString()}"
  }
}
class ObjectComponent() extends Component {
  val getAttributes = new MutableList[Attribute]()

  override def toString(): String = {
    val b = StringBuilder.newBuilder
    b.append("{")
    getAttributes.foreach(x => { b.append(s"${x.toString()} ,") })
    if(getAttributes.size>0){
      b.setLength(b.length() - 2)
    }
    b.append("}").toString()
  }

}

class PrimitiveObject(v: String) extends Component {
  override def toString(): String = {
    v
  }

}
class ArrayObject() extends Component {
  def getAttributes = new MutableList[Component]()
  //def getAttributes(name:String):Attribute = {getAttributes.
}