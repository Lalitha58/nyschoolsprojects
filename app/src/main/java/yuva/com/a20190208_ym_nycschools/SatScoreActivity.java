package yuva.com.a20190208_ym_nycschools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class SatScoreActivity extends AppCompatActivity {
    //Declare the Listview
    ListView sat_score_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sat_score_activity);
        //Intilize the Listview
        sat_score_list = (ListView) findViewById(R.id.sat_score_list);
        //Call the SatScoreTask class pass the Url in execute method
        new SatScoreTask().execute("https://data.cityofnewyork.us/resource/734v-jeq5.json");
    }
    //Create class SatScoreTask extends the AsyTask class
    public class SatScoreTask extends AsyncTask<String, String, List<SatScoreModel>> {
        String data = " ";
        String i=getIntent().getStringExtra("schoolname");
        @Override
        protected void onPreExecute() {
        }
        //Create ArrayList which holds the Json objects
        List<SatScoreModel> satModelList = new ArrayList<>();
        @Override
        //doInBackground Method write the connection and Json responses
        protected List<SatScoreModel> doInBackground(String... params) {
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
                String satScoreSchoolName =getIntent().getStringExtra("schoolname");
                JSONArray jsonArray = new JSONArray(data);


                //Intilize the variable to check the no match found for the school
                String matchFound = "N";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    SatScoreModel satScoreModel = new SatScoreModel();
                    String reading_avg_score="";
                    String math_avg_score="";
                    String writing_avg_score="";
                    String schoolNameFromSatSVC = "";
                    if (!jsonObject.isNull("school_name")) {

                        schoolNameFromSatSVC = jsonObject.getString("school_name");

                    } else {


                        schoolNameFromSatSVC = "";

                    }

                    Log.i("Scholl name: ", schoolNameFromSatSVC );
                    Log.i("school from Intent", satScoreSchoolName);
                    if(schoolNameFromSatSVC.equalsIgnoreCase(satScoreSchoolName)){

                        //set value to Y if the matcgfound the selected school
                        matchFound = "Y";
                        satScoreModel.setNoSatScoreAvailble("");

                        // Get the SAT scores from the JSON for the selected school
                        if (!jsonObject.isNull("sat_critical_reading_avg_score")) {
                            reading_avg_score = jsonObject.getString("sat_critical_reading_avg_score");
                            satScoreModel.setReading_avg_score(reading_avg_score);
                        } else {
                            satScoreModel.setReading_avg_score(reading_avg_score);
                        }
                        if (!jsonObject.isNull("sat_math_avg_score")) {

                            math_avg_score = jsonObject.getString("sat_math_avg_score");
                            satScoreModel.setMath_avg_score(math_avg_score);
                        } else {

                            satScoreModel.setMath_avg_score(math_avg_score);
                        }

                        if (!jsonObject.isNull("sat_writing_avg_score")) {

                            writing_avg_score = jsonObject.getString("sat_writing_avg_score");
                            satScoreModel.setWriting_avg_score(writing_avg_score);
                        } else {

                            satScoreModel.setWriting_avg_score(writing_avg_score);
                        }

                        satModelList.add(satScoreModel);

                    }







                }


                // Set the message if no match found for the selected school
                if(matchFound.equalsIgnoreCase("N")){

                    SatScoreModel satScoreModel = new SatScoreModel();
                    satScoreModel.setNoSatScoreAvailble("SAT scores not found for this school");
                    satScoreModel.setWriting_avg_score("");
                    satScoreModel.setMath_avg_score("");
                    satScoreModel.setReading_avg_score("");
                    satModelList.add(satScoreModel);
                }

                return satModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // set the SAT scores to the adapter
        @Override
        protected void onPostExecute(final List<SatScoreModel> result) {

            SatScoreAdapter satScoreAdapter = new SatScoreAdapter(getApplicationContext(), R.layout.sat_score_list_view, result);

            sat_score_list.setAdapter(satScoreAdapter);

            super.onPostExecute(result);

        }
    }

    // adapter class to set the SAT score values to the views.
    public class SatScoreAdapter extends ArrayAdapter {
        int resource;
        List<SatScoreModel> scoreList;
        LayoutInflater layoutInflater;
        Context con;

        public SatScoreAdapter(Context context, int resource, List<SatScoreModel> objects) {
            super(context, resource, objects);
            this.scoreList = objects;
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


            // set scores to views if match found
            TextView reading_avg_score_tv,math_avg_score_tv,writing_avg_score_tv,sat_school_name,no_sat_score_view;
            no_sat_score_view = (TextView) convertView.findViewById(R.id.no_sat_score_view);
            reading_avg_score_tv = (TextView) convertView.findViewById(R.id.reading_avg_score_tv);
            math_avg_score_tv = (TextView) convertView.findViewById(R.id.math_avg_score_tv);
            writing_avg_score_tv = (TextView) convertView.findViewById(R.id.writing_avg_score_tv);
            sat_school_name = (TextView) convertView.findViewById(R.id.sat_school_name);
            reading_avg_score_tv.setText("Reading Average Score: " + scoreList.get(position).getReading_avg_score());
            math_avg_score_tv.setText("Math Average Score is: " +scoreList.get(position).getMath_avg_score());
            writing_avg_score_tv.setText("Writing Average Score is: " +scoreList.get(position).getWriting_avg_score());
            String satScoreSchoolName1=getIntent().getStringExtra("schoolname");
            sat_school_name.setText(satScoreSchoolName1);


            //set message if no match found for the selected school
            if(!((scoreList.get(position).getNoSatScoreAvailble()).equalsIgnoreCase(""))){

                reading_avg_score_tv.setText("");
                math_avg_score_tv.setText("");
                writing_avg_score_tv.setText("");
                no_sat_score_view.setText(scoreList.get(position).getNoSatScoreAvailble());


            }

            return convertView;
        }

    }
}
