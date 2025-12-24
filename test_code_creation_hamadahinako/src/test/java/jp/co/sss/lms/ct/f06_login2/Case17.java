package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
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
 * 結合テスト ログイン機能②
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {
	
	private String caseNo = "case17";

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']"));
		
		loginId.clear();
		loginId.sendKeys("StudentAA05");
		password.clear();
		password.sendKeys("StudentAA05");
		
		getEvidence(new Object() {}, caseNo, "01_inputFilled");
		
		loginButton.click();
		
		visibilityTimeout(By.tagName("h2"), 10);
		
		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_agreement");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		WebElement checkBox = webDriver.findElement(By.cssSelector("input[type='checkbox']"));
		WebElement nextButton = webDriver.findElement(By.className("btn-primary"));
		
		checkBox.click();
		getEvidence(new Object() {}, caseNo, "01_agreementChecked");
		
		nextButton.click();
		
		pageLoadTimeout(5);
		
		assertEquals("パスワード変更 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_changePassword");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement confirmPassword = webDriver.findElement(By.id("passwordConfirm"));
		WebElement changeButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		currentPassword.sendKeys("StudentAA05");
		password.sendKeys("StudentAA05Pass");
		confirmPassword.sendKeys("StudentAA05Pass");
		
		getEvidence(new Object() {}, caseNo, "01_changePasswordFilled");
		
		changeButton.click();
		
		//モーダル上の変更ボタンをクリック
		visibilityTimeout(By.id("upd-btn"), 5);
		WebElement modalChangeButton = webDriver.findElement(By.id("upd-btn"));
		modalChangeButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/course/detail"));
		
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

}
