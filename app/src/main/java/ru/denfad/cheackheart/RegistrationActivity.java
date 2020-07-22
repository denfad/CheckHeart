package ru.denfad.cheackheart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;
import ru.denfad.cheackheart.services.Animator;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText age;
    private EditText login;
    private EditText password;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_layout);

        final Spinner spinner = findViewById(R.id.sex);
        SpinnerAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.sex));
        spinner.setAdapter(adapter);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        Button regist = findViewById(R.id.regist_button);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIsEmpty()) {
                    User user = new User();
                    user.setLogin(login.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setName(name.getText().toString());
                    user.setSex(getResources().getStringArray(R.array.sex)[(int) spinner.getSelectedItemId()]);
                    user.setAge(Integer.valueOf(age.getText().toString()));

                    Animator.buttonPress((NeomorphFrameLayout) findViewById(R.id.regist_button_neomorph));
                    NetworkService.getInstance()
                            .getJSONApi()
                            .registUser(user)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    Toast.makeText(getApplicationContext(), "Успешная регестрация", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    GsonBuilder gson = new GsonBuilder();
                                    editor.putString("user",gson.create().toJson(response.body())).apply();
                                    editor.putString("login",response.body().getLogin()).apply();
                                    editor.putString("password",response.body().getPassword()).apply();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                }
                else Toast.makeText(getApplicationContext(),"Заполните все поля",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkIsEmpty(){
        if(login.getText().toString().equals("") || password.getText().toString().equals("")||name.getText().toString().equals("")||age.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}
