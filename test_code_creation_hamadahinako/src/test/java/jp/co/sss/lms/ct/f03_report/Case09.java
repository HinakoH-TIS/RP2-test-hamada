package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.Constant;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {
	
	private String caseNo = "case09";

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		WebElement userNameButton = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ１さん"));
		userNameButton.click();
		
		pageLoadTimeout(10);
		
		assertEquals("ユーザー詳細", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_userDetail");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		scrollTo("300");
		
		WebElement modifyButton = webDriver.findElement(
				By.xpath("//*[@id=\"main\"]/table[3]/tbody/tr[3]/td[5]/form[2]/input[1]"));
		
		modifyButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/report/regist"));
		
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_reportRegist");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		WebElement topic = webDriver.findElement(By.id("intFieldName_0"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		
		topic.clear();
		scrollBy("300");
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("理解度を入力した場合は、学習項目は必須です。")));
		
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		WebElement topic = webDriver.findElement(By.id("intFieldName_0"));
		WebElement understanding = webDriver.findElement(By.id("intFieldValue_0"));
		Select understandingDropdown = new Select(understanding);
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		
		topic.sendKeys("ITリテラシー①");
		understandingDropdown.selectByIndex(0);
		scrollBy("200");
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("学習項目を入力した場合は、理解度は必須です。")));
		
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		WebElement understanding = webDriver.findElement(By.id("intFieldValue_0"));
		Select understandingDropdown = new Select(understanding);
		WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
	
		understandingDropdown.selectByIndex(2);
		achievementLevel.clear();
		achievementLevel.sendKeys("a");
		scrollBy("300");
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("目標の達成度は半角数字で入力してください。")));
		
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
	
		achievementLevel.clear();
		achievementLevel.sendKeys("11");
		scrollBy("300");
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("目標の達成度は、半角数字で、1～10の範囲内で入力"
				+ "してください。")));
		
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		WebElement impression = webDriver.findElement(By.id("content_1"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
	
		achievementLevel.clear();
		impression.clear();
		scrollBy("200");
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("目標の達成度は半角数字で入力してください。")));
		assertThat(errorList.get(1).getText(), is(containsString("所感は必須です。")));
		
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		WebElement impression = webDriver.findElement(By.id("content_1"));
		WebElement weeklyReview = webDriver.findElement(By.id("content_2"));
		WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
	
		scrollBy("200");
		achievementLevel.sendKeys("6");
		impression.sendKeys(Constant.INPUT_ERROR_STRING);
		weeklyReview.clear();
		weeklyReview.sendKeys(Constant.INPUT_ERROR_STRING);
		submitButton.click();
		
		//エラーが表示されるまで待機
		visibilityTimeout(By.className("error"), 5);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(0).getText(), is(containsString("所感の長さが最大値(2000)を超えています。")));
		assertThat(errorList.get(1).getText(), is(containsString("一週間の振り返りの長さが最大値(2000)を超えています。")));
		
		scrollBy("200");
		getEvidence(new Object() {}, caseNo, "01_reportRegistFail");
	}

}
