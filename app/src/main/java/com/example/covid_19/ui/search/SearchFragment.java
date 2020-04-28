package com.example.covid_19.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covid_19.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {
    TextView Tcases,TcasesI,Tdeaths,TdeathsI,Trecovered,TrecoveredI;
    public static final String TAG = "MyTag";
    RequestQueue requestQueue;  // Assume this exists.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tcases = (TextView) view.findViewById(R.id.CasesN);
        TcasesI = (TextView) view.findViewById(R.id.CasesI);
        Tdeaths = (TextView) view.findViewById(R.id.DeathN);
        TdeathsI = (TextView) view.findViewById(R.id.DeathI);
        Trecovered = (TextView) view.findViewById(R.id.RecoveredN);
        TrecoveredI = (TextView) view.findViewById(R.id.RecoveredI);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();

        String url = "https://corona.lmao.ninja/v2/states/Florida?yesterday=1";
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String cases = response.getString ("cases");
                            String casesI = response.getString ("todayCases");
                            String deaths = response.getString ("deaths");
                            String deathsI = response.getString ("todayDeaths");


                            Tcases.setText(cases);
                            TcasesI.setText("+"+casesI);
                            Tdeaths.setText(deaths);
                            TdeathsI.setText("+"+deathsI);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Tcases.setText("That didn't work!");
                    }
                });



        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);



    }
}
