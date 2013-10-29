package org.magnum.soda.example.maint.test;

import org.magnum.soda.example.maint.LoginActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivityTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {

	private LoginActivity loginActivity;
	private EditText username;
	private EditText password;
	private Button signin;
	private static final String TESTName= "alice";
	private static final String TESTPwd= "123";
	
	public LoginActivityTest() {
		super(LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);

		loginActivity = getActivity();
		username = (EditText) loginActivity.findViewById(org.magnum.soda.example.maint.R.id.username_edit);
		username = (EditText) loginActivity.findViewById(org.magnum.soda.example.maint.R.id.password_edit);
		signin = (Button) loginActivity.findViewById(org.magnum.soda.example.maint.R.id.signin_button);

		username.setText(TESTName);
		username.setText(TESTPwd);
		
	}
	
	public void testRestart(){
		loginActivity.finish();
		loginActivity = this.getActivity();
		assertEquals(username.getText().toString(),TESTName);
		assertEquals(password.getText().toString(),TESTPwd);
		
	}
}
