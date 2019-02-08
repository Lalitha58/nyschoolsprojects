package yuva.com.a20190208_ym_nycschools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SchoolDetailsActivity extends AppCompatActivity {
    //declare the views
    TextView school_name;
    TextView school_overview;
    Button sat_button;
    String schoolNameForSatScoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_details_activity);
        school_name=(TextView)findViewById(R.id.school_name);
        school_overview=(TextView)findViewById(R.id.school_overview);
        sat_button =(Button)findViewById(R.id.sat_button);
        //get the bundle data
        Bundle bundle = getIntent().getExtras();
        //to check not null the bundle
        if (bundle != null) {
            Bundle data = getIntent().getExtras();
            SchoolModel schoolModel = (SchoolModel) data.getParcelable("sendSchoolModel");
            schoolNameForSatScoreView=schoolModel.getSchoolName();
            school_name.setText(schoolModel.getSchoolName());
            school_overview.setText(schoolModel.getSchoolOverview());
        }
        //click action to the Sat score button
        sat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //from current activity Satscore activity calling by using intent
                Intent i = new Intent(SchoolDetailsActivity.this,SatScoreActivity.class);
                i.putExtra("schoolname", schoolNameForSatScoreView);
                startActivity(i);
            }
        });
    }
}
