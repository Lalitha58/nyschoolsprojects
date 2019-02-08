package yuva.com.a20190208_ym_nycschools;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //declare the listview
    ListView school_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intilize the listview
        school_list = (ListView) findViewById(R.id.school_list);
        //calling the schoolTask and send the url
        new SchoolTask().execute("https://data.cityofnewyork.us/resource/97mf-9njv.json");
    }

    //create the Background class
    public class SchoolTask extends AsyncTask<String, String, List<SchoolModel>> {
        String data = " ";
        @Override
        protected void onPreExecute() {
            //dialog.setMessage("Loading , Please wait....");
        }
        //Create the ArrayList which can hold the Json objects from url
        List<SchoolModel> schoolModelList = new ArrayList<>();
        @Override
        protected List<SchoolModel> doInBackground(String... params) {
            try {
                URL urlofurl = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) urlofurl.openConnection();
                InputStream in = con.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while (line != null) {
                    line = bf.readLine();
                    data = data + line;
                }
                //create the Json Array
                JSONArray jsonArray = new JSONArray(data);
                //Looping the JsonArray
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    SchoolModel schoolModel = new SchoolModel();
                    //Declare the empty Strings for if statement
                    String schoolName = "";
                    String schoolOverview="";
                    //if particular Json object avaolble or not checking in if statement
                    if(!jsonObject.isNull("school_name")) {
                        schoolName = jsonObject.getString("school_name");
                        schoolModel.setSchoolName(schoolName);
                    }else {
                        schoolModel.setSchoolName(schoolName);
                    }
                    if(!jsonObject.isNull("overview_paragraph")) {
                        schoolOverview = jsonObject.getString("overview_paragraph");
                        schoolModel.setSchoolOverview(schoolOverview);
                    }else{
                        schoolModel.setSchoolOverview(schoolOverview);
                    }
                    schoolModelList.add(schoolModel);

                }

                // Sort the school names in ascending order
                Collections.sort(schoolModelList, new SchoolListComparator());

                return schoolModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(final List<SchoolModel> result) {

            //Declare and intilize the Adapter and call the SchoolAdapter constuctor
            SchoolAdapter schoolAdapter = new SchoolAdapter(getApplicationContext(), R.layout.school_name_list_item, result);
            school_list.setAdapter(schoolAdapter);
            super.onPostExecute(result);
            school_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SchoolModel sendSchoolModel = result.get(position); // getting the model

                    //calling schoolDetailsActivity using intent
                    Intent intent = new Intent(MainActivity.this, SchoolDetailsActivity.class);
                    //sending JsonObjects from current activity to schooldetailsactivity using parcelable
                    // converting model json into string type and sending it via intent
                    intent.putExtra("sendSchoolModel", (Parcelable) sendSchoolModel);
                    startActivity(intent);
                }
            });
        }



    }
    //create SchoolAdapter class for Adpter
    public class SchoolAdapter extends ArrayAdapter {
        int resource;
        List<SchoolModel> schoolList;
        LayoutInflater layoutInflater;
        Context con;
        public SchoolAdapter(Context context, int resource, List<SchoolModel> objects) {
            super(context, resource, objects);
            this.schoolList = objects;
            this.resource = resource;
            //call the method getSystemService
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(resource, null);
            }
            TextView school_name_view;
            school_name_view = (TextView) convertView.findViewById(R.id.school_name_view);
            //set  the schoolname
            school_name_view.setText(schoolList.get(position).getSchoolName());
            return convertView;
        }


    }
    // Class to sort the school names in ascending order
    public class SchoolListComparator implements Comparator<SchoolModel>
    {
        public int compare(SchoolModel left, SchoolModel right) {
            return left.schoolName.compareTo(right.schoolName);
        }
    }
}


