package com.developer.sarthak.businessportal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class ChooserPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView nav;
    FirebaseAuth auth;
    String personName,personEmail,personId;
    Uri personPhoto;
    GoogleApiClient mGoogleApiClient;
    Button nav_menu;
    DrawerLayout mDrawer;
    FloatingActionButton add,purchase,sales,expenses,report,payment;
    Animation fabopen,fabclose,fabclock,fabanti;
    TextView pur,sal,exp,pay;
    Boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser_page);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        auth=FirebaseAuth.getInstance();
        mDrawer=(DrawerLayout)findViewById(R.id.drawerlayout);
        nav_menu=(Button)findViewById(R.id.cp_nav_menu);
        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });

        //Floating Action Buttons
        add=(FloatingActionButton)findViewById(R.id.plus);
        purchase=(FloatingActionButton)findViewById(R.id.purchase);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserPage.this,PurchaseActivity.class));
            }
        });
        sales=(FloatingActionButton)findViewById(R.id.sales);
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserPage.this,SalesActivity.class));
            }
        });
        expenses=(FloatingActionButton)findViewById(R.id.expenses);
        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserPage.this,ExpenseActivity.class));
            }
        });
        payment=(FloatingActionButton)findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserPage.this,PaymentActivity.class));
            }
        });
        fabopen= AnimationUtils.loadAnimation(ChooserPage.this,R.anim.fab_anim);
        fabclose= AnimationUtils.loadAnimation(ChooserPage.this,R.anim.fab_anim_close);
        fabclock= AnimationUtils.loadAnimation(ChooserPage.this,R.anim.rotate_clock);
        fabanti= AnimationUtils.loadAnimation(ChooserPage.this,R.anim.rotate_anti);
        report=(FloatingActionButton)findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserPage.this,MainPage.class));
            }
        });
        pur=(TextView)findViewById(R.id.tv_purchase);
        sal=(TextView)findViewById(R.id.tv_sales);
        exp=(TextView)findViewById(R.id.tv_expenses);
        pay=(TextView)findViewById(R.id.tv_payment);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    add.startAnimation(fabanti);
                    purchase.startAnimation(fabclose);
                    sales.startAnimation(fabclose);
                    expenses.startAnimation(fabclose);
                    payment.startAnimation(fabclose);
                    pur.startAnimation(fabclose);
                    sal.startAnimation(fabclose);
                    exp.startAnimation(fabclose);
                    pay.startAnimation(fabclose);
                    purchase.setClickable(false);
                    sales.setClickable(false);
                    expenses.setClickable(false);
                    isOpen=false;
                }
                else {
                    add.startAnimation(fabclock);
                    purchase.startAnimation(fabopen);
                    sales.startAnimation(fabopen);
                    expenses.startAnimation(fabopen);
                    payment.startAnimation(fabopen);
                    pur.startAnimation(fabopen);
                    sal.startAnimation(fabopen);
                    exp.startAnimation(fabopen);
                    pay.startAnimation(fabopen);
                    purchase.setClickable(true);
                    sales.setClickable(true);
                    expenses.setClickable(true);
                    isOpen=true;
                }
            }
        });

        //Google Data Intake
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ChooserPage.this);
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
        Picasso.with(ChooserPage.this)
                .load(personPhoto).placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(img);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.nav_pur){
            startActivity(new Intent(ChooserPage.this,PurchaseActivity.class));
        }
        if (item.getItemId()==R.id.nav_sal){
            startActivity(new Intent(ChooserPage.this,SalesActivity.class));
        }
        if (item.getItemId()==R.id.nav_pay){
            startActivity(new Intent(ChooserPage.this,PaymentActivity.class));
        }
        if (item.getItemId()==R.id.nav_exp){
            startActivity(new Intent(ChooserPage.this,ExpenseActivity.class));
        }
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
            startActivity(new Intent(ChooserPage.this,Login.class));
            Toast.makeText(ChooserPage.this,"Signed Out from the Account",Toast.LENGTH_SHORT);
        }
        if(item.getItemId()==R.id.delete){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(ChooserPage.this,Login.class));
                                    Toast.makeText(ChooserPage.this, "Your profile is deleted :( Create a account now!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChooserPage.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            startActivity(new Intent(ChooserPage.this,Login.class));
                            Toast.makeText(ChooserPage.this,"Revoked Access from your Google Account :( Do come back soon.",Toast.LENGTH_SHORT);
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
}
