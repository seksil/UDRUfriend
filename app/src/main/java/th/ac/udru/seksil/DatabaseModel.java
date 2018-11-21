package th.ac.udru.seksil;

import android.os.Parcel;
import android.os.Parcelable;

public class DatabaseModel implements Parcelable {
    private String uidString,pathUrlString,nameString;
    private Double latADouble , lngADouble;

    public DatabaseModel() {
    }

    public DatabaseModel(String uidString, String pathUrlString, String nameString, Double latADouble, Double lngADouble) {
        this.uidString = uidString;
        this.pathUrlString = pathUrlString;
        this.nameString = nameString;
        this.latADouble = latADouble;
        this.lngADouble = lngADouble;
    }

    protected DatabaseModel(Parcel in) {
        uidString = in.readString();
        pathUrlString = in.readString();
        nameString = in.readString();
        if (in.readByte() == 0) {
            latADouble = null;
        } else {
            latADouble = in.readDouble();
        }
        if (in.readByte() == 0) {
            lngADouble = null;
        } else {
            lngADouble = in.readDouble();
        }
    }

    public static final Creator<DatabaseModel> CREATOR = new Creator<DatabaseModel>() {
        @Override
        public DatabaseModel createFromParcel(Parcel in) {
            return new DatabaseModel(in);
        }

        @Override
        public DatabaseModel[] newArray(int size) {
            return new DatabaseModel[size];
        }
    };

    public String getUidString() {
        return uidString;
    }

    public void setUidString(String uidString) {
        this.uidString = uidString;
    }

    public String getPathUrlString() {
        return pathUrlString;
    }

    public void setPathUrlString(String pathUrlString) {
        this.pathUrlString = pathUrlString;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public Double getLatADouble() {
        return latADouble;
    }

    public void setLatADouble(Double latADouble) {
        this.latADouble = latADouble;
    }

    public Double getLngADouble() {
        return lngADouble;
    }

    public void setLngADouble(Double lngADouble) {
        this.lngADouble = lngADouble;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidString);
        dest.writeString(pathUrlString);
        dest.writeString(nameString);
        if (latADouble == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latADouble);
        }
        if (lngADouble == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lngADouble);
        }
    }
}