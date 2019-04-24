package android.example.caproject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterModule.ItemClickListener {


    InternetReciever internetReciever = new InternetReciever();
    private String userId;
    TextView name;
    public static Context applicationContext;
    public static RequestQueue queue;
    private AppDatabase database;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView) findViewById(R.id.name);
        Intent intent = getIntent();


        database = AppDatabase.getDatabase((getApplicationContext()));
        user = database.userDAO().getUser();
        name.setText(user.FullName.toString());

        applicationContext = getApplicationContext();



        if (MainActivity.queue == null) {
            MainActivity.queue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = getResources().getString(R.string.url_api) + "/Module/GetModulesForUser";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            Map responseApi = MainActivity.toMap(new JSONObject(response));
                            if (responseApi.get("status").toString().equals("success")) {
                                List<HashMap> moduleList = (ArrayList) responseApi.get("modules");


                                database = AppDatabase.getDatabase(getApplicationContext());


                                for (int i = 0; i < moduleList.size(); i++) {
                                    Map module = moduleList.get(i);

                                    String moduleId = module.get("Module_ID").toString();
                                    String moduleCode = module.get("Module_Code").toString();
                                    String moduleName = module.get("Module_Name").toString();
                                    String course = module.get("Course").toString();
                                    String courseIntake = module.get("Course_Intake").toString();
                                    String lecturer = module.get("Lecturer").toString();
                                    String startDate = module.get("StartDate").toString();
                                    String endDate = module.get("EndDate").toString();
                                    String start = module.get("From").toString();
                                    String end = module.get("To").toString();

                                    Module moduleItem = new Module(Integer.parseInt(moduleId), moduleCode, moduleName,
                                            course, courseIntake, lecturer, startDate, endDate, start, end);
                                    database.moduleDAO().addModule(moduleItem);
                                }


                            } else {
                                Log.v("error", responseApi.get("modules").toString());
                            }




                        } catch (Exception e) {
                            Log.v("error", e.getMessage());

                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Response", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User_ID", Integer.toString(user.User_ID));
                params.put("ForApp", "true");
                return params;
            }

        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.queue.add(stringRequest);
            }
        }, 200);


            List<Module> modules = (ArrayList) database.moduleDAO().getAllModules();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            AdapterModule mAdapter = new AdapterModule(modules);
            recyclerView.setAdapter(mAdapter);
            ((AdapterModule) mAdapter).setmItemClickListener(MainActivity.this);
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        database = AppDatabase.getDatabase(getApplicationContext());
        if(!(database.userDAO().getAllUsers().isEmpty())) {
            database.userDAO().removeAllUsers();
            database.moduleDAO().removeAllModules();
        }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            },1000);








        return true;
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




    @Override
    public void onListClick(int position) {

        Intent intent = new Intent(MainActivity.this, ModuleActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReciever, filter);

    }

    protected void onStop() {
        super.onStop();
    }

}


