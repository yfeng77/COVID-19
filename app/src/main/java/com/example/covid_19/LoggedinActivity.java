package com.example.covid_19;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class LoggedinActivity extends Activity {

    ImageView imageView;
    TextView name, email;
    Button signOut;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.imagePersonView);
        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        signOut = findViewById(R.id.buttonSignout);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);

            // only works for local URI's
            // imageView.setImageURI(personPhoto);

            /*
            // use bitmap below
            Bitmap bitmap;
            String personPhotoStringURL=null;
            URL personPhotoURL;
            try {
                personPhotoStringURL = personPhoto.toString();
                personPhotoURL=new URL(personPhotoStringURL);
                bitmap = BitmapFactory.decodeStream((InputStream)personPhotoURL.getContent());
            }
            catch (Exception e) {
                System.out.println("Error: Unable to retrieve person image: "+personPhotoStringURL);
                System.out.println(e.toString());
                bitmap=null;
            }
            imageView.setImageBitmap(bitmap);
            */

            // use glide which has a bunch of config: https://github.com/bumptech/glide
            try {
                if (personPhoto != null)
                    Glide.with(this).load(personPhoto.toString()).into(imageView);
            } catch (Exception e)
            {
                System.out.println("Error: Unable to retrieve person image: "+personPhoto.toString());
            }

            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.buttonSignout:
                            signOut();
                            break;
                    }
                }
            });

        }

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LoggedinActivity.this,"Signed out successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }


}

