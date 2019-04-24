package android.example.caproject;

import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterModuleContent extends RecyclerView.Adapter<AdapterModuleContent.Viewholder> {

    private List<Topics> modules;
    private List<Topics> subdata;



    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView topic_name;
        public TextView subtopic_name;
        public TextView description;
        public TextView subtopic_description;
        public View layout;


        public Viewholder(View v) {
            super(v);
            layout = v;
            topic_name = (TextView) v.findViewById(R.id.topic_name);
            description = (TextView) v.findViewById(R.id.description_name);
            subtopic_name = (TextView) v.findViewById(R.id.subtopic_name);
            subtopic_description = (TextView) v.findViewById(R.id.subtopic_description);


        }
    }

        @Override
        public AdapterModuleContent.Viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View moduleView = inflater.inflate(R.layout.details_module, viewGroup, false);
            Viewholder module = new Viewholder(moduleView);
            return module;
    }

        public AdapterModuleContent(List<Topics> dataset, List<Topics> subdataset) {
        modules = dataset;
        subdata = subdataset;


        }

        public void onBindViewHolder(AdapterModuleContent.Viewholder viewholder, int i) {

        Topics topic = modules.get(i);
        Topics subtopic = subdata.get(i);
        viewholder.topic_name.setText(topic.Topic_Name);
        viewholder.description.setText(topic.Topic_Description);
        viewholder.subtopic_name.setText(subtopic.Topic_Name);
        viewholder.subtopic_description.setText(subtopic.Topic_Description);



        }



        @Override
        public int getItemCount() {
            return modules.size();

        }
    }



