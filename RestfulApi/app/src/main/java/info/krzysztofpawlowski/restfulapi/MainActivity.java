package info.krzysztofpawlowski.restfulapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(getApplicationContext());

        final Button addBearButton = (Button)
                findViewById(R.id.add_bear_button);
        final EditText bearNameEditText = (EditText)
                findViewById(R.id.bear_name_edit_text);

        addBearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bearName = bearNameEditText
                        .getText().toString();
                postBear(bearName);
            }
        });

        final Button refreshButton = (Button)
                findViewById(R.id.refresh_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBears();
            }
        });
    }

    private void getBears() {
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "http://10.0.2.2:8080/api/bears",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    List<String> namesList = new LinkedList<>();
                                    for (int i = 0; i < jsonArray.length(); ++i) {
                                        namesList.add(jsonArray.getJSONObject(i).getString("name"));
                                    }
                                    StringBuilder bearsListStringBuilder = new StringBuilder();
                                    for (String name : namesList) {
                                        bearsListStringBuilder.append(name + "\n");
                                    }
                                    String bearsNames = bearsListStringBuilder.toString();
                                    TextView bearsNameTextView = (TextView)
                                            findViewById(R.id.bears_list_text_view);
                                    bearsNameTextView.setText(bearsNames);

                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Error while parking " +
                                            "response", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    private void postBear(final String bearName) {
        StringRequest sr = new StringRequest
                (Request.Method.POST,"http://10.0.2.2:8080/api/bears", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", bearName);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}
