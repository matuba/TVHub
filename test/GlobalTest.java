import java.util.Calendar;
import java.util.Date;

import org.junit.*;
import play.mvc.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class GlobalTest {
    @Test 
    public void test実行時間が４時以前の時に開始時間までの秒数を取得() {
    	Calendar nowCalendar = Calendar.getInstance();
    	nowCalendar.set( 2013, 9, 20, 3, 59, 0);
    	Global globalJobs = new Global();
    	long firstProcSecond = globalJobs.getAfterSecondsListingsProc( nowCalendar);
        assertThat(firstProcSecond).isEqualTo(60 * 1);
    }

    @Test 
    public void test実行時間が４時以後の時に開始時間までの秒数を取得() {
    	Calendar nowCalendar = Calendar.getInstance();
    	nowCalendar.set( 2013, 9, 20, 4, 0, 0);
    	Global globalJobs = new Global();
    	long firstProcSecond = globalJobs.getAfterSecondsListingsProc( nowCalendar);
        assertThat(firstProcSecond).isEqualTo(60 * 60 * 24);
    }
   
}
