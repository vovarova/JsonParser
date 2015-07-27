package fine.project.parser

object scsd {
  val t = "   {dsdsdsd}"                          //> t  : String = "   {dsdsdsd}"
  println(t.charAt(Utils.getNextPosition(t, 4)))  //> d
  
  
  
  HandlerManger.getHandler("{\"vova\":\"vova\",\"t\":{\"d\":\"d\"}}", 0)
                                                  //> java.util.NoSuchElementException: next on empty iterator
                                                  //| 	at scala.collection.Iterator$$anon$2.next(Iterator.scala:39)
                                                  //| 	at scala.collection.Iterator$$anon$2.next(Iterator.scala:37)
                                                  //| 	at scala.collection.mutable.FlatHashTable$$anon$1.next(FlatHashTable.sca
                                                  //| la:212)
                                                  //| 	at scala.collection.IterableLike$class.head(IterableLike.scala:107)
                                                  //| 	at scala.collection.AbstractIterable.head(Iterable.scala:54)
                                                  //| 	at fine.project.parser.HandlerManger$.getHandler(Handler.scala:17)
                                                  //| 	at fine.project.parser.scsd$$anonfun$main$1.apply$mcV$sp(fine.project.pa
                                                  //| rser.scsd.scala:9)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at fine.project.parser.scsd$.main(fine.project
                                                  //| Output exceeds cutoff limit.
  
  }