package ru.denfad.cheackheart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;

public class GraphicFragment extends Fragment {

    public GraphicFragment(){}

    public static GraphicFragment newInstance(){
        return new GraphicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_fragment, container, false);

        final NeomorphFrameLayout ecgNeo = rootView.findViewById(R.id.ecg_button_neomorph);
        final NeomorphFrameLayout ppgNeo = rootView.findViewById(R.id.ppg_button_neomorph);
        ppgNeo.setShadowInner();

        Button ecgButton = rootView.findViewById(R.id.ecg_button);
        Button ppgButton = rootView.findViewById(R.id.ppg_button);

        loadFragment(PpgFragment.newInstance());

        ecgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecgNeo.setShadowInner();
                ppgNeo.setShadowNone();
                loadFragment(EcgFragment.newInstance());
            }
        });

        ppgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ppgNeo.setShadowInner();
                ecgNeo.setShadowNone();
                loadFragment(PpgFragment.newInstance());
            }
        });
        return rootView;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(R.id.fl_graph_content, fragment);
        ft.commit();
    }
}
