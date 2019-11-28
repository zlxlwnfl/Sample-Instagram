package com.xnx.sample_instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button email_signInButton = (Button)findViewById(R.id.email_signUpButton);
        email_signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String email = ((EditText)findViewById(R.id.email_editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.password_editText)).getText().toString();
        String password_checked = ((EditText)findViewById(R.id.password_checked_editText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && password_checked.length() > 0) {
            if(password.length() > 7) {
                if (password.equals(password_checked)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        makeToast("회원가입에 성공하였습니다.");
                                        startActivity(MainActivity.class, false);
                                    } else {
                                        makeToast("회원가입에 실패하였습니다.");
                                    }
                                }
                            });
                } else {
                    makeToast("비밀번호가 일치하지 않습니다.");
                }
            } else {
                makeToast("비밀번호는 8자 이상 입력해 주십시오.");
            }
        } else {
            makeToast("이메일 또는 비밀번호를 입력해 주십시오");
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void startActivity(Class c, Boolean noHistory) {
        Intent intent = new Intent(this, c);
        if(noHistory)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
