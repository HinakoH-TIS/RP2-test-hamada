package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.Constant.*;
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
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {
	
	private String caseNo = "case16";

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
		loginId.sendKeys(FIRST_LOGIN_USER);
		password.clear();
		password.sendKeys(FIRST_LOGIN_USER);
		
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
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		WebElement changeButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		changeButton.click();
		
		//モーダル上の変更ボタンをクリック
		visibilityTimeout(By.id("upd-btn"), 5);
		WebElement modalChangeButton = webDriver.findElement(By.id("upd-btn"));
		getEvidence(new Object() {}, caseNo, "01_changePasswordModal");
		modalChangeButton.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(1).getText(), is(containsString("現在のパスワードは必須です。")));
		assertThat(errorList.get(2).getText(), is(containsString("パスワードは必須です。")));
		assertThat(errorList.get(3).getText(), is(containsString("確認パスワードは必須です。")));
		
		getEvidence(new Object() {}, caseNo, "02_changePasswordFail");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement confirmPassword = webDriver.findElement(By.id("passwordConfirm"));
		WebElement changeButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		currentPassword.sendKeys(FIRST_LOGIN_USER);
		password.sendKeys(PASSWORD_TOO_LONG);
		confirmPassword.sendKeys(PASSWORD_TOO_LONG);
		
		getEvidence(new Object() {}, caseNo, "01_changePasswordFilled");
		
		changeButton.click();
		
		//モーダル上の変更ボタンをクリック
		visibilityTimeout(By.id("upd-btn"), 5);
		WebElement modalChangeButton = webDriver.findElement(By.id("upd-btn"));
		modalChangeButton.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(1).getText(), is(containsString("パスワードの長さが最大値(20)を超えています。")));
		
		getEvidence(new Object() {}, caseNo, "02_changePasswordFail");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement confirmPassword = webDriver.findElement(By.id("passwordConfirm"));
		WebElement changeButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		currentPassword.sendKeys(FIRST_LOGIN_USER);
		password.sendKeys(PASSWORD_FORMAT_ERROR);
		confirmPassword.sendKeys(PASSWORD_FORMAT_ERROR);
		
		getEvidence(new Object() {}, caseNo, "01_changePasswordFilled");
		
		changeButton.click();
		
		//モーダル上の変更ボタンをクリック
		visibilityTimeout(By.id("upd-btn"), 5);
		WebElement modalChangeButton = webDriver.findElement(By.id("upd-btn"));
		modalChangeButton.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(1).getText(), is(containsString("「パスワード」には半角英数字のみ使用可能です。"
				+ "また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。")));
		
		getEvidence(new Object() {}, caseNo, "02_changePasswordFail");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement confirmPassword = webDriver.findElement(By.id("passwordConfirm"));
		WebElement changeButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		currentPassword.sendKeys(FIRST_LOGIN_USER);
		password.sendKeys(FIRST_LOGIN_USER + "Pass");
		confirmPassword.sendKeys(FIRST_LOGIN_USER + "Passs");
		
		getEvidence(new Object() {}, caseNo, "01_changePasswordFilled");
		
		changeButton.click();
		
		//モーダル上の変更ボタンをクリック
		visibilityTimeout(By.id("upd-btn"), 5);
		WebElement modalChangeButton = webDriver.findElement(By.id("upd-btn"));
		modalChangeButton.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorList = webDriver.findElements(By.className("error"));
		
		assertThat(errorList.get(1).getText(), is(containsString("パスワードと確認パスワードが一致しません。")));
		
		getEvidence(new Object() {}, caseNo, "02_changePasswordFail");
	}

}
