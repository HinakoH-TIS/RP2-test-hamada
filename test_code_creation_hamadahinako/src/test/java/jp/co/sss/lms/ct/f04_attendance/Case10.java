package jp.co.sss.lms.ct.f04_attendance;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {
	
	private String caseNo = "case10";

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
		loginId.sendKeys("StudentAA07");
		password.clear();
		password.sendKeys("StudentAA07Pass");

		getEvidence(new Object() {}, caseNo, "01_inputFilled");

		loginButton.click();
		
		pageLoadTimeout(10);

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		WebElement attendanceButton = webDriver.findElement(By.linkText("勤怠"));
		attendanceButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/attendance/detail"));
		
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_attendance");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		WebElement clockInButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='出勤']"));
		clockInButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 10);
		
		WebElement message = webDriver.findElement(By.className("alert-info"));
		assertThat(message.getText(), is(containsString("勤怠情報の登録が完了しました。")));
		getEvidence(new Object() {}, caseNo, "01_attendanceClockedIn");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		WebElement clockOutButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='退勤']"));
		clockOutButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 10);
		
		WebElement message = webDriver.findElement(By.className("alert-info"));
		assertThat(message.getText(), is(containsString("勤怠情報の登録が完了しました。")));
		getEvidence(new Object() {}, caseNo, "01_attendanceClockedOut");
	}

}
