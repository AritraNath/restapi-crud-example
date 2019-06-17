package com.aritra.restapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    
    SubtitleCollapsingToolbarLayout scLayout;
    private TextView tvPerMobile, tvPerLocation, tvPerSkills, tvPerLinks;
    private TextView tvEduOrg, tvEduDegree, tvEduLoc, tvEduStartYear, tvEduEndYear;
    private TextView tvProOrg, tvProDesignation, tvProStartDate, tvProEndDate;
    RequestQueue requestQueue;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    int id;
    String email, personalURL, educationURL, professionURL;

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

        FloatingActionButton floatingActionButton = findViewById(R.id.fabLogout);
        floatingActionButton.setOnClickListener(this);

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
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_personal, null);
        final TextInputEditText personalDetailName = view.findViewById(R.id.personalName);
        final TextInputEditText personalDetailLocation = view.findViewById(R.id.personalLocation);
        final TextInputEditText personalDetailPhone = view.findViewById(R.id.personalPhone);
        final TextInputEditText personalDetailSkills = view.findViewById(R.id.personalSkill);
        final TextInputEditText personalDetailLinks = view.findViewById(R.id.personalLinks);

        personalDetailName.setText(scLayout.getTitle());
        personalDetailLocation.setText(tvPerLocation.getText());
        personalDetailPhone.setText(tvPerMobile.getText());
        personalDetailSkills.setText(tvPerSkills.getText());
        personalDetailLinks.setText(tvPerLinks.getText());

        Button saveButton = view.findViewById(R.id.personalSave);
        saveButton.setText("Update");

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String skills = personalDetailSkills.getText().toString();
                String mobile_no = personalDetailPhone.getText().toString();
                String name = personalDetailName.getText().toString();
                String links = personalDetailLinks.getText().toString();
                String location = personalDetailLocation.getText().toString();

                Map<String, String> personalParams = new HashMap<>();
                personalParams.put("skills", skills);
                personalParams.put("mobile_no", mobile_no);
                personalParams.put("name", name);
                personalParams.put("links", links);
                personalParams.put("location", location);
                personalParams.put("email", email);

                JSONObject personalDetails = new JSONObject(personalParams);

                JsonObjectRequest personalRequest = new JsonObjectRequest(Request.Method.PUT,
                        Constants.BASE_URL + Constants.PERSONAL_DETAILS_URL + id, personalDetails, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DetailsActivity.this, "Personal details updated", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(personalRequest);
                alertDialog.dismiss();
                startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
                        .putExtra("userID", id)
                        .putExtra("email", email));

            }
        });
    }

    private void updateEducationDetails() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_educational, null);

        final TextInputEditText educationStartYear = view.findViewById(R.id.educationDetailStartYear);
        final TextInputEditText educationEndYear = view.findViewById(R.id.educationDetailEndYear);
        final TextInputEditText educationDegree = view.findViewById(R.id.educationDetailDegree);
        final TextInputEditText educationOrganisation = view.findViewById(R.id.educationDetailOrganisation);
        final TextInputEditText educationLocation = view.findViewById(R.id.educationDetailLocation);
        Button saveButton = view.findViewById(R.id.educationalSave);
        saveButton.setText("Update");

        educationStartYear.setText(tvEduStartYear.getText());
        educationEndYear.setText(tvEduEndYear.getText());
        educationDegree.setText(tvEduDegree.getText());
        educationOrganisation.setText(tvEduOrg.getText());
        educationLocation.setText(tvEduLoc.getText());

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_year = educationStartYear.getText().toString();
                String degree = educationDegree.getText().toString();
                String organisation = educationOrganisation.getText().toString();
                String location = educationLocation.getText().toString();
                String end_year = educationEndYear.getText().toString();

                Map<String, String> educationParams = new HashMap<>();
                educationParams.put("start_year", start_year);
                educationParams.put("degree", degree);
                educationParams.put("organisation", organisation);
                educationParams.put("location", location);
                educationParams.put("end_year", end_year);

                JSONObject educationObject = new JSONObject(educationParams);

                JsonObjectRequest educationRequest = new JsonObjectRequest(Request.Method.PUT,
                        Constants.BASE_URL + Constants.EDUCATION_DETAILS_URL + id, educationObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(DetailsActivity.this, "Educational details updated", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(educationRequest);
                alertDialog.dismiss();
                startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
                .putExtra("userID", id)
                .putExtra("email", email));
            }
        });
    }

    private void updateProfessionalDetails() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_proffesional, null);

        final TextInputEditText professionStartDate = view.findViewById(R.id.professionalStartDate);
        final TextInputEditText professionEndDate = view.findViewById(R.id.professionalEndDate);
        final TextInputEditText professionOrganisation = view.findViewById(R.id.professionalOrganisation);
        final TextInputEditText professionDesignation = view.findViewById(R.id.professionalDesignation);
        Button saveButton = view.findViewById(R.id.professionalSave);
        saveButton.setText("Update");

        professionStartDate.setText(tvProStartDate.getText());
        professionEndDate.setText(tvProEndDate.getText());
        professionOrganisation.setText(tvProOrg.getText());
        professionDesignation.setText(tvProDesignation.getText());

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String end_date = professionEndDate.getText().toString();
                String organisation = professionOrganisation.getText().toString();
                String designation = professionDesignation.getText().toString();
                String start_date = professionStartDate.getText().toString();

                Map<String, String> professionalParams = new HashMap<>();
                professionalParams.put("end_date", end_date);
                professionalParams.put("organisation", organisation);
                professionalParams.put("designation", designation);
                professionalParams.put("start_date", start_date);

                JSONObject educationObject = new JSONObject(professionalParams);
                JsonObjectRequest professionalRequest = new JsonObjectRequest(Request.Method.PUT,
                        Constants.BASE_URL + Constants.PROFESSIONAL_DETAILS_URL + id, educationObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(DetailsActivity.this, "Professional details updated",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(professionalRequest);
                alertDialog.dismiss();
                startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
                        .putExtra("userID", id)
                        .putExtra("email", email));
            }
        });
    }

    private void deletePersonalDetails() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, personalURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void deleteEducationDetails() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, educationURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void deleteProfessionalDetails() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, professionURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void logoutUser() {
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
    }

}
