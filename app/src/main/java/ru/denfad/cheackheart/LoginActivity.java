package ru.denfad.cheackheart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;
import ru.denfad.cheackheart.services.Animator;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button loginButton;
    private Button regist;
    private NeomorphFrameLayout frame;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(mSharedPreferences.getString("password",null)!=null & mSharedPreferences.getString("login",null)!= null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        frame = findViewById(R.id.login_button_neomorph);
        regist = findViewById(R.id.not_register);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator.buttonPress(frame);
                NetworkService.getInstance()
                    .getJSONApi()
                    .authUser(login.getText().toString(),password.getText().toString())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(getApplicationContext(),"Вы успешно вошли!",Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            GsonBuilder gson = new GsonBuilder();
                            editor.putString("user",gson.create().toJson(response.body())).apply();
                            editor.putString("login",response.body().getLogin()).apply();
                            editor.putString("password",response.body().getPassword()).apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Неверный логин или пароль",Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

}
