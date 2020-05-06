package com.gentech.beanvending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends AppCompatActivity {

    final static String tag = "APITag";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameKey = "username";
    private static String usernameValue = "";
    private String username;
    private Context context;
    private TextView tvUsername;

    private TextView tvBeanType1Num, tvBeanType2Num;

    private RequestQueue mRequestQueue;
    private Button btOrder;
    private RadioGroup rdSelectType;
    private RadioButton rdSelectType1, rdSelectType2;

    String beanType1Value = "";
    String beanNum1Value = "";
    String beanType1Name = "";
    String beanType2Value = "";
    String beanNum2Value = "";
    String beanType2Name = "";

    private int selectType = 1;



    Handler handler = new Handler();

    private static final int rdType1ID = 1;//second radio button id
    private static final int rdType2ID = 2;//third radio button id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        context = getApplicationContext();
        username = loadPreferences(usernameKey);
        mRequestQueue = Volley.newRequestQueue(this);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText(username);

        tvBeanType1Num = (TextView) findViewById(R.id.tvBeanType1Num);
        tvBeanType2Num = (TextView) findViewById(R.id.tvBeanType2Num);

        btOrder = (Button) findViewById(R.id.btOrder);
        rdSelectType1 = (RadioButton) findViewById(R.id.rdSelectType1);
        rdSelectType2 = (RadioButton) findViewById(R.id.rdSelectType2);

        rdSelectType = (RadioGroup) findViewById(R.id.rdSelectType);


        btOrder.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i(tag, "selectType = "+selectType);
            }
        });

        rdSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdSelectType1:
                        // do operations specific to this selection
                        selectType = 1;
                        break;
                    case R.id.rdSelectType2:
                        selectType = 2;
                        break;
                    default:
                        selectType = -1;
                        break;
                }
            }
        });

        handler.post(runnableCode);
    }


    public String loadPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, "") ;
    }

    public void monitor() {
        String url = "http://35.240.143.70:5000/monitor";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(tag, response.toString());
                        try{
                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject bean = jsonArray.getJSONObject(i);
                                String beanType = bean.getString("bean_type");
                                String beanNum = bean.getString("bean_num");
                                String beanName = bean.getString("bean_name");
                                if(beanType.equals("1")){
                                    beanType1Value = beanType;
                                    beanNum1Value = beanNum;
                                    beanType1Name = beanName;
                                }else if(beanType.equals("2")){
                                    beanType2Value = beanType;
                                    beanNum2Value = beanNum;
                                    beanType2Name = beanName;
                                }
                                Log.i(tag, "beans["+i+"] => bean_type: "+beanType+", bean_num: "+beanNum+", bean_name: "+ beanName);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        mRequestQueue.add(request);
    }

    private void updateUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tvBeanType1Name.setText(beanType1Name);
//                tvBeanType2Name.setText(beanType2Name);
                tvBeanType1Num.setText(beanNum1Value);
                tvBeanType2Num.setText(beanNum2Value);
            }
        });
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            monitor();
            updateUi();
            handler.postDelayed(this, 1000);
        }
    };

    public void makeOrder(String username, String beanType, String beanNum){
        String url = "http://35.240.143.70:5000/makeOrder/"+username+"/"+beanType+"/"+beanNum;
        Log.i(tag, "/makeOrder url: "+url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(tag, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        mRequestQueue.add(request);

    }


}
