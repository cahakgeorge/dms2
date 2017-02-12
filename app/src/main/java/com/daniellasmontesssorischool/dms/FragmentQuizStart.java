package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuizStart extends Fragment {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    TextView _quiztv, optv1, optv2, optv3, optv4, qnumber,outputtext;
    LinearLayout _opt1, _opt2, _opt3, _opt4;
    View rootView;

    String quest;
    String option1, option2, option3, option4, ans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (((Home)getActivity()).quiztype == "challenge")
            rootView = inflater.inflate(R.layout.fragment_quizstart, container, false);
        else
            rootView = inflater.inflate(R.layout.fragment_quizstart, container, false);

        //generate and populate the quiz
        _quiztv = (TextView) rootView.findViewById(R.id.question);
        _opt1 = (LinearLayout) rootView.findViewById(R.id.lay1);
        _opt2 = (LinearLayout) rootView.findViewById(R.id.lay2);
        _opt3 = (LinearLayout) rootView.findViewById(R.id.lay3);
        _opt4 = (LinearLayout) rootView.findViewById(R.id.lay4);

        optv1 = (TextView) rootView.findViewById(R.id.opt1);
        optv2 = (TextView) rootView.findViewById(R.id.opt2);
        optv3 = (TextView) rootView.findViewById(R.id.opt3);
        optv4 = (TextView) rootView.findViewById(R.id.opt4);

        qnumber = (TextView) rootView.findViewById(R.id.qnumber);
        outputtext = (TextView) rootView.findViewById(R.id.outputtext);


        setQuestions();
        _opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set animation and check for correct answer;
                validateAnswer(optv1.getText().toString(), 1);
            }
        });

        _opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set animation and check for correct answer;
                validateAnswer(optv2.getText().toString(), 2);
            }
        });

        _opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set animation and check for correct answer;
                validateAnswer(optv3.getText().toString(), 3);
            }
        });

        _opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set animation and check for correct answer;
                validateAnswer(optv4.getText().toString(), 4);
            }
        });

        return rootView;
    }

    private void setQuestions(){
        _quiztv.setText(((Home)getActivity()).quiz[((Home)getActivity()).quizcount][0]);
        optv1.setText(((Home)getActivity()).quiz[((Home)getActivity()).quizcount][1]);
        optv2.setText(((Home)getActivity()).quiz[((Home)getActivity()).quizcount][2]);
        optv3.setText(((Home)getActivity()).quiz[((Home)getActivity()).quizcount][3]);
        optv4.setText(((Home)getActivity()).quiz[((Home)getActivity()).quizcount][4]);

        ans = ((Home)getActivity()).quiz[((Home)getActivity()).quizcount][5];
        option1 = ((Home)getActivity()).quiz[((Home)getActivity()).quizcount][1];
        option2 = ((Home)getActivity()).quiz[((Home)getActivity()).quizcount][2];
        option3 = ((Home)getActivity()).quiz[((Home)getActivity()).quizcount][3];
        option4 = ((Home)getActivity()).quiz[((Home)getActivity()).quizcount][4];

        int i = ((Home)getActivity()).quizcount+1;
        qnumber.setText("Question "+ i + "/14");
    }

    private void validateAnswer(final String chosenAns, final int pos) {

                if (!chosenAns.equalsIgnoreCase(ans)) {
                    setWrongText();
                    animatewrong(pos);
                }

        if (chosenAns.equalsIgnoreCase(ans)) {
            setRightText();
            animatecorrect(pos);
            ((Home)getActivity()).scorecount += 10;
        } else if (ans.equalsIgnoreCase(option1) && !option1.equalsIgnoreCase(chosenAns))
            animatecorrect(1);
        else if (ans.equalsIgnoreCase(option2) && !option2.equalsIgnoreCase(chosenAns))
            animatecorrect(2);
        else if (ans.equalsIgnoreCase(option3) && !option3.equalsIgnoreCase(chosenAns))
            animatecorrect(3);
        else if (ans.equalsIgnoreCase(option4) && !option4.equalsIgnoreCase(chosenAns))
            animatecorrect(4);


        if (((Home) getActivity()).quizcount != 13){
            ((Home)getActivity()).quizcount++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Fragment frag = new FragmentQuizStart();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.popup_enter, 0);
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                    fragmentTransaction.commit();
                }
            }, SPLASH_TIME_OUT);
        }else{
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("Quiz Result")
                    .content("You scored "+((Home)getActivity()).scorecount + "/140")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();

        }
    }

    private void setWrongText(){
        String[] wrong_array = new String[]{"Hmmmn! Wrong", "Wrong answer", "C'mon, Try again!", "Ooops, try harder"};
        //Display wrong text
        Random rand = new Random(5);
        int randint = rand.nextInt(3);
        String wrText = wrong_array[randint];

        //Now set the Text
        outputtext.setText(wrText);
    }
    private void setRightText(){
        String[] right_array = new String[]{"Right", "Correct", "Nice One!", "Good, Keep it up"};
        //Display right text
        Random rand = new Random(5);
        int randint = 0 + rand.nextInt(3);
        String rtText = right_array[randint];

        //Now set the text
        outputtext.setText(rtText);
    }

    private void animatecorrect(int position){
        //final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.grow);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(700); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        animation.setBackgroundColor(getResources().getColor(R.color.LightGreen));

        if(position == 1){
            _opt1.setBackgroundColor(getResources().getColor(R.color.green));
            _opt1.startAnimation(animation);
        }else if(position == 2){
            _opt2.setBackgroundColor(getResources().getColor(R.color.green));
            _opt2.startAnimation(animation);
        }else if(position == 3){
            _opt3.setBackgroundColor(getResources().getColor(R.color.green));
            _opt3.startAnimation(animation);
        } else if(position == 4){
            _opt4.setBackgroundColor(getResources().getColor(R.color.green));
            _opt4.startAnimation(animation);
        }

       /* if(position == 1){
            _opt1.setBackgroundColor(getResources().getColor(R.color.green));
        }else if(position == 2){
            _opt2.setBackgroundColor(getResources().getColor(R.color.green));
        }else if(position == 3){
            _opt3.setBackgroundColor(getResources().getColor(R.color.green));
        } else if(position == 4){
            _opt4.setBackgroundColor(getResources().getColor(R.color.green));
        }*/

        //after animating the correct answer, load next question
    }

    private void animatewrong(int position){
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(400); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        animation.setBackgroundColor(getResources().getColor(R.color.dmsPink));

        if(position == 1){
            _opt1.setBackgroundColor(getResources().getColor(R.color.dmsPink));
            _opt1.startAnimation(animation);
        }else if(position == 2){
            _opt2.startAnimation(animation);
            _opt2.setBackgroundColor(getResources().getColor(R.color.dmsPink));
        }else if(position == 3){
            _opt3.setBackgroundColor(getResources().getColor(R.color.dmsPink));
            _opt3.startAnimation(animation);
        } else if(position == 4){
            _opt4.setBackgroundColor(getResources().getColor(R.color.dmsPink));
            _opt4.startAnimation(animation);
        }

    }



    //OR TRY this

    /*new Thread(){
        public void run(){
            //sleep(5000);
            //refreshSthHere();
        }
    }.start();*/
}
