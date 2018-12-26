package com.developer.sarthak.businessportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PurchaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner item;
    LinearLayout llt;
    View child;
    int sum=0;
    EditText pur_amt;
    Button add;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        String items[]={"1","2","3","4","5","6","7","8","9","10"};

        item = (Spinner)findViewById(R.id.pur_item);
        llt = (LinearLayout)findViewById(R.id.pur_llt);
        item.setOnItemSelectedListener(this);
        pur_amt=(EditText)findViewById(R.id.pur_amt);
        add=(Button)findViewById(R.id.pur_add);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase db=openOrCreateDatabase("businessportal",MODE_PRIVATE,null);
                    db.execSQL("create table if not exists add_busi(type varchar, date varchar, amount varchar)");
                    db.execSQL("insert into add_busi values('purchase','"+formattedDate+"','"+pur_amt.getText().toString()+"')");
                    db.close();
                    Toast.makeText(PurchaseActivity.this, "Details Added", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(PurchaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        item.setAdapter(aa);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
        llt.removeAllViews();
        for (int i=0;i<j+1;i++){
            child = getLayoutInflater().inflate(R.layout.pur_items,null);
            child.setId(i);
            EditText qty = (EditText)child.findViewById(R.id.pur_proqty);
            qty.setId(i);
            EditText prc = (EditText)child.findViewById(R.id.pur_proprc);
            prc.setId(i);
            EditText amt = (EditText)child.findViewById(R.id.pur_amt);
            amt.setId(i);
            calcAmount(qty,prc,amt,j);
            llt.addView(child);
        }

    }

    @SuppressLint("ResourceType")
    public void calcAmount(final EditText qty, final EditText prc, final EditText amt, int j) {
        for (int i=0;i<j+1;i++) {
            if (qty.getId() == i && prc.getId() == i && amt.getId() == i) {
                TextWatcher tw = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int quant = Integer.parseInt(qty.getText().toString());
                        int price = Integer.parseInt(prc.getText().toString());
                        int amount = quant * price;
                        amt.setText(String.valueOf(amount));
                        sum=sum+amount;
                        pur_amt.setText(String.valueOf(sum));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };
                prc.addTextChangedListener(tw);

            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
