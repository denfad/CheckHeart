package ru.denfad.cheackheart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;

public class ProfileFragment extends Fragment {

    private TextView age, sex, height, weight;
    private Switch saveData;
    private SharedPreferences mSharedPreferences;
    private User user;
    private Gson gson;

    public ProfileFragment(){}

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        age = rootView.findViewById(R.id.profile_age);
        sex = rootView.findViewById(R.id.profile_sex);
        height = rootView.findViewById(R.id.profile_height);
        weight = rootView.findViewById(R.id.profile_weight);
        saveData = rootView.findViewById(R.id.profile_save_data_switch);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userStr = mSharedPreferences.getString("user","");
        gson = new Gson();
        user = gson.fromJson(userStr,User.class);

        saveData.setChecked(user.getSaveData());

        saveData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                user.setSaveData(isChecked);
                updateUser();
            }
        });
        fill();
        return rootView;
    }


    private void fill(){
        age.setText(user.getAge().toString());
        sex.setText(user.getSex());
        if(user.getHeight()!=null) height.setText(user.getHeight() + " см");
        else height.setText("Рост не задан");
        if(user.getWeight()!=null) weight.setText(user.getWeight() + " кг");
        else weight.setText("Вес не задан");
    }

    private void updateUser(){
        mSharedPreferences.edit().putString(gson.toJson(user),"").apply();
        NetworkService.getInstance()
                .getJSONApi()
                .updateUser(user)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }
}
