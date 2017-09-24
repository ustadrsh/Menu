//TODO: (Required) Add your package name
package hk.ust.aed.menu;

//TODO: (Required) Add your package name to the resource import

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Primary on 4/20/2016.
 *
 */
public class LoginRegisterDialog extends DialogFragment {

    private static View mView;
    private static AlertDialog mDialog;
    private FirebaseAuth mAuth;
    private Login parent;
    private Context context;

    private String email;
    private String password;
    private int yearOfBirth;
    private String displayName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mView = inflater.inflate(R.layout.dialog_login_register, null);
        mAuth = FirebaseAuth.getInstance();
        context = getActivity().getApplicationContext();
        parent = (Login) getActivity();

        builder.setView(mView)
                .setPositiveButton(R.string.login_register, null)
                .setNegativeButton(R.string.login_verify_cancel, null);
        this.setRetainInstance(true);

        mDialog = builder.create();
        return mDialog;
    }

    private boolean verifyInput() {
        EditText emailView = (EditText) mView.findViewById(R.id.register_email);
        email = emailView.getText().toString();

        EditText passwordView = (EditText) mView.findViewById(R.id.register_password);
        password = passwordView.getText().toString();

        EditText passwordRetypeView = (EditText) mView.findViewById(R.id.register_retype_password);
        String passwordRetype = passwordRetypeView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!Login.isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            return false;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!Login.isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            return false;
        } else {
            // Check for a valid retyped password, if the user entered one.
            if (TextUtils.isEmpty(passwordRetype)) {
                passwordRetypeView.setError(getString(R.string.error_field_required));
                return false;
            } else if (!password.equals(passwordRetype)) {
                passwordView.setError(getString(R.string.error_password_retype));
                passwordRetypeView.setError(getString(R.string.error_password_retype));
                return false;
            }
        }

        EditText nameView = (EditText) mView.findViewById(R.id.register_name);
        displayName = nameView.getText().toString();
        if (TextUtils.isEmpty(displayName)){
            nameView.setError(getString(R.string.error_field_required));
            return false;
        }

        EditText ageView = (EditText) mView.findViewById(R.id.register_age);
        String ageString = ageView.getText().toString();
        if (TextUtils.isEmpty(ageString)) {
            ageView.setError(getString(R.string.error_field_required));
            return false;
        }
        try{
            yearOfBirth = Integer.parseInt(ageString);
            if(yearOfBirth < 1897 || yearOfBirth > 1999){
                ageView.setError(getString(R.string.error_invalid_yob));
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInput()) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        addUserInfo();
                                        LoginRegisterDialog.this.getDialog().cancel();
                                        parent.setCredentials(email, password);
                                        boolean fromLoginActivity = false;
                                        parent.attemptLogin(fromLoginActivity);
                                        Toast.makeText(context, "Registration successful. Logging in!", Toast.LENGTH_SHORT);
                                    }
                                    else{
                                        Toast.makeText(context, "These credentials are too weak for Firebase! Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterDialog.this.getDialog().cancel();
            }
        });
    }

    public void addUserInfo() {
        String uid = mAuth.getCurrentUser().getUid();

        // create record
        //String profileImageUrl = "Not provided by authData";
        //String provider = mAuth.getCurrentUser().getProviderId();//.getProvider();

        //if(mAuth.getAuth().getProviderData().containsKey("profileImageURL")) profileImageUrl = mAuth.getAuth().getProviderData().get("profileImageURL").toString();

        // define users
        //DbUserInfo newUserInfo = new DbUserInfo(provider, email, profileImageUrl, displayName);
        //Firebase pushUser = mAuth.child("userInfo/users").push();
        //pushUser.setValue(newUserInfo);

        // define userMap
        //LoginActivity.populateUserMap(mAuth, uid, pushUser.getKey());
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("users/" + uid + "/profile/");

        Map<String, Object> user = new HashMap<String, Object>();
        user.put("name", displayName);
        user.put("yearOfBirth", yearOfBirth);

        profileRef.setValue(user);
        parent.setDisplayName(displayName);
        //return pushUser.getKey();
    }
}
