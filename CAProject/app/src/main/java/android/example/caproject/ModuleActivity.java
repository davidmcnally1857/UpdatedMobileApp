package android.example.caproject;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class ModuleActivity extends AppCompatActivity  {

    public static RequestQueue queue;
    private AppDatabase database;
    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        Intent intent = getIntent();
        final List<Object> modules;
        final Map subtopic = new HashMap();
        database = AppDatabase.getDatabase(getApplicationContext());
        module = database.moduleDAO().getModule();


        if (ModuleActivity.queue == null) {
            ModuleActivity.queue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = getResources().getString(R.string.url_api) + "/Module/GetModuleContent";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Map apiResponse = ModuleActivity.toMap(new JSONObject(response));
                             if(apiResponse.get("status").toString().equals("success")){
                                 List<HashMap> modules = (ArrayList)apiResponse.get("topics");



                                 database = AppDatabase.getDatabase(getApplicationContext());


                                 for (int i = 0; i < modules.size(); i++) {
                                     Map module = modules.get(i);

                                     String topicId = module.get("Topic_ID").toString();
                                     String topicName = module.get("Topic_Name").toString();
                                     String subTopic = module.get("SubTopic").toString();
                                     String parentTopic = module.get("ParentTopic").toString();
                                     String topicDescription = module.get("TopicDescription").toString();
                                     String moduleId = module.get("Module_ID").toString();
                                     List<HashMap> subTopics = (ArrayList) module.get("subTopics");
                                     for(int j = 0; j < subTopics.size(); j ++){
                                         Map subtopic = subTopics.get(j);
                                         String subTopicId = subtopic.get("Topic_ID").toString();
                                         String subTopicName = subtopic.get("Topic_Name").toString();
                                         String subTopic1 = subtopic.get("SubTopic").toString();
                                         String parentTopic1 = subtopic.get("ParentTopic").toString();
                                         String topicDescription1 = subtopic.get("TopicDescription").toString();
                                         String moduleId1 = subtopic.get("Module_ID").toString();

                                         Topics sTopic = new Topics(Integer.parseInt(subTopicId), subTopicName, Integer.parseInt(subTopic1), Integer.parseInt(parentTopic1), topicDescription1,
                                                 Integer.parseInt(moduleId1));
                                         if(subTopic == "null"){
                                             subTopic = "0";
                                         }
                                         database.topicDAO().addTopic(sTopic);
                                     }
                                     if(parentTopic == "null"){
                                         parentTopic = "0";
                                     }


                                     Topics topic = new Topics(Integer.parseInt(topicId), topicName, Integer.parseInt(subTopic), Integer.parseInt(parentTopic), topicDescription,
                                             Integer.parseInt(moduleId));

                                     database.topicDAO().addTopic(topic);
                                 }
                                 List<Topics> topics = database.topicDAO().getAllTopicsWherParentTopicsIsZero();
                                 List<Topics> subTopics = database.topicDAO().getAllTopicsWhereParentTopisIsNotZero();

                                 RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_view);
                                 RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                 recyclerView.setLayoutManager(layoutManager);
                                 RecyclerView.Adapter mAdapter = new AdapterModuleContent(topics, subTopics);
                                 recyclerView.setAdapter(mAdapter);

                             } else {
                                 Log.v("error", apiResponse.get("message").toString());
                             }

                        }
                        catch(Exception e){
                          Log.v("Error", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.v("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Module_ID", Integer.toString(module.Module_ID));
                params.put("ForApp", "true");
                return params;
            }
        };




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               ModuleActivity.queue.add(stringRequest);
            }
        },200);

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

