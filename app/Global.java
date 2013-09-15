import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import play.*;
import play.libs.Akka;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import scala.concurrent.duration.*;

public class Global extends GlobalSettings {

    ActorRef tickActor;
    
    public int startListingsHour = 4;
    public int startListingsMin = 0;
    Cancellable cancellableListings = null;

    @Override
    public void onStart(Application app) {
    	long afterSecondsFirstProc = getAfterSecondsListingsProc(Calendar.getInstance());
    	cancellableListings = Akka.system().scheduler().schedule(
                Duration.create(afterSecondsFirstProc, TimeUnit.SECONDS),
        		Duration.create(1, TimeUnit.DAYS),
                new Runnable() {
                    @Override
                    public void run() {
                        Logger.info("ON START ---    " + System.currentTimeMillis());
                    }
                }
                ,Akka.system().dispatcher()
        );
    }
    /*
     * 指定時刻に起動して番組表xmlファイルを出力
     * その後は同じ時刻に毎日実行する
     * xmlファイルを出力後にデータベースへ登録する
     * */
    public long getAfterSecondsListingsProc( Calendar nowCalendar){
    	Logger.info("Listing proc" + Integer.toString(startListingsHour) + ":" + Integer.toString(startListingsMin));

    	Date now = nowCalendar.getTime();
    	Calendar firstProcCalendar = Calendar.getInstance();
    	firstProcCalendar.set( nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH), nowCalendar.get(Calendar.DATE), startListingsHour, startListingsMin, 0);
    	Date firstProc = firstProcCalendar.getTime();

    	if(now.compareTo(firstProc) >= 0){
    		firstProcCalendar.add(Calendar.DATE, 1);
    		firstProc = firstProcCalendar.getTime();
    	}
    	return (firstProc.getTime() - now.getTime())/1000;
    }

}