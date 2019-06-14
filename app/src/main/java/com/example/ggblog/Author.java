package com.example.ggblog;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

/*
Author Info that can be passed between Activities through Intent.
This allow us to avoid asking the Author information to server when passing from the Authors List
to Author details, since this information has already been retrieved and saved into this object.
*/
public class Author implements Parcelable {

    private static final String TAG = "Author";

    private static final int LATITUDE_ARRAY_INDEX = 0;
    private static final int LONGITUDE_ARRAY_INDEX = 1;

    private String mId;
    private String mName;
    private String mUserName;
    private String mEmail;
    private String mAvatarUrl;
    /* Latitude and Longitude */
    private String[] mAddressCoordinates;


    public static final Parcelable.Creator<Author> CREATOR
            = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mUserName);
        dest.writeString(mEmail);
        dest.writeString(mAvatarUrl);
        dest.writeInt(mAddressCoordinates.length);
        for (String coordinate: mAddressCoordinates) {
            dest.writeString(coordinate);
        }
    }

    private Author(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mUserName = in.readString();
        mEmail = in.readString();
        mAvatarUrl = in.readString();
        int size = in.readInt();
        mAddressCoordinates = new String[size];
        for(int i=0; i < size; i++){
            mAddressCoordinates[i] = in.readString();
        }
    }

    public Author(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                mId = jsonObject.getString(UrlParams.ID);
                mName = jsonObject.getString(UrlParams.NAME);
                mUserName = jsonObject.getString(UrlParams.USERNAME);
                mEmail = jsonObject.getString(UrlParams.EMAIL);
                mAvatarUrl = jsonObject.getString(UrlParams.AVATAR_URL);
                JSONObject jsonObjectAddress =
                        jsonObject.getJSONObject(UrlParams.ADDRESS);
                if (jsonObjectAddress != null) {
                    mAddressCoordinates = new String[2];
                    mAddressCoordinates[LATITUDE_ARRAY_INDEX] =
                            jsonObjectAddress.getString(UrlParams.ADDRESS_LAT);
                    mAddressCoordinates[LONGITUDE_ARRAY_INDEX] =
                            jsonObjectAddress.getString(UrlParams.ADDRESS_LONG);
                } else {
                    Log.e(TAG, "unable to retrieve the author address");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "jsonObject is NULL");
        }
    }

    public Author() {
        mId = null;
        mName = null;
        mUserName = null;
        mEmail = null;
        mAvatarUrl = null;
        mAddressCoordinates = new String[2];
        Arrays.fill(mAddressCoordinates, null);
    }

    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public void setAddressLatitude(String latitude) {
        mAddressCoordinates[LATITUDE_ARRAY_INDEX] = latitude;
    }

    public void setAddressLongitude(String longitude) {
        mAddressCoordinates[LONGITUDE_ARRAY_INDEX] = longitude;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public String getAddressLatitude() {
        return mAddressCoordinates[LATITUDE_ARRAY_INDEX];
    }

    public String getAddressLongitude() {
        return mAddressCoordinates[LONGITUDE_ARRAY_INDEX];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id=").append(mId).append(" - ");
        stringBuilder.append("Name=").append(mName).append(" - ");
        stringBuilder.append("User Name=").append(mUserName).append(" - ");
        stringBuilder.append("Email=").append(mEmail).append(" - ");
        stringBuilder.append("Avatar URL=").append(mAvatarUrl).append(" - ");
        stringBuilder.append("Address Latitude=").append(
                mAddressCoordinates[LATITUDE_ARRAY_INDEX]).append(" - ");
        stringBuilder.append("Address Longitude=").append(
                mAddressCoordinates[LONGITUDE_ARRAY_INDEX]);
        return stringBuilder.toString();
    }
}