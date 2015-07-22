package fine.project.parser

import scala.collection.mutable.ListBuffer

/**
 * @author Vroman
 * 
 */
trait Component {
    
}

 class Attribute(n:String,c:Component) extends Component{
     def name = n
     def element = c
 }
 class ObjectComponent() extends Component{
     def getAttributes = new ListBuffer[Attribute]()
     //def getAttributes(name:String):Attribute = {getAttributes.
 }
 
 class PrimitiveObject(v:String) extends Component{
     def value = v;
     //def getAttributes(name:String):Attribute = {getAttributes.
 }
 class ArrayObject() extends Component{
     def getAttributes = new ListBuffer[Component]()
     //def getAttributes(name:String):Attribute = {getAttributes.
 }