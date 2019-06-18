package com.aritra.restapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aritra.restapp.model.DatePickerFragment;
import com.aritra.restapp.util.Constants;
import com.aritra.restapp.handlers.CheckAPIStatus;
import com.aritra.restapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputLayout tiEmail, tiPass;
    private EditText etEmail, etPass;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private RequestQueue requestQueue, personalrequestQueue;
    private Button btnLoginSignUp;
    private CheckBox cbSignIn;
    private ConstraintLayout logInLayout;
    TextInputEditText professionEndDate, professionStartDate;
    int id;
    int flag;
    int startYear, endYear;
    List<Integer> yearList;
    String email, password, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInLayout = findViewById(R.id.logInLayout);
        CheckAPIStatus apiStatus = new CheckAPIStatus();
        String APIStatus;

        boolean status = apiStatus.isOnline(LoginActivity.this);
        if (!status)
            APIStatus = "Online";
        else
            APIStatus = "Offline";
        Snackbar.make(logInLayout, "API is " + APIStatus, Snackbar.LENGTH_LONG).show();
        setUpUI();

        cbSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbSignIn.isChecked())
                    btnLoginSignUp.setText(R.string.signIn_button);
                else
                    btnLoginSignUp.setText(R.string.logIn_button);
            }
        });
    }

    private void setUpUI() {
        tiEmail = findViewById(R.id.textInputEmail);

        tiPass = findViewById(R.id.textInputPass);
        tiEmail.setHelperText("* Required");
        tiPass.setHelperText("* Required");

        etEmail = tiEmail.getEditText();
        etPass = tiPass.getEditText();

        cbSignIn = findViewById(R.id.cbSignIn);
        btnLoginSignUp = findViewById(R.id.btnSignUp);

        requestQueue = Volley.newRequestQueue(this);
        personalrequestQueue = Volley.newRequestQueue(this);
        
        yearList = new ArrayList<>();
        
        int year = (Calendar.getInstance().get(Calendar.YEAR)) - 40;
        for(int i = 0; i < 80; i++) {
            yearList.add(year);
            year++;
        }
    }

    public void signIn(View view) {
        email = etEmail.getText().toString();
        password = etPass.getText().toString();
        tiEmail.setError(null);
        tiEmail.setHelperText("* Required");
        tiPass.setError(null);
        tiPass.setHelperText("* Required");

        if (email.isEmpty() | password.isEmpty() | password.length() < 4) {
            if (email.isEmpty()) {
                tiEmail.setErrorEnabled(true);
                tiEmail.setError("Field cannot be empty");
            }
            if (password.isEmpty()) {
                tiPass.setErrorEnabled(true);
                tiPass.setError("Field cannot be empty");
            } else if (password.length() < 4)
                tiPass.setError("Password must be at least 4 characters...");
        } else {

            if (cbSignIn.isChecked()) {
                url = Constants.BASE_URL + Constants.SIGNUP_URL;
            } else {
                url = Constants.BASE_URL + Constants.LOGIN_URL;
            }

            executeJSONRequest();
        }

    }

    private void executeJSONRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        JSONObject signInParams = new JSONObject(params);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, signInParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("status_message"))
                        Snackbar.make(logInLayout, "Email/Password is incorrect", Snackbar.LENGTH_LONG).show();

                    else if (response.has("data")) {
                        JSONObject data = response.getJSONObject("data");
                        id = data.getInt("id");

                        SharedPreferences userId = getSharedPreferences("LoginID", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userId.edit();
                        editor.putInt("userID", id);
                        editor.apply();

                        if (cbSignIn.isChecked()) {
                            Snackbar.make(logInLayout, "User signed up with ID: " + id, Snackbar.LENGTH_LONG).show();

                            createPersonalDetailsPopup();
                        } else {
                            Snackbar.make(logInLayout, "User logged in with ID: " + id, Snackbar.LENGTH_LONG).show();
                            //TODO : Intent details activity.
                            startActivity(new Intent(LoginActivity.this, DetailsActivity.class)
                                    .putExtra("userID", id)
                            .putExtra("email", email));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("REQ_FAIL", "JSON Request Failed!");
                Toast.makeText(LoginActivity.this, "Something happened!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void createPersonalDetailsPopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_personal, null);
        final TextInputEditText personalDetailName = view.findViewById(R.id.personalName);
        final TextInputEditText personalDetailLocation = view.findViewById(R.id.personalLocation);
        final TextInputEditText personalDetailPhone = view.findViewById(R.id.personalPhone);
        final TextInputEditText personalDetailSkills = view.findViewById(R.id.personalSkill);
        final TextInputEditText personalDetailLinks = view.findViewById(R.id.personalLinks);
        Button saveButton = view.findViewById(R.id.personalSave);

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
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

                JsonObjectRequest personalRequest = new JsonObjectRequest(Request.Method.POST,
                        Constants.BASE_URL + Constants.PERSONAL_DETAILS_URL + id, personalDetails, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Personal details saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                personalrequestQueue.add(personalRequest);
                alertDialog.dismiss();
                createEducationPopup();

            }
        });
    }

    private void createEducationPopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_educational, null);

        final LabelledSpinner educationStartYear = view.findViewById(R.id.educationDetailStartYear);
        final LabelledSpinner educationEndYear = view.findViewById(R.id.educationDetailEndYear);
        final TextInputEditText educationDegree = view.findViewById(R.id.educationDetailDegree);
        final TextInputEditText educationOrganisation = view.findViewById(R.id.educationDetailOrganisation);
        final TextInputEditText educationLocation = view.findViewById(R.id.educationDetailLocation);
        Button saveButton = view.findViewById(R.id.educationalSave);
        
        educationStartYear.setItemsArray(yearList);
        educationStartYear.setSelection(yearList.indexOf(Calendar.getInstance().get(Calendar.YEAR)));
        educationEndYear.setItemsArray(yearList);
        educationEndYear.setSelection(yearList.indexOf(Calendar.getInstance().get(Calendar.YEAR) + 4));

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        educationStartYear.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                startYear = yearList.get(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        educationEndYear.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                endYear = yearList.get(position);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_year = String.valueOf(startYear);
                String degree = educationDegree.getText().toString();
                String organisation = educationOrganisation.getText().toString();
                String location = educationLocation.getText().toString();
                String end_year = String.valueOf(endYear);

                Map<String, String> educationParams = new HashMap<>();
                educationParams.put("start_year", start_year);
                educationParams.put("degree", degree);
                educationParams.put("organisation", organisation);
                educationParams.put("location", location);
                educationParams.put("end_year", end_year);

                JSONObject educationObject = new JSONObject(educationParams);

                JsonObjectRequest educationRequest = new JsonObjectRequest(Request.Method.POST,
                        Constants.BASE_URL + Constants.EDUCATION_DETAILS_URL + id, educationObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Educational details saved", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(educationRequest);
                alertDialog.dismiss();
                createProfessionalPopout();
            }
        });

    }

    private void createProfessionalPopout() {
        final Calendar calendar = Calendar.getInstance();
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_proffesional, null);

        professionStartDate = view.findViewById(R.id.professionalStartDate);
        professionEndDate = view.findViewById(R.id.professionalEndDate);
        final TextInputEditText professionOrganisation = view.findViewById(R.id.professionalOrganisation);
        final TextInputEditText professionDesignation = view.findViewById(R.id.professionalDesignation);
        final CheckBox professionalWorking = view.findViewById(R.id.cbStillWorking);
        Button saveButton = view.findViewById(R.id.professionalSave);

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
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
                JsonObjectRequest professionalRequest = new JsonObjectRequest(Request.Method.POST,
                        Constants.BASE_URL + Constants.PROFESSIONAL_DETAILS_URL + id, educationObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(LoginActivity.this, "Professional details saved",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(professionalRequest);
                alertDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, DetailsActivity.class)
                .putExtra("userID", id)
                .putExtra("email", email));
            }
        });
    }

    private void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

        Log.d("DATE", date + "-" + (month + 1) + "-" + year);
        if(flag == 0)
            professionStartDate.setText(date + "-" + (month + 1) + "-" + year);
        else
            professionEndDate.setText(date + "-" + (month + 1) + "-" + year);
    }
}
