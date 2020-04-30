package com.example.covid_19.ui.search;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.covid_19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {
    TextView Tcases,TcasesI,Tdeaths,TdeathsI,Trecovered,TStateName;
    public static final String TAG = "MyTag";
    RequestQueue requestQueue;  // Assume this exists.



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TableLayout tl = (TableLayout) view.findViewById(R.id.CityStats);
        super.onViewCreated(view, savedInstanceState);
        Tcases = (TextView) view.findViewById(R.id.CasesN);
        TcasesI = (TextView) view.findViewById(R.id.CasesI);
        Tdeaths = (TextView) view.findViewById(R.id.DeathN);
        TdeathsI = (TextView) view.findViewById(R.id.DeathI);
        Trecovered = (TextView) view.findViewById(R.id.RecoveredN);
        TStateName = (TextView) view.findViewById(R.id.StateName);
        Button search = (Button) view.findViewById(R.id.search);

        WebView myWebView = (WebView) view.findViewById(R.id.webView);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("https://lamp.cse.fau.edu/~yfeng2016/covidchart/StateChart.html");


        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                EditText editText = (EditText) getActivity().findViewById(R.id.editText);
                String name = editText.getText().toString();

                Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

                // Set up the network to use HttpURLConnection as the HTTP client.
                Network network = new BasicNetwork(new HurlStack());

                // Instantiate the RequestQueue with the cache and network.
                requestQueue = new RequestQueue(cache, network);
                // Start the queue
                requestQueue.start();

                String url = "https://covidtracking.com/api/v1/states/"+name+"/daily.json";
                // Formulate the request and handle the response.
                JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < 1; i++) {

                                        JSONObject obj = response.getJSONObject(i);
                                        String state = obj.getString ("state");
                                        String cases =  obj.getString ("positive");
                                        String casesI =  obj.getString ("positiveIncrease");
                                        String deaths =  obj.getString ("death");
                                        String deathsI =  obj.getString ("deathIncrease");
                                        String recovered =  obj.getString ("recovered");

                                        TStateName.setText(state);
                                        Tcases.setText(cases);
                                        TcasesI.setText("+"+casesI);
                                        Tdeaths.setText(deaths);
                                        TdeathsI.setText("+"+deathsI);
                                        Trecovered.setText(recovered);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                TStateName.setText("Error");
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                // Set the tag on the request.
                jsonarrayRequest.setTag(TAG);
                // Add the request to the RequestQueue.
                requestQueue.add(jsonarrayRequest);
            }

        });
    }
}
