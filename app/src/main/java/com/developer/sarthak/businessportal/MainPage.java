package com.developer.sarthak.businessportal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener{
NavigationView nav;
FirebaseAuth auth;
String personName,personEmail,personId;
Uri personPhoto;
DrawerLayout mDrawer;
GoogleApiClient mGoogleApiClient;
Button nav_menu;
static int sum_pur=0;
    static int sum_sal=0;
    static int sum_exp=0;
    static int sum_pay=0;
    static int profit=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        mDrawer=(DrawerLayout)findViewById(R.id.report_drawer);
        nav_menu=(Button)findViewById(R.id.nav_menu);
        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        auth=FirebaseAuth.getInstance();


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
//purchase
        String str="select amount from add_busi where type='purchase' and date= '"+formattedDate+"'";
        SQLiteDatabase db=openOrCreateDatabase("businessportal", MODE_PRIVATE,null);
        Cursor cursor=db.rawQuery(str,null);
        if(cursor.moveToFirst())
        {
            do{
                sum_pur=sum_pur+Integer.parseInt(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        else
        {
            Toast.makeText(MainPage.this, "Record not found", Toast.LENGTH_SHORT).show();
        }
//sales
        String str_sales="select amount from add_busi where type='sales' and date= '"+formattedDate+"'";
        SQLiteDatabase db_sales=openOrCreateDatabase("businessportal", MODE_PRIVATE,null);
        Cursor cursor1=db_sales.rawQuery(str_sales,null);
        if(cursor1.moveToFirst())
        {
            do{
                sum_sal=sum_sal+Integer.parseInt(cursor1.getString(0));
            }while (cursor1.moveToNext());
        }
        else
        {
            Toast.makeText(MainPage.this, "Record not found", Toast.LENGTH_SHORT).show();
        }
//expenses
        String str_exp="select amount from add_busi where type='expense' and date= '"+formattedDate+"'";
        SQLiteDatabase db_exp=openOrCreateDatabase("businessportal", MODE_PRIVATE,null);
        Cursor cursor2=db_exp.rawQuery(str_exp,null);
        if(cursor2.moveToFirst())
        {
            do{
                sum_exp=sum_exp+Integer.parseInt(cursor2.getString(0));
            }while (cursor2.moveToNext());
        }
        else
        {
            Toast.makeText(MainPage.this, "Record not found", Toast.LENGTH_SHORT).show();
        }
//payment
        String str_pay="select amount from add_busi where type='payment' and date= '"+formattedDate+"'";
        SQLiteDatabase db_pay=openOrCreateDatabase("businessportal", MODE_PRIVATE,null);
        Cursor cursor3=db_pay.rawQuery(str_pay,null);
        if(cursor3.moveToFirst())
        {
            do{
                sum_pay=sum_pay+Integer.parseInt(cursor3.getString(0));
            }while (cursor3.moveToNext());
        }
        else
        {
            Toast.makeText(MainPage.this, "Record not found", Toast.LENGTH_SHORT).show();
        }

        profit = sum_pay-(sum_pur+sum_exp);

        //Tab Layout
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Profit/Loss"));
        tabLayout.addTab(tabLayout.newTab().setText("Turnover"));
        tabLayout.addTab(tabLayout.newTab().setText("Revenue"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //View Pager
        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Google Data Intake
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainPage.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }
        TextView txtProfileName = (TextView) nav.getHeaderView(0).findViewById(R.id.user_name);
        txtProfileName.setText(personName);
        TextView txtProfileEmail = (TextView) nav.getHeaderView(0).findViewById(R.id.email);
        txtProfileEmail.setText(personEmail);
        ImageView img=(ImageView)nav.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.with(MainPage.this)
                .load(personPhoto).placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(img);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            auth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),Login.class);
                            startActivity(i);
                        }
                    });
            startActivity(new Intent(MainPage.this,Login.class));
            Toast.makeText(MainPage.this,"Signed Out from the Account",Toast.LENGTH_SHORT);
        }
        if(item.getItemId()==R.id.delete){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(MainPage.this,Login.class));
                                    Toast.makeText(MainPage.this, "Your profile is deleted :( Create a account now!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainPage.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            startActivity(new Intent(MainPage.this,Login.class));
                            Toast.makeText(MainPage.this,"Revoked Access from your Google Account :( Do come back soon.",Toast.LENGTH_SHORT);
                        }
                    });
        }
        return false;
    }
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
