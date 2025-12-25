package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 試験実施機能
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果50点")
public class Case14 {
	
	private String caseNo = "case14";

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/course/detail"));

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		WebElement detailButton = webDriver.findElement(
				By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/table/tbody/tr[2]/td[5]/form/input[3]"));
		
		detailButton.click();
		
		visibilityTimeout(By.tagName("h2"), 10);
		
		WebElement sectionName = webDriver.findElement(By.tagName("h2"));
		
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertThat(sectionName.getText(), is(containsString("アルゴリズム、フローチャート")));
		
		getEvidence(new Object() {}, caseNo, "01_sectionDetail");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		WebElement detailButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='詳細']"));
		
		detailButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/exam/start"));
		
		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_examStart");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		WebElement startExamButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='試験を開始する']"));
		
		startExamButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/exam/question"));
		
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_exam");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		
		ArrayList<WebElement> answers = new ArrayList<WebElement>();
		answers.add(webDriver.findElement(By.id("answer-0-0")));
		answers.add(webDriver.findElement(By.id("answer-1-0")));
		answers.add(webDriver.findElement(By.id("answer-2-2")));
		answers.add(webDriver.findElement(By.id("answer-3-0")));
		answers.add(webDriver.findElement(By.id("answer-4-1")));
		answers.add(webDriver.findElement(By.id("answer-5-3")));
		answers.add(webDriver.findElement(By.id("answer-6-2")));
		answers.add(webDriver.findElement(By.id("answer-7-2")));
		answers.add(webDriver.findElement(By.id("answer-8-1")));
		answers.add(webDriver.findElement(By.id("answer-9-1")));
		answers.add(webDriver.findElement(By.id("answer-10-1")));
		answers.add(webDriver.findElement(By.id("answer-11-3")));
		
		//1問ごとに回答してエビデンス取得を繰り返す
		for (int i = 1; i <= answers.size() ; i++) {
			answers.get(i-1).click();
			String suffix = "answer" + String.format("%02d", i);
			getEvidence(new Object() {}, caseNo, suffix);
			scrollBy("350");
		}

		scrollTo("document.body.scrollHeight");
		WebElement toConfirmationButton = webDriver.findElement(By.cssSelector("input[type='submit']"
				+ "[value='確認画面へ進む']"));
		
		toConfirmationButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/exam/answerCheck"));
		
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_answerConfirmation");
		
		scrollBy("document.body.scrollHeight");
		getEvidence(new Object() {}, caseNo, "02_answerConfirmationBottom");
		
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException{
		WebElement sendButton = webDriver.findElement(By.id("sendButton"));
		
		//試験時間にNULLが入るのを防ぐため、2秒待機
		Thread.sleep(2000);
		
		sendButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();;
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/exam/result"));
		
		WebElement sectionName = webDriver.findElement(By.tagName("h2"));
		
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertThat(sectionName.getText(), is(containsString("あなたのスコア：50.0点")));
		
		getEvidence(new Object() {}, caseNo, "01_examResult");
		
		scrollBy("document.body.scrollHeight");
		getEvidence(new Object() {}, caseNo, "02_examResultBottom");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		WebElement returnButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='戻る']"));
		
		returnButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/exam/start"));
		
		//過去の試験結果の一番下の行を取得し、点数が50.0点になっているか確認
		scrollBy("document.body.scrollHeight");
		WebElement score = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/table[2]/tbody/tr[last()]/td[2]"));
		
		assertEquals("50.0点", score.getText());
		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		
		getEvidence(new Object() {}, caseNo, "01_examScore");
	}

}
