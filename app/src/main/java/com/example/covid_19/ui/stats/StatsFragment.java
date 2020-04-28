package com.example.covid_19.ui.stats;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covid_19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatsFragment extends Fragment {

    TextView Tcases,TcasesI,Tdeaths,TdeathsI,Trecovered,TrecoveredI,TL1,TC1,TD1,TR1,TL2,TC2,TD2,TR2;
    public static final String TAG = "MyTag";
    RequestQueue requestQueue;  // Assume this exists.


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
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
        TL1 = (TextView) view.findViewById(R.id.L1);
        TC1 = (TextView) view.findViewById(R.id.C1);
        TD1 = (TextView) view.findViewById(R.id.D1);
        TR1 = (TextView) view.findViewById(R.id.R1);
        TL2 = (TextView) view.findViewById(R.id.L2);
        TC2 = (TextView) view.findViewById(R.id.C2);
        TD2 = (TextView) view.findViewById(R.id.D2);
        TR2 = (TextView) view.findViewById(R.id.R2);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();
        
        String url = "https://corona.lmao.ninja/v2/countries/US";
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
                            String recovered = response.getString ("recovered");

                            Tcases.setText(cases);
                            TcasesI.setText("+"+casesI);
                            Tdeaths.setText(deaths);
                            TdeathsI.setText("+"+deathsI);
                            Trecovered.setText(recovered);


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

        getstate();

    }

    public void getstate()
    {
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();

        String url2 = "https://covidtracking.com/api/states";
        // Formulate the request and handle the response.
        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONObject AK = response.getJSONObject(0);
                            String l1 = AK.getString ("state");
                            String c1 = AK.getString ("positive");
                            String d1 = AK.getString ("death");
                            String r1 = AK.getString ("recovered");

                            JSONObject AL = response.getJSONObject(1);
                            String l2 = AL.getString ("state");
                            String c2 = AL.getString ("positive");
                            String d2 = AL.getString ("death");
                            String r2 = AL.getString ("recovered");

                            TL1.setText(l1);
                            TC1.setText(c1);
                            TD1.setText(d1);
                            TR1.setText(r1);

                            TL2.setText(l2);
                            TC2.setText(c2);
                            TD2.setText(d2);
                            TR2.setText(r2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TL1.setText("That didn't work!");
                    }
                });

        // Set the tag on the request.
        jsonarrayRequest.setTag(TAG);

        requestQueue.add(jsonarrayRequest);

    }

}
