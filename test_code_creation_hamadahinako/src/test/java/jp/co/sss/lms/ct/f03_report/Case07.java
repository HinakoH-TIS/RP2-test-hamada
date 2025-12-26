package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {
	
	private String caseNo = "case07";

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");

		assertEquals("ログイン | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_showLogin");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']"));

		loginId.clear();
		loginId.sendKeys("StudentAA01");
		password.clear();
		password.sendKeys("StudentAA01Pass");

		getEvidence(new Object() {}, caseNo, "01_inputFilled");

		loginButton.click();

		waitForUrlToBe("http://localhost:8080/lms/course/detail");

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		
		//「クラス、オブジェクト」の詳細ボタンをクリック
		WebElement detailButton = webDriver.findElement(
				By.xpath("//*[@id=\"main\"]/div/div[3]/div[2]/table/tbody/tr[4]/td[5]/form/input[3]"));
		detailButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/section/detail");
		
		WebElement sectionTitle = webDriver.findElement(By.xpath("//*[@id=\"sectionDetail\"]/h2"));
		
		assertEquals("セクション詳細 | LMS" , webDriver.getTitle());
		assertThat(sectionTitle.getText(), is(containsString("クラス、オブジェクト")));
		getEvidence(new Object() {}, caseNo, "01_sectionDetail");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		
		WebElement toReportPageButton = webDriver.findElement(By.cssSelector("input[type='submit']"
											+ "[value='日報【デモ】を提出する']"));
		toReportPageButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/report/regist");
		
		assertEquals("レポート登録 | LMS" , webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_dailyReport");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		WebElement reportTextarea = webDriver.findElement(By.id("content_0"));
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		reportTextarea.clear();
		reportTextarea.sendKeys("本日は練習問題を5問解きました。");
		
		getEvidence(new Object() {}, caseNo, "01_reportFilled");
		
		submitButton.click();
		
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		assertEquals("セクション詳細 | LMS" , webDriver.getTitle());
		WebElement submissionStatus = webDriver.findElement(
				By.xpath("//*[@id=\"sectionDetail\"]/table/tbody/tr[2]/td/form/input[6]"));
		
		assertEquals("提出済み日報【デモ】を確認する", submissionStatus.getAttribute("value"));
		getEvidence(new Object() {}, caseNo, "02_AfterReportSubmitted");
	}

}
