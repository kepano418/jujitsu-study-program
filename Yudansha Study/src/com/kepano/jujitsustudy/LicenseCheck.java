package com.kepano.jujitsustudy;

/**
 * @author Nick Eubanks
 * 
 * Copyright (C) 2010 Android Infinity (http://www.androidinfinity.com)
 *
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

/**
 * NOTES ON USING THIS LICENSE FILE IN YOUR APPLICATION: 1. Define the package
 * of you application above 2. Be sure your public key is set properly @BASE64_PUBLIC_KEY
 * 3. Change your SALT using random digits 4. Under AllowAccess, Add your
 * previously used MainActivity 5. Add this activity to your manifest and set
 * intent filters to MAIN and LAUNCHER 6. Remove Intent Filters from previous
 * main activity
 */
public class LicenseCheck extends Activity {
	private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
		public void allow(int reason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// Should allow user access.
			startMainActivity();

		}

		public void applicationError(int reason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}
			// This is a polite way of saying the developer made a mistake
			// while setting up or calling the license checker library.
			// Please examine the error code and fix the error.
			toast("Error: " + reason);
			startMainActivity();

		}

		public void dontAllow(int reason) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
			}

			// Should not allow access. In most cases, the app should assume
			// the user has access unless it encounters this. If it does,
			// the app should inform the user of their unlicensed ways
			// and then either shut down the app or limit the user to a
			// restricted set of features.
			// In this example, we show a dialog that takes the user to Market.
			showDialog(0);
		}
	}

	private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoAQIcwkUkqz4oovQoWKN4ft+jGf9ncSEGTWX53xOXlOi5NpTv6D35Pxk+KDYeQfenMmlAboSwp1d8/DsF67r6OsEI4ig7MApatx6WgzlvQyelrzqJJusCxJ65peLYMQ+meWpJN3BV1Lik6GcLmKZ9ZTybRQRScYshQs3bYNZRBGmCLnsPjHVWzkBcBiosQVVoOpD0OLdtb0MWkhLyp6XNk3nkXpoJQCN41TDJVQlynhAH/KTzDk558czp3cRrMTFN0lylHMYEWibThqDnkOvdA9CBgQHKxJ932/uG+2Zy2Tp2OTRPGuTFG4NZXj0qhYMkiAy6C4Oz9hz6LLpzEFw+wIDAQAB";

	private static final byte[] SALT = new byte[] { 81, 89, 99, -78, 8, -2,
			-81, 44, 17, -64, -67, -2, -88, 97, -32, 13, 45, -20, 82, 41 };
	private LicenseChecker mChecker;

	// A handler on the UI thread.

	private LicenseCheckerCallback mLicenseCheckerCallback;

	private void doCheck() {

		mChecker.checkAccess(mLicenseCheckerCallback);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Try to use more data here. ANDROID_ID is a single point of attack.
		String deviceId = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);

		// Library calls this when it's done.
		mLicenseCheckerCallback = new MyLicenseCheckerCallback();
		// Construct the LicenseChecker with a policy.
		mChecker = new LicenseChecker(this, new ServerManagedPolicy(this,
				new AESObfuscator(SALT, getPackageName(), deviceId)),
				BASE64_PUBLIC_KEY);
		doCheck();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// We have only one dialog.
		return new AlertDialog.Builder(this)
				.setTitle("Application Not Licensed")
				.setCancelable(false)
				.setMessage(
						"This application is not licensed. Please purchase it from Android Market")
				.setPositiveButton("Buy App",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent marketIntent = new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("http://market.android.com/details?id="
												+ getPackageName()));
								startActivity(marketIntent);
								finish();
							}
						})
				.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).create();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mChecker.onDestroy();
	}

	private void startMainActivity() {
		startActivity(new Intent(this, YudanshaStudyActivity.class)); // REPLACE
																// MainActivity.class
																// WITH YOUR
																// APPS ORIGINAL
																// LAUNCH
																// ACTIVITY
		finish();
	}

	public void toast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

}