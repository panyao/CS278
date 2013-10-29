
package org.magnum.soda.example.maint;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.magnum.soda.android.AndroidSoda;
import org.magnum.soda.android.AndroidSodaConnectionException;
import org.magnum.soda.android.AndroidSodaListener;
import org.magnum.soda.android.SodaInvokeInUi;
import org.magnum.soda.example.controllers.homeactivity.HomeActivity;
import org.magnum.soda.example.object.MaintenanceListener;
import org.magnum.soda.example.object.MaintenanceReport;
import org.magnum.soda.example.object.MaintenanceReports;
import org.magnum.soda.example.object.User;
import org.magnum.soda.example.object.UserListener;
import org.magnum.soda.example.slidingmenu.CreateReportFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportEditorActivity extends Activity implements
		AndroidSodaListener {

	private final String TAG = "ReportEditorActivity";
	private EditText reportTitle_;
	private EditText reportContent_;
	private TextView creatorText_;
	private TextView createtimeText_;
	private Button saveButton_;
	private Button deleteButton_;
	private Button bindLocationButton_;
	private Button bindQRButton_;
	private Button followButton_;
	private ImageButton photoView_;

	private String username;
	private MaintenanceReports reports_;
	private MaintenanceReport currReport_;
	private ReportParcelable current;
	private static final int STATIC_INTEGER_VALUE = 10;

	private String mContent = null;
	private byte[] mImageData = null;
	private String creator = null;

	private AndroidSoda as;
	private AndroidSodaListener asl_;
	private Context ctx_ = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		asl_ = this;
		setContentView(R.layout.report_form);
		
		AndroidSoda.init(ctx_, LoginActivity.mHost, 8081, asl_);
		
		reportTitle_ = (EditText) findViewById(R.id.title_text_editreport);
		reportContent_ = (EditText) findViewById(R.id.reportEditText_editreport);
		saveButton_ = (Button) findViewById(R.id.saveButton_reportedit);
		deleteButton_ = (Button) findViewById(R.id.DeleteButton);
		bindLocationButton_ = (Button) findViewById(R.id.bindLocationButton);
		bindQRButton_ = (Button) findViewById(R.id.BindQR);
		followButton_ = (Button) findViewById(R.id.Button_follow);
		creatorText_ = (TextView) findViewById(R.id.textView_creatorID);
		createtimeText_ = (TextView) findViewById(R.id.textView_createTime);
		photoView_ = (ImageButton) findViewById(R.id.image_editreport);

		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE);
		username = sharedPref.getString("username", "no");

		if (getIntent().hasExtra("mReport")) {
			current = getIntent().getExtras().getParcelable("mReport");
			currReport_ = current.getReport();
			reportTitle_.setText(currReport_.getTitle());
			reportContent_.setText(currReport_.getContents());
			creator = currReport_.getCreatorId();
			creatorText_.setText(creator);

			Date createtime = currReport_.getCreateTime_();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
			createtimeText_.setText(sdf.format(createtime));

			byte[] b = current.getReport().getImageData();

			if (b != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				photoView_.setImageBitmap(bitmap);
				photoView_.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				photoView_.setAdjustViewBounds(true);

			}
		}

		photoView_.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				callIntent(getBmp(photoView_.getDrawable()));

			}

		});

		saveButton_.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mContent = reportContent_.getText().toString();
				if (mContent != null) {
					currReport_.setContents(mContent);

					if (photoView_.getDrawable() != null) {
						mImageData = getBytes(getBmp(photoView_.getDrawable()));
						currReport_.setImageData(mImageData);
					}
					updateReport();

				}
			}
		});

		followButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				follow();
			}
		});

		deleteButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteReport();
				Intent homeActivityIntent = new Intent(ctx_, HomeActivity.class);
				startActivity(homeActivityIntent);	
			}
		});

	}
	
	private void updateReport() {
		AndroidSoda.async(new Runnable() {

			@Override
			public void run() {
				reports_ = as.get(MaintenanceReports.class,
						MaintenanceReports.SVC_NAME);
				reports_.modifyReport(currReport_);
				
				statusMsg("Report updated:" + currReport_.getContents());
	
			}
		});

	}

	private void deleteReport() {
		AndroidSoda.async(new Runnable() {
			@Override
			public void run() {
				reports_ = as.get(MaintenanceReports.class,
						MaintenanceReports.SVC_NAME);
				reports_.deleteReport(currReport_.getId());
				
				statusMsg("Report deleted:" + currReport_.getContents());

			}
		});
	}
	
	private void follow(){
		AndroidSoda.async(new Runnable() {
			@Override
			public void run() {
				reports_ = as.get(MaintenanceReports.class,
						MaintenanceReports.SVC_NAME);
				reports_.addFollowerListener(currReport_.getId(),
						new UserListener() {

							@Override
							public void userAdded(User u) {

							}

							@Override
							public void notifyFollowers(
									MaintenanceReport u) {
								Log.d(TAG,"-------notify followers---: "+ u.getContents());
							}

						});

				reports_.addFollower(currReport_, username);
				
				statusMsg(username + " is now following this report.");
				
			}

		});		
	}
	
	private void statusMsg(final String msg){
		as.inUi(new Runnable() {                
            @Override
            public void run() {
            	Toast.makeText(ctx_, msg,
						Toast.LENGTH_SHORT).show(); 
            }
		});
		
	}
	
	private Bitmap getBmp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			return bitmap;
		}
		return null;
	}

	private void callIntent(Bitmap img) {
		// Intent i = new Intent(this,
		// leadtools.annotationsdemo.AnnotationsDemoActivity.class);
		// i.putExtra("byteArray", getBytes(img));
		// startActivityForResult(i, STATIC_INTEGER_VALUE);
	}

	private byte[] getBytes(Bitmap bmp) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 10 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();
		return bitmapdata;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case STATIC_INTEGER_VALUE: {
			if (resultCode == Activity.RESULT_OK) {
				byte[] b = data.getByteArrayExtra("result");
				Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
				photoView_.setImageBitmap(bitmap);
				photoView_.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				photoView_.setAdjustViewBounds(true);

			}
			break;
		}
		}
	}


	public void setImage() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				byte[] b = currReport_.getImageData();
				if (b != null) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
							b.length);
					photoView_.setImageBitmap(bitmap);
					photoView_.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					photoView_.setAdjustViewBounds(true);

				}
			}
		});
	}

	@Override
	public void connected(final AndroidSoda s) {
		this.as = s;
	}

	@Override
	public void connectionFailure(AndroidSoda s,
			AndroidSodaConnectionException ex) {
		Toast.makeText(this, "Unable to connect to server.", Toast.LENGTH_LONG);
	}

}
