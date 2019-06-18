package com.aritra.restapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aritra.restapp.R;
import com.aritra.restapp.model.DatePickerFragment;
import com.aritra.restapp.util.Constants;
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    
    SubtitleCollapsingToolbarLayout scLayout;
    CardView personal, educational, professional;
    private TextView tvPerMobile, tvPerLocation, tvPerSkills, tvPerLinks;
    private TextView tvEduOrg, tvEduDegree, tvEduLoc, tvEduStartYear, tvEduEndYear;
    private TextView tvProOrg, tvProDesignation, tvProStartDate, tvProEndDate;
    RequestQueue requestQueue;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    int id, eduDelID, proDelID;
    TextInputEditText professionEndDate, professionStartDate;
    int flag;

    int syYear, eyYear;
    List<Integer> yearList;
    String email, personalURL, educationURL, professionURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        id = intent.getIntExtra("userID", -1);
        email = intent.getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        scLayout = findViewById(R.id.subtitleLayout);
        scLayout.setSubtitle(email);
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

        ImageButton ivPerEdit = findViewById(R.id.personalEditButton);
        ivPerEdit.setOnClickListener(this);

        tvEduOrg = findViewById(R.id.educationOrganisation);
        tvEduDegree = findViewById(R.id.educationDegree);
        tvEduLoc = findViewById(R.id.educationLocation);
        tvEduStartYear = findViewById(R.id.educationStartYear);
        tvEduEndYear = findViewById(R.id.educationEndYear);

        ImageButton ivEduEdit = findViewById(R.id.educationEditButton);
        ImageButton ivEduDelete = findViewById(R.id.educationDeleteButton);
        ivEduEdit.setOnClickListener(this);
        ivEduDelete.setOnClickListener(this);

        tvProOrg = findViewById(R.id.professionOrganisation);
        tvProDesignation = findViewById(R.id.professionDesignation);
        tvProStartDate = findViewById(R.id.professionStartDate);
        tvProEndDate = findViewById(R.id.professionEndDate);

        ImageButton ivProEdit = findViewById(R.id.professionEditButton);
        ImageButton ivProDelete = findViewById(R.id.professionDeleteButton);
        ivProEdit.setOnClickListener(this);
        ivProDelete.setOnClickListener(this);

        FloatingActionButton floatingActionButton = findViewById(R.id.fabLogout);
        floatingActionButton.setOnClickListener(this);

        personal = findViewById(R.id.personalDetailsHideCard);
        educational = findViewById(R.id.educationalDetailsHideCard);
        professional = findViewById(R.id.professionalDetailsHideCard);

        requestQueue = Volley.newRequestQueue(this);

        yearList = new ArrayList<>();
        int year = (Calendar.getInstance().get(Calendar.YEAR)) - 40;
        for(int i = 0; i < 80; i++) {
            yearList.add(year);
            year++;
        }

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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else
                    personal.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                personal.setVisibility(View.VISIBLE);
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error occurred!", Snackbar.LENGTH_LONG).show();
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
                        eduDelID = object.getInt("id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    educational.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                educational.setVisibility(View.VISIBLE);
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
                        proDelID = object.getInt("id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    professional.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                professional.setVisibility(View.VISIBLE);
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
                        Snackbar.make(getWindow().getDecorView().getRootView(),
                                "Details updated", Snackbar.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error occurred!", Snackbar.LENGTH_LONG).show();
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

        final LabelledSpinner educationStartYear = view.findViewById(R.id.educationDetailStartYear);
        final LabelledSpinner educationEndYear = view.findViewById(R.id.educationDetailEndYear);
        final TextInputEditText educationDegree = view.findViewById(R.id.educationDetailDegree);
        final TextInputEditText educationOrganisation = view.findViewById(R.id.educationDetailOrganisation);
        final TextInputEditText educationLocation = view.findViewById(R.id.educationDetailLocation);
        Button saveButton = view.findViewById(R.id.educationalSave);
        saveButton.setText("Update");

        educationStartYear.setItemsArray(yearList);
        educationEndYear.setItemsArray(yearList);

        syYear = Integer.parseInt(tvEduStartYear.getText().toString());
        educationStartYear.setSelection(yearList.indexOf(syYear));

        eyYear = Integer.parseInt(tvEduEndYear.getText().toString());
        educationEndYear.setSelection(yearList.indexOf(eyYear));
        educationDegree.setText(tvEduDegree.getText());
        educationOrganisation.setText(tvEduOrg.getText());
        educationLocation.setText(tvEduLoc.getText());

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        educationStartYear.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                syYear = yearList.get(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        educationEndYear.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                eyYear = yearList.get(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_year = String.valueOf(syYear);
                String degree = educationDegree.getText().toString();
                String organisation = educationOrganisation.getText().toString();
                String location = educationLocation.getText().toString();
                String end_year = String.valueOf(eyYear);

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
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Details updated", Snackbar.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error occurred!", Snackbar.LENGTH_LONG).show();
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

        professionStartDate = view.findViewById(R.id.professionalStartDate);
        professionEndDate = view.findViewById(R.id.professionalEndDate);
        final TextInputEditText professionOrganisation = view.findViewById(R.id.professionalOrganisation);
        final TextInputEditText professionDesignation = view.findViewById(R.id.professionalDesignation);
        final CheckBox professionalWorking = view.findViewById(R.id.cbStillWorking);
        Button saveButton = view.findViewById(R.id.professionalSave);
        saveButton.setText("Update");

        professionStartDate.setText(tvProStartDate.getText());
        professionEndDate.setText(tvProEndDate.getText());
        professionOrganisation.setText(tvProOrg.getText());
        professionDesignation.setText(tvProDesignation.getText());

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        professionalWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(professionalWorking.isChecked()) {
                    professionEndDate.setText("Present");
                    professionEndDate.setClickable(false);
                }

                else {
                    professionEndDate.setClickable(true);
                    professionEndDate.setText(null);
                }
            }
        });

        professionStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                showDatePicker();
            }
        });

        professionEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                showDatePicker();
            }
        });

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
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Details updated", Snackbar.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error occurred!", Snackbar.LENGTH_LONG).show();
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

    private void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void deleteEducationDetails() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete educational details?")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                                Constants.BASE_URL + Constants.EDUCATION_DETAILS_URL + eduDelID,
                                null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Details deleted", Snackbar.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                                startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
                                        .putExtra("userID", id)
                                        .putExtra("email", email));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Delete request failed", Snackbar.LENGTH_LONG).show();
                            }
                        });
                        requestQueue.add(request);
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void deleteProfessionalDetails() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete profession details?")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                                Constants.BASE_URL + Constants.PROFESSIONAL_DETAILS_URL + proDelID,
                                null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Details deleted", Snackbar.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                                startActivity(new Intent(DetailsActivity.this, DetailsActivity.class)
                                        .putExtra("userID", id)
                                        .putExtra("email", email));

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        "Delete request failed", Snackbar.LENGTH_LONG).show();
                            }
                        });
                        requestQueue.add(request);
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    private void logoutUser() {
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        Log.d("DATE: ", "Hello");
        if(flag == 0)
            professionStartDate.setText(date + "-" + (month + 1) + "-" + year);
        else
            professionEndDate.setText(date + "-" + (month + 1) + "-" + year);
    }
}
