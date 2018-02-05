/**
  * Created by maaamir on 2/5/18.
  */
import java.io.{PrintWriter,File}
import java.util.Date
import java.text.SimpleDateFormat

/**
  * Task assigned by Travel Audience: Finding active users and week day bizzers
  * */

object mainClass {

  def toDate(dateString: String): Date = {
    val formatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss")
    val date: Date = formatter.parse(dateString)
    return date
  }
  def toString(d:Date): String ={
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateString=dateFormat.format(d)
    return dateString
  }

  def isBusinessHour(time:Date): Boolean ={
    if(time.getDay>0 &&time.getDay<6 && time.getHours>=9 &&time.getHours<=16) //from 9-16:59 time, monday to friday
      return true
    else return false
  }

  def readData(dataFile:String): List[(String,Date,String,String)] = {
    val data = scala.io.Source.fromFile(dataFile).getLines().drop(1) // drop header
      .map(t => t.split(",")).map(t => (t(0), toDate(t(1)),t(2),t(3)))//take
      .toList
    return data
  }

  def getUserEvents(data:List[(String,Date,String,String)])
  : Map[String,List[(String,Date,String,String)]] ={
    val usersEvents=data.groupBy(_._1)
    return usersEvents
  }

  def getActiveUsers(userEvents:Map[String,List[(String,Date,String,String)]], minEvents:Int,activityPeriod:Long)
  : Set[String] ={
    val activeUsers=userEvents.filter(_._2.size>minEvents)// filter users having less than min activities
      .map(t=> (t._1,(t._2.maxBy(_._2)._2.getTime - t._2.minBy(_._2)._2.getTime)/t._2.size)) // average time difference for activity
      .filter(t=> t._2<activityPeriod) // filter users with average activity within given period
      .map(_._1).toSet// filterUsers
    return activeUsers
  }

  def getWeekDayBizzers(userEvents:Map[String,List[(String,Date,String,String)]],bizPercent:Double)
  : Set[String] ={
    var bizHrsPercent=0.0
    val bizUsers=userEvents.filter{ue=>
      bizHrsPercent=ue._2.filter(t=> isBusinessHour(t._2)).size.toDouble/ue._2.size.toDouble
      bizHrsPercent>bizPercent //filter on percentage of office hours
    }.map(_._1).toSet
    return bizUsers
  }


  //input parameters: inputDataFile,outputDateFile,minEvents,ActivityHour,WeekDayPercent

    def main(args: Array[String]): Unit = {
      val inputFile = args(0)
      val data = readData(inputFile)
      println("total rows::"+data.size)
      val minEvents=args(2).toInt // ignore users having events less than this number
      val activityPeriod= args(3).toLong*60*60*1000 // hourly active users
      //find events/activities of each user
      val userEvents=getUserEvents(data)
      println("total users::"+userEvents.size)
      //find active users
      val activeUsers=getActiveUsers(userEvents,minEvents,activityPeriod)
      println("active users per: "+args(3)+"hours ::"+activeUsers.size)
      val bizPercent=args(4).toDouble
      val weekDayBizers=getWeekDayBizzers(userEvents,bizPercent)
      println("Users having activity during office hours greater than "+bizPercent+" percent::"+weekDayBizers.size)
      /**write output*/
      val outputFile=args(1)
      val writer=new PrintWriter(new File(outputFile))
      writer.println("uuid,ts,useragent,hashed_ip,hourlyActive,WeekdayBizzers")
      data.foreach{t=>
        writer.println(t._1+","+toString(t._2)+","+t._3+","+t._4+","+activeUsers.contains(t._1)
          +","+weekDayBizers.contains(t._1))
      }
      writer.close()
      println("Output file written")
    }
}
