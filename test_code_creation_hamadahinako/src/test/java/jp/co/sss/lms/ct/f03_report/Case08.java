package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {
	
	private String caseNo = "case08";

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
		
		pageLoadTimeout(10);

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		//「アルゴリズム、フローチャート」の詳細ボタンをクリック
		WebElement detailButton = webDriver.findElement(
				By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/table/tbody/tr[2]/td[5]/form/input[3]"));
		detailButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/section/detail");
		
		WebElement sectionTitle = webDriver.findElement(By.xpath("//*[@id=\"sectionDetail\"]/h2"));
		
		assertEquals("セクション詳細 | LMS" , webDriver.getTitle());
		assertThat(sectionTitle.getText(), is(containsString("アルゴリズム、フローチャート")));
		getEvidence(new Object() {}, caseNo, "01_sectionDetail");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement checkReportButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='提出済み週報【デモ】を確認する']"));
		
		checkReportButton.click();
		
		pageLoadTimeout(10);
		
		assertEquals("レポート登録 | LMS" , webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_weeklyReport");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		
		Select levelDropdown = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		WebElement ahievementLevel = webDriver.findElement(By.id("content_0"));
		WebElement impression = webDriver.findElement(By.id("content_1"));
		WebElement weeklyReview = webDriver.findElement(By.id("content_2"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		
		//レポート内容を書き換え
		levelDropdown.selectByVisibleText("3");
		ahievementLevel.clear();
		ahievementLevel.sendKeys("4");
		impression.clear();
		impression.sendKeys("週報を1回編集しました");
		weeklyReview.clear();
		weeklyReview.sendKeys("今週は演習問題を5問解けました。");
		
		scrollBy("100");
		getEvidence(new Object() {}, caseNo, "01_weeklyReportFilled");
		
		submitButton.click();
			
		pageLoadTimeout(10);
		
		assertEquals("セクション詳細 | LMS" , webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_sectionDetail");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		WebElement userNameButton = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ１さん"));
		userNameButton.click();
		
		pageLoadTimeout(10);
		
		assertEquals("ユーザー詳細" , webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_userDetail");
		
		//レポート一覧が見える位置までスクロールしてエビデンス取得
		scrollBy("250");
		getEvidence(new Object() {}, caseNo, "02_userDetailBottom");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		
		scrollBy("200");
		
		WebElement detailButton = webDriver.findElement(
				By.xpath("//*[@id=\"main\"]/table[3]/tbody/tr[3]/td[5]/form[1]/input[1]"));
		
		detailButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/report/detail");
		
		assertEquals("レポート詳細 | LMS" , webDriver.getTitle());
		
		WebElement comprehensionLevel = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/table/tbody/tr[2]/td[2]"));
		WebElement ahievementLevel = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[1]/td"));
		WebElement impression = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[2]/td"));
		WebElement weeklyReview = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[3]/td"));
		
		//レポート内容がTest05で書き換えた内容と一致するか確認
		assertEquals("3", comprehensionLevel.getText());
		assertEquals("4", ahievementLevel.getText());
		assertEquals("週報を1回編集しました", impression.getText());
		assertEquals("今週は演習問題を5問解けました。", weeklyReview.getText());
		
		getEvidence(new Object() {}, caseNo, "01_reportDetail");
	}

}
