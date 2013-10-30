import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.TVProgramme;
import models.TvListings;

import org.junit.*;

import static org.fest.assertions.Assertions.*;

public class TvListingsXMLTest {
	TvListings listingsGR27 = new TvListings();
	TVProgramme programme0 = null;
	@Before
	public void before(){
		listingsGR27 = new TvListings();
		listingsGR27.LoadXML("public/listings/27.xml");
    	programme0 = listingsGR27.getProgramme(0);
	}
    @Test 
    public void test読み込み成功() {
    	TvListings obj = new TvListings();
    	assertThat(obj.LoadXML("public/listings/27ch.xml")).isEqualTo(true);
    }
    @Test 
    public void testチャンネルID取得() {
    	assertThat(listingsGR27.getChannelID()).isEqualTo("27");
    }
    @Test 
    public void testチャンネル名取得() {
    	assertThat(listingsGR27.getChannelName("27")).isEqualTo("ＮＨＫ総合１・東京");
    }
    @Test 
    public void test番組数取得() {
    	assertThat(listingsGR27.getProgrammeNum()).isEqualTo(422);
    }
    @Test
    public void testProgramme0番目の番組タイトルをクラスに設定() {
    	assertThat(programme0.title).isEqualTo("ニュース【字】");
    }
    @Test
    public void testProgramme0番目のカテゴリをクラスに設定() {
    	assertThat(programme0.category).isEqualTo("ニュース・報道");
    }
    @Test
    public void testProgramme0番目の解説をクラスに設定() {
    	assertThat(programme0.desc).isEqualTo("");
    }
    @Test
    public void testProgramme0番目のチャンネル名をクラスに設定() {
    	assertThat(programme0.channel).isEqualTo("27");
    }
    @Test
    public void testProgramme0番目の開始時刻をクラスに設定() {
    	Calendar calendor = Calendar.getInstance(); 
    	calendor.set(2013, 9, 23, 18, 0, 0);
    	assertThat(programme0.start.toString()).isEqualTo(calendor.getTime().toString());
    }
    @Test
    public void testProgramme0番目の終了時刻をクラスに設定() {
    	Calendar calendor = Calendar.getInstance(); 
    	calendor.set(2013, 9, 23, 18, 10, 0);
    	assertThat(programme0.stop.toString()).isEqualTo(calendor.getTime().toString());
    }
    @Test
    public void test12時から13時の番組を取得() {
    	Calendar calendar = Calendar.getInstance(); 
    	
    	calendar.set(2013, 8, 24, 12, 00, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	Date start = calendar.getTime();

    	calendar.set(2013, 8, 24, 13, 00, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	Date stop = calendar.getTime();

    	List<TVProgramme> programme = listingsGR27.getTVProgrammeList("27",start, stop);
    	assertThat(programme.size()).isEqualTo(3);
    }
    @Test
    public void test13時から14時の番組を取得() {
    	Calendar calendar = Calendar.getInstance(); 
    	calendar.set(2013, 8, 24, 13, 00, 0);
    	Date start = calendar.getTime();
    	calendar.set(2013, 8, 24, 14, 00, 0);
    	Date stop = calendar.getTime();

    	List<TVProgramme> programme = listingsGR27.getTVProgrammeList("27",start, stop);
    	assertThat(programme.size()).isEqualTo(3);
    }
       
}
