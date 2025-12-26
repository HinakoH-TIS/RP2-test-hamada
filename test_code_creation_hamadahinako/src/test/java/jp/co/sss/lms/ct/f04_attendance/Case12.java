package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import jp.co.sss.lms.ct.util.Constant;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {
	
	private String caseNo = "case12";

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
		
		//出勤・退勤時刻が表示されるようウィンドウサイズを調整
        int width = 2000;
        int height = 1500;
        webDriver.manage().window().setSize(new Dimension(width, height));
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

		waitForUrlToBe("http://localhost:8080/lms/course/detail");

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		WebElement attendanceButton = webDriver.findElement(By.linkText("勤怠"));
		attendanceButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/attendance/detail");
		
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_attendance");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		WebElement editAttendanceButton = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		
		editAttendanceButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/attendance/update");
		
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "01_attendanceEdit");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		Select startMinute01 = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour01 = new Select(webDriver.findElement(By.id("endHour0")));
		Select startHour02 = new Select(webDriver.findElement(By.id("startHour1")));
		Select endMinute02 = new Select(webDriver.findElement(By.id("endMinute1")));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		startMinute01.selectByVisibleText("");
		endHour01.selectByVisibleText("");
		startHour02.selectByVisibleText("");
		endMinute02.selectByVisibleText("");
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditFilled");
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("error"), 10);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		assertThat(errorList.get(0).getText(), is(containsString("出勤時間が正しく入力されていません。")));
		assertThat(errorList.get(1).getText(), is(containsString("退勤時間が正しく入力されていません。")));
		assertThat(errorList.get(2).getText(), is(containsString("出勤時間が正しく入力されていません。")));
		assertThat(errorList.get(3).getText(), is(containsString("退勤時間が正しく入力されていません。")));
		
		getEvidence(new Object() {}, caseNo, "02_attendanceEditFail");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		Select startHour01 = new Select(webDriver.findElement(By.id("startHour0")));
		Select endHour01 = new Select(webDriver.findElement(By.id("endHour0")));
		Select startHour02 = new Select(webDriver.findElement(By.id("startHour1")));
		Select endMinute02 = new Select(webDriver.findElement(By.id("endMinute1")));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		startHour01.selectByVisibleText("");
		endHour01.selectByVisibleText("18");
		startHour02.selectByVisibleText("09");
		endMinute02.selectByVisibleText("00");
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditFilled");
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("error"), 10);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		assertThat(errorList.get(0).getText(), is(containsString("出勤情報がないため退勤情報を入力出来ません。")));
		
		getEvidence(new Object() {}, caseNo, "02_attendanceEditFail");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		Select startHour01 = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute01 = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour01 = new Select(webDriver.findElement(By.id("endHour0")));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		startHour01.selectByVisibleText("16");
		startMinute01.selectByVisibleText("00");
		endHour01.selectByVisibleText("14");
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditFilled");
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("error"), 10);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		assertThat(errorList.get(0).getText(), is(containsString("退勤時刻[0]は出勤時刻[0]より後でなければいけません。")));
		
		getEvidence(new Object() {}, caseNo, "02_attendanceEditFail");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		Select startHour01 = new Select(webDriver.findElement(By.id("startHour0")));
		Select endHour01 = new Select(webDriver.findElement(By.id("endHour0")));
		Select blankTime01 = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		startHour01.selectByVisibleText("09");
		endHour01.selectByVisibleText("11");
		blankTime01.selectByVisibleText("3時間");
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditFilled");
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("error"), 10);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		assertThat(errorList.get(0).getText(), is(containsString("中抜け時間が勤務時間を超えています。")));
		
		getEvidence(new Object() {}, caseNo, "02_attendanceEditFail");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		Select startHour01 = new Select(webDriver.findElement(By.id("startHour0")));
		Select endHour01 = new Select(webDriver.findElement(By.id("endHour0")));
		Select blankTime01 = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		WebElement notes01 = webDriver.findElement(By.name("attendanceList[0].note"));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		startHour01.selectByVisibleText("09");
		endHour01.selectByVisibleText("18");
		blankTime01.selectByVisibleText("");
		notes01.sendKeys(Constant.NOTES_TOO_LONG);
		
		getEvidence(new Object() {}, caseNo, "01_attendanceEditFilled");
		
		scrollBy("document.body.scrollHeight");
		
		updateButton.click();
		
		//ダイアログにフォーカスして「OK」ボタンを押す
		webDriver.switchTo().alert().accept();
		
		//メッセージが表示されるまで待機
		visibilityTimeout(By.className("error"), 10);
		
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		assertThat(errorList.get(0).getText(), is(containsString("備考の長さが最大値(100)を超えています。")));
		
		getEvidence(new Object() {}, caseNo, "02_attendanceEditFail");
	}

}
