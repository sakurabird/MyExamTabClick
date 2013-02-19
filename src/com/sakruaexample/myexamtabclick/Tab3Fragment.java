
package com.sakruaexample.myexamtabclick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Tab3Fragment extends Fragment {

    /*
     * (non-Javadoc)
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.tab3_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button = (Button) getView().findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tab3_1Fragment fragment = new Tab3_1Fragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ft.add(R.id.realtabcontent, fragment, "Tab3_1Fragment");
                ft.addToBackStack("Tab3_1Fragment");
                ft.commit();
            }
        });
    }
}
