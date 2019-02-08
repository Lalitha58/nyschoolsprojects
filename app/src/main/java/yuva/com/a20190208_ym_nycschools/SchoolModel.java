package yuva.com.a20190208_ym_nycschools;

import android.os.Parcel;
import android.os.Parcelable;

public class SchoolModel implements Parcelable {




        String schoolName;
        String schoolOverview;
    public SchoolModel(Parcel in) {
        schoolName = in.readString();
        schoolOverview = in.readString();
    }
        public static final Parcelable.Creator<SchoolModel> CREATOR = new Parcelable.Creator<SchoolModel>() {
            @Override
            public SchoolModel createFromParcel(Parcel in) {
                return new SchoolModel(in);
            }

            @Override
            public SchoolModel[] newArray(int size) {
                return new SchoolModel[size];
            }
        };

        public String getSchoolOverview() {
        return schoolOverview;
    }

        public void setSchoolOverview(String schoolOverview) {
        this.schoolOverview = schoolOverview;
    }

        public String getSchoolName() {
        return schoolName;
    }

        public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public SchoolModel(String schoolName) {
        this.schoolName = schoolName;
    }
    public SchoolModel(){

    }
        @Override
        public int describeContents() {
        return 0;
    }
        @Override
        public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(schoolName);
        dest.writeString(schoolOverview);
    }
}
