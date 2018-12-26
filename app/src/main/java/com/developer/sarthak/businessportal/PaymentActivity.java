package com.developer.sarthak.businessportal;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
EditText paymoney,totalmoney;
Button add;
String formattedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymoney = (EditText)findViewById(R.id.pay_money);
        totalmoney = (EditText)findViewById(R.id.pay_amt);
        add=(Button)findViewById(R.id.pay_add);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase db=openOrCreateDatabase("businessportal",MODE_PRIVATE,null);
                    db.execSQL("create table if not exists add_busi(type varchar, date varchar, amount varchar)");
                    db.execSQL("insert into add_busi values('payment','"+formattedDate+"','"+totalmoney.getText().toString()+"')");
                    db.close();
                    Toast.makeText(PaymentActivity.this, "Details Added", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextWatcher tw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                totalmoney.setText(paymoney.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        paymoney.addTextChangedListener(tw);
    }
}
