package android.example.caproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText Email;
    EditText Password;
    String name;
    Button submit;

    public static RequestQueue queue;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.enterEmail);
        Password = (EditText) findViewById(R.id.enterPassword);


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.queue == null) {
                    LoginActivity.queue = Volley.newRequestQueue(getApplicationContext());
                }


                String url = getResources().getString(R.string.url_api) + "/User/Login";

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Map loginResponse = LoginActivity.toMap(new JSONObject(response));
                                    if (loginResponse.get("status").toString().equals("success")) {
                                        Log.v("user", loginResponse.get("user").toString());

                                        Map userLogin = (HashMap)loginResponse.get("user");
                                        database = AppDatabase.getDatabase(getApplicationContext());

                                        String id = userLogin.get("User_ID").toString();
                                        String name = userLogin.get("FullName").toString();
                                        String email = userLogin.get("Email").toString();
                                        String username = userLogin.get("Username").toString();
                                        String password = userLogin.get("Password").toString();
                                        String userType = userLogin.get("User_Type").toString();
                                        String avatar = userLogin.get("Avatar").toString();
                                        String created = userLogin.get("DateCreated").toString();
                                        String lastlogin = userLogin.get("LastLogin").toString();
                                        String active = userLogin.get("Active").toString();

                                       User user = new User(Integer.parseInt(id), name, email, username, password, userType, avatar, created, lastlogin,Integer.parseInt(active));

                                             database.userDAO().addUser(user);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.v("error", loginResponse.get("message").toString());
                                        Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.v("Error", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response is", " Didnt work");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", Email.getText().toString());
                        params.put("password", Password.getText().toString());
                        return params;
                    }


                };
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.queue.add(stringRequest);
                    }
                }, 200);
            }

        });
    }




    public static Map<String, Object> toMap(JSONObject object) throws JSONException {

        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysIterator = object.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object objectValue = object.get(key);
            if (objectValue instanceof JSONArray) {
                objectValue = toList((JSONArray) objectValue);

            } else if (objectValue instanceof JSONObject) {
                objectValue = toMap((JSONObject) objectValue);
            }
            map.put(key, objectValue);
        }

        return map;
    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        try {
            for (int i = 0; i < array.length(); i++) {
                Object objectValue = array.get(i);
                if (objectValue instanceof JSONArray) {
                    objectValue = toList((JSONArray) objectValue);
                } else if (objectValue instanceof JSONObject) {
                    objectValue = toMap((JSONObject) objectValue);
                }
                list.add(objectValue);
            }
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
        return list;
    }


}