package com.vidya.lunchbox.cart;

import android.content.Context;
import android.content.ContextWrapper;

import org.json.simple.JSONObject;

import java.io.File;
import java.util.Map;

public class Carteasy {

    public static String carteasyDirName = "carteasy";
    public static String carteasyFileName = "items.json";
    public static String settingsFileName = "settings.json";
    public static String persist = "persist";
    /* check when user's app starts for the first time, it prompts carteasy to clear existing contents of the JSON file */
    public static boolean applaunched = false;
    private JSONObject items = new JSONObject();
    private JSONObject products = new JSONObject();
    private String uniqueId;

    public static File getContextWrapper(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        return cw.getDir(Carteasy.carteasyDirName, Context.MODE_PRIVATE);
    }

    //This function Receive values as objects and ties them with an ID
    public void add(String id, String key, Object value) {
        uniqueId = id;
        products.put(key, value);
        //Store in items
        items.clear();
        items.put(id, products);
    }

    //This function calls the commit function in SaveData Class to save user's input to file.
    public void commit(Context context) {
        clearPreviousData(context);
        SaveData sd = new SaveData();
        sd.save(context, items, uniqueId, products);
    }

    //This function retrieves objects value by given id and key and displays it
    public Object get(String id, String key, Context context) {
        GetData gd = new GetData();
        return gd.getFile(id, key, context);
    }

    //This function calls the update function in SaveData Class and updates user's input data
    public void update(String mid, String mkey, Object newvalue, Context context) {
        SaveData sd = new SaveData();
        sd.updateValue(mid, mkey, newvalue, context);
    }

    //This function removes any key specified by the user
    public void Removekey(String mid, String mkey, Context context) {
        RemoveData rm = new RemoveData();
        rm.RemoveDataByKey(mid, mkey, context);
    }

    //This function removes data based on any key specified by the user
    public void RemoveId(String mid, Context context) {
        RemoveData rm = new RemoveData();
        rm.RemoveDataById(mid, context);
    }

    public Map<String, String> ViewData(String mid, Context context) {
        clearPreviousData(context);
        GetData gd = new GetData();
        return gd.ViewById(mid, context);
    }

    public Map<Integer, Map> ViewAll(Context context) {
        clearPreviousData(context);
        GetData gd = new GetData();
        return gd.ViewAll(context);
    }

    /**
     * This functions below will retrieve data and typecast it before returning it the user
     **/
    public String getString(String id, String key, Context context) {
        String value = "";
        value = (String) get(id, key, context);
        return value;
    }

    public Integer getInteger(String id, String key, Context context) {
        Integer value = 0;
        value = (Integer) get(id, key, context);
        return value;
    }

    public Float getFloat(String id, String key, Context context) {
        float value = 0;
        value = (Float) get(id, key, context);
        return value;
    }

    public Double getDouble(String id, String key, Context context) {
        Double value = 0.2;
        value = (Double) get(id, key, context);
        return value;
    }

    public Long getLong(String id, String key, Context context) {
        Long value = 9484480494l;
        value = (Long) get(id, key, context);
        return value;
    }

    public Short getShort(String id, String key, Context context) {
        Short value = 9446;
        value = (Short) get(id, key, context);
        return value;
    }

    public void clearPreviousData(Context context) {
        if (applaunched == false && !isPersistEnabled(context)) {
            RemoveData rm = new RemoveData();
            rm.ClearAllData(context);
            persistData(context, false);
            applaunched = true;
        }
    }

    public void clearCart(Context context) {
        RemoveData rm = new RemoveData();
        rm.clearAllFromCart(context);
    }

    public void persistData(Context context, boolean status) {
        SaveData sd = new SaveData();
        sd.persist(context, status);
    }

    public Boolean isPersistEnabled(Context context) {
        GetData gd = new GetData();
        return gd.getPersistStatus(context);
    }
}