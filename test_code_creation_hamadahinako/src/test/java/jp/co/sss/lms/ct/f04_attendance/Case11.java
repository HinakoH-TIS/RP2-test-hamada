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
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {
	
	private String caseNo = "case11";

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		WebElement editAttendanceButton = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		
		editAttendanceButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/attendance/update"));
		
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_attendanceEdit");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		WebElement regularTimeButton01 = webDriver.findElement(By.cssSelector("button[type='button'][value='0']"));
		WebElement regularTimeButton02 = webDriver.findElement(By.cssSelector("button[type='button'][value='1']"));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		regularTimeButton01.click();
		regularTimeButton02.click();
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 10);
		
		WebElement message = webDriver.findElement(By.className("alert-info"));
		WebElement clockInTime01 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[3]/div/table/tbody/tr[1]/td[3]"));
		WebElement clockInTime02 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[3]/div/table/tbody/tr[2]/td[3]"));
		WebElement clockOutTime01 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[3]/div/table/tbody/tr[1]/td[4]"));
		WebElement clockOutTime02 = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[3]/div/table/tbody/tr[2]/td[4]"));
		
		assertEquals("09:00", clockInTime01.getText());
		assertEquals("09:00", clockInTime02.getText());
		assertEquals("18:00", clockOutTime01.getText());
		assertEquals("18:00", clockOutTime02.getText());
		assertThat(message.getText(), is(containsString("勤怠情報の登録が完了しました。")));
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditSuccess");
	}

}
