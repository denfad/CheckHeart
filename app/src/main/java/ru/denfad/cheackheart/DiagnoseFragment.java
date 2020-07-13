package ru.denfad.cheackheart;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.ClientDiagnose;
import ru.denfad.cheackheart.models.Diagnose;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;
import ru.denfad.cheackheart.services.Animator;

public class DiagnoseFragment extends Fragment {

    private User user;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter adapter;
    private ListView list;

    public DiagnoseFragment(){}

    public static DiagnoseFragment newInstance(){
        return new DiagnoseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.diagnose_fragment, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        user = gson.fromJson(sharedPreferences.getString("user",""), User.class);

        Button add = rootView.findViewById(R.id.diagnose_add);
        final NeomorphFrameLayout frame = rootView.findViewById(R.id.diagnose_add_neomorph);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator.buttonPress(frame);
                AddDiagnoseDialog dialog = new AddDiagnoseDialog(getContext());
                dialog.show();
            }
        });

        list = rootView.findViewById(R.id.diagnose_list);

        NetworkService.getInstance()
                .getJSONApi()
                .getDiagnoses(user.getId())
                .enqueue(new Callback<List<ClientDiagnose>>() {
                    @Override
                    public void onResponse(Call<List<ClientDiagnose>> call, Response<List<ClientDiagnose>> response) {
                            adapter = new DiagnoseAdapter(getContext(), android.R.layout.simple_list_item_1, response.body());
                            list.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<ClientDiagnose>> call, Throwable t) {

                    }
                });

        return rootView;
    }

    public class DiagnoseAdapter extends ArrayAdapter<ClientDiagnose>{

        public DiagnoseAdapter(@NonNull Context context, int resource, @NonNull List<ClientDiagnose> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent){
            final ClientDiagnose diagnose = getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.diagnose_item,null);

            DiagnoseHolder holder = new DiagnoseHolder();
            holder.date = convertView.findViewById(R.id.diagnose_date);
            holder.diagnose = convertView.findViewById(R.id.diagnose);

            //форматирование даты
            String strDate = diagnose.getDate();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar();
            try {
                calendar.setTime(format.parse(strDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.date.setText(calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())+" "+calendar.get(Calendar.YEAR));
            holder.diagnose.setText(diagnose.getDiagnose());

            convertView.setTag(holder);
            return  convertView;
        }
    }

    private class DiagnoseHolder{
        public TextView date;
        public TextView diagnose;
    }

    class AddDiagnoseDialog extends Dialog{


        public AddDiagnoseDialog(@NonNull Context context) {
            super(context);

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.FILL_PARENT;
            getWindow().setAttributes( params);
            this.setContentView(R.layout.add_diagnose_dialog);

            final EditText diagnoseText = findViewById(R.id.diagnose);
            final CalendarView calendarView = findViewById(R.id.calendarView);

            final Calendar calendar = new GregorianCalendar();
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    calendar.set(year,month,dayOfMonth);
                }
            });

            Button b = findViewById(R.id.diagnose_add_dialog);
            final NeomorphFrameLayout neo = findViewById(R.id.diagnose_add_dialog_neomorph);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animator.buttonPress(neo);
                    AddDiagnoseDialog.this.cancel();
                    Diagnose diagnose = new Diagnose();
                    diagnose.setDiagnose(diagnoseText.getText().toString());
                    NetworkService.getInstance()
                            .getJSONApi()
                            .saveDiagnoses(user.getId(),diagnose,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH))
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                }
                            });
                    NetworkService.getInstance()
                            .getJSONApi()
                            .getDiagnoses(user.getId())
                            .enqueue(new Callback<List<ClientDiagnose>>() {
                                @Override
                                public void onResponse(Call<List<ClientDiagnose>> call, Response<List<ClientDiagnose>> response) {
                                    adapter = new DiagnoseAdapter(getContext(),android.R.layout.simple_list_item_1,response.body());
                                    list.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<List<ClientDiagnose>> call, Throwable t) {

                                }
                            });
                }
            });
        }
    }
}

