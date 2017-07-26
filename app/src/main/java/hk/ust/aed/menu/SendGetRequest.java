package hk.ust.aed.menu;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 18/07/2017.
 */

public class SendGetRequest {
    private Context context;
    private String url;
    private ParseRequest parseRequest;
    //private ImageView holder;

    public SendGetRequest(Context context, String url, ParseRequest parseRequest) {
        this.context = context;
        this.url = url;
        this.parseRequest = parseRequest;
        //this.holder = holder;
        sendRequest();
    }

    public void sendRequest(){
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseRequest.execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get Request", "Doesn't work");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}