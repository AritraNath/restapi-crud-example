package com.aritra.restapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aritra.restapp.data.Details;
import com.aritra.restapp.R;
import com.aritra.restapp.util.Constants;
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    
    SubtitleCollapsingToolbarLayout scLayout;
    private TextView tvPerMobile, tvPerLocation, tvPerSkills, tvPerLinks;
    private TextView tvEduOrg, tvEduDegree, tvEduLoc, tvEduStartYear, tvEduEndYear;
    private TextView tvProOrg, tvProDesignation, tvProStartDate, tvProEndDate;
    RequestQueue requestQueue;
    int id;
    String email, personalURL, educationURL, professionURL;
    String perName, perLoc, perMob, perSkill, perLink;
    String eduOrg, eduDegree, eduLoc, eduStartYear, eduEndYear;
    String proOrg, proDesignation, proStartDate, proEndDate;

    Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        id = intent.getIntExtra("userID", -1);
        email = intent.getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        scLayout = findViewById(R.id.subtitleLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        personalURL = Constants.BASE_URL + Constants.PERSONAL_DETAILS_URL + id;
        educationURL = Constants.BASE_URL + Constants.EDUCATION_DETAILS_URL + id;
        professionURL = Constants.BASE_URL + Constants.PROFESSIONAL_DETAILS_URL + id;

        setupUI();
        
    }

    private void setupUI() {
        tvPerMobile = findViewById(R.id.personalMobile);
        tvPerLocation = findViewById(R.id.personalLocation);
        tvPerSkills = findViewById(R.id.personalSkills);
        tvPerLinks = findViewById(R.id.personalLink);

        ImageButton ivperEdit = findViewById(R.id.personalEditButton);
        ImageButton ivperDelete = findViewById(R.id.personalDeleteButton);
        ivperEdit.setOnClickListener(this);
        ivperDelete.setOnClickListener(this);

        tvEduOrg = findViewById(R.id.educationOrganisation);
        tvEduDegree = findViewById(R.id.educationDegree);
        tvEduLoc = findViewById(R.id.educationLocation);
        tvEduStartYear = findViewById(R.id.educationStartYear);
        tvEduEndYear = findViewById(R.id.educationEndYear);

        ImageButton iveduEdit = findViewById(R.id.educationEditButton);
        ImageButton iveduDelete = findViewById(R.id.educationDeleteButton);
        iveduEdit.setOnClickListener(this);
        iveduDelete.setOnClickListener(this);

        tvProOrg = findViewById(R.id.professionOrganisation);
        tvProDesignation = findViewById(R.id.professionDesignation);
        tvProStartDate = findViewById(R.id.professionStartDate);
        tvProEndDate = findViewById(R.id.professionEndDate);

        ImageButton ivproEdit = findViewById(R.id.professionEditButton);
        ImageButton ivproDelete = findViewById(R.id.professionDeleteButton);
        ivproEdit.setOnClickListener(this);
        ivproDelete.setOnClickListener(this);

        details = new Details();
        requestQueue = Volley.newRequestQueue(this);

        jsonQuery();
    }

    private void jsonQuery() {
        JsonObjectRequest requestPer = new JsonObjectRequest(Request.Method.GET, personalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("data")) {
                    try {
                        JSONObject object = response.getJSONObject("data");
                        scLayout.setTitle(object.getString("name"));
                        tvPerLocation.setText(object.getString("location"));
                        tvPerMobile.setText(object.getString("mobile_no"));
                        tvPerSkills.setText(object.getString("skills"));
                        tvPerLinks.setText(object.getString("links"));

//                        details.setPersonalDetails(object.getString("name"), object.getString("location"),
//                                object.getString("mobile_no"), object.getString("skills"), object.getString("links"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(requestPer);

        JsonObjectRequest requestEdu = new JsonObjectRequest(Request.Method.GET, educationURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("data")) {
                    try {
                        JSONObject object = response.getJSONObject("data");
                        tvEduOrg.setText(object.getString("organisation"));
                        tvEduDegree.setText(object.getString("degree"));
                        tvEduLoc.setText(object.getString("location"));
                        tvEduStartYear.setText(object.getString("start_year"));
                        tvEduEndYear.setText(object.getString("end_year"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(requestEdu);

        JsonObjectRequest requestPro = new JsonObjectRequest(Request.Method.GET, professionURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("data")) {
                    try {
                        JSONObject object = response.getJSONObject("data");
                        tvProOrg.setText(object.getString("organisation"));
                        tvProDesignation.setText(object.getString("designation"));
                        tvProStartDate.setText(object.getString("start_date"));
                        tvProEndDate.setText(object.getString("end_date"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(requestPro);

        getDataFromFields();
    }

    private void getDataFromFields() {
        perName = Objects.requireNonNull(scLayout.getTitle()).toString();

        perMob = tvPerMobile.getText().toString();
        perLoc = tvPerLocation.getText().toString();
        perSkill = tvPerSkills.getText().toString();
        perLink = tvPerLinks.getText().toString();

        eduOrg = tvEduOrg.getText().toString();
        eduDegree = tvEduDegree.getText().toString();
        eduLoc = tvEduLoc.getText().toString();
        eduStartYear = tvEduStartYear.getText().toString();
        eduEndYear = tvEduEndYear.getText().toString();

        proOrg = tvProOrg.getText().toString();
        proDesignation = tvProDesignation.getText().toString();
        proStartDate = tvProStartDate.getText().toString();
        proEndDate = tvProEndDate.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.personalEditButton :
                updatePersonalDetails();
                break;
            case R.id.personalDeleteButton :
                deletePersonalDetails();
                break;
            case R.id.educationEditButton :
                updateEducationDetails();
                break;
            case R.id.educationDeleteButton :
                deleteEducationDetails();
                break;
            case R.id.professionEditButton :
                updateProfessionalDetails();
                break;
            case R.id.professionDeleteButton :
                deleteProfessionalDetails();
            case R.id.fabLogout :
                logoutUser();
                break;
        }
    }

    private void updatePersonalDetails() {

    }

    private void updateEducationDetails() {

    }

    private void updateProfessionalDetails() {

    }

    private void deletePersonalDetails() {

    }

    private void deleteEducationDetails() {

    }

    private void deleteProfessionalDetails() {

    }

    private void logoutUser() {
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            finish();
            startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
            .putExtra("userID", id)
            .putExtra("email", email));
        }
    }
}
