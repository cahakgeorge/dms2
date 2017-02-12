package com.daniellasmontesssorischool.dms;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLanding extends Fragment {
    Button student, teacher;

    public FragmentLanding() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_landing, container, false);

        student = (Button)rootView.findViewById(R.id._signin_student);
        teacher = (Button)rootView.findViewById(R.id._signup_teacher);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandingPage)getActivity()).logintype="student";
                ((LandingPage)getActivity()).pref.edit()
                        .putString("LoginType", "student").commit();

                if(((LandingPage)getActivity()).pref.contains("pref_prevlogin")) {
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                    getActivity().finish();

                }else{
                    Fragment frag = new FragmentLogin();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slideleftenter, 0);
                    fragmentTransaction.replace(R.id._frame_select, frag, "fraglogin");
                    fragmentTransaction.commit();
                }

            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandingPage)getActivity()).logintype="teacher";
                ((LandingPage)getActivity()).pref.edit()
                        .putString("LoginType", "teacher").commit();

                if(((LandingPage)getActivity()).pref.contains("pref_prevlogin_teacher")) {
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                    getActivity().finish();

                }else {

                    Fragment frag = new FragmentLoginTeachers();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slideleftenter, 0);
                    fragmentTransaction.replace(R.id._frame_select, frag, "fragloginteacher");
                    fragmentTransaction.commit();
                }
            }
        });

        return rootView;
    }

}
