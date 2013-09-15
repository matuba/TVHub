import java.util.Calendar;

import org.junit.*;

import tv.TVProgramme;
import tv.TvListings;
import static org.fest.assertions.Assertions.*;

public class TvListingsXMLTest {
	TvListings m_objXML = new TvListings();
	TVProgramme programme0 = null;
	@Before
	public void before(){
		m_objXML = new TvListings();
		m_objXML.LoadXML("public/listings/27ch.xml");
    	programme0 = m_objXML.getProgramme(0);
	}
    @Test 
    public void test読み込み成功() {
    	TvListings obj = new TvListings();
    	assertThat(obj.LoadXML("public/listings/27ch.xml")).isEqualTo(true);
    }
    @Test 
    public void testチャンネルID取得() {
    	assertThat(m_objXML.getChannelID()).isEqualTo("GR27");
    }
    @Test 
    public void testチャンネル名取得() {
    	assertThat(m_objXML.getChannelName()).isEqualTo("ＮＨＫ総合１・東京");
    }
    @Test 
    public void test番組数取得() {
    	assertThat(m_objXML.getProgrammeNum()).isEqualTo(459);
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
    public void testProgramme0番目の開始時刻をクラスに設定() {
    	Calendar calendor = Calendar.getInstance(); 
    	calendor.set(2013, 7, 27, 12, 0, 0);
    	assertThat(programme0.start.toString()).isEqualTo(calendor.getTime().toString());
    }
    @Test
    public void testProgramme0番目の終了時刻をクラスに設定() {
    	Calendar calendor = Calendar.getInstance(); 
    	calendor.set(2013, 7, 27, 12, 15, 0);
    	assertThat(programme0.stop.toString()).isEqualTo(calendor.getTime().toString());
    }
       
}
