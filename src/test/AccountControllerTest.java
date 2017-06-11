package test;

import model.Account;
import controller.AccountController;
import dao.AccountDAO;
import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;
import java.sql.*;
import java.util.Scanner;


public class AccountControllerTest {
@Mock private AccountDAO accountdao;
@Mock public MockitoRule rule = MockitoJUnit.rule();
@Mock private Account account;
@Mock Scanner input = new Scanner(System.in);

	@Test
	public void testInsertController() {
		AccountController accountController = new AccountController();
		System.out.print(" Toets uw keuze in : ");
		switch(input.nextInt()){
		case 1:	accountController.InsertAccount();
		break;
		case 2: accountController.ShowAccount();
		break;
		case 3: accountController.DeleteAccount();
		}
		
		 Mockito.verify(accountdao).InsertAccount(account);
		 Mockito.verify(accountdao).ShowAccount(account);
		 Mockito.verify(accountdao).DeletAccount(account);
		
		}
		
		
	
	@Test
	public void test(){
		
		AccountController accountController = new AccountController();
		/* insert  */
	    Assert.assertEquals(input.nextLine(), account.getNaam());
		Assert.assertEquals(input.nextLine(), account.getWachtWoord());
		Assert.assertEquals(input.nextInt(), account.getAccountStatus());
		Mockito.verify(accountdao).InsertAccount(account);
		/*  show  */
		Assert.assertEquals(input.nextLine(), account.getNaam());
		Mockito.verify(accountdao).ShowAccount(account);
		/*  delete  */
		Assert.assertEquals(input.nextLine(), account.getNaam());
		Mockito.verify(accountdao).DeletAccount(account);
		
		
		
		
		
	}
	private Account createAccount(){
		Account account = new Account();
		account.setNaam(input.nextLine());
		account.setWachtWoord(input.nextLine());
		account.setAccountStatus(input.nextInt());
		return account;
	}

}

