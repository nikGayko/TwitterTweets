package gmap.polygon.API;

import twitter4j.JSONObject;

public class Location {

    private final String GMAPS_API_KEY = "&key=AIzaSyDcDiBmRg_NQeo1yuT3YMk_-yid8h7My0Y";
//    private final String GMAPS_API_KEY = "&key=AIzaSyATOYFPG3cc7A2VGJsd27W3EnhdMrxc_Ro";
//    private final String GMAPS_API_KEY = "&key=AIzaSyB8KcL05BVdm1s5-6q56ki2Ot1PvVklu2M";
    private final String GMAPS_GEOCODE_QUERY = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";

    private static Location sInstance;
    private Location() {

    }

    public static Location getInstance() {
        if (sInstance == null) {
            sInstance = new Location();
        }
        return sInstance;
    }

    public JSONResponse getLocation(float lat, float lon, AreaLevel areaLevel) {
        JSONObject jsonObject = new HttpClient().getJSON(GMAPS_GEOCODE_QUERY + getLatLon(lat, lon) + areaLevel.value() + GMAPS_API_KEY);

        return parseJSON(jsonObject);
    }

    private String getLatLon(float latitude, float longitude) {
        return String.valueOf(latitude) + "," + String.valueOf(longitude);
    }

    private JSONResponse parseJSON(JSONObject jObject)  {

        StatusCodes statusCodes = StatusCodes.NO_CODE;
        try {
            statusCodes = StatusCodes.valueOf(jObject.getString("status"));

            JSONObject object = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(0);

            return new JSONResponse(object.getString("long_name"), object.getString("short_name"), statusCodes);

        } catch (twitter4j.JSONException e) {
            return new JSONResponse(statusCodes);
        }
    }
}
