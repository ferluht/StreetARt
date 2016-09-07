package eu.kudan.ar;

import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor;

    /*static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        /*Random rnd = new Random();*/
        backColor = Color.WHITE;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        switch (pageNumber){
            case 0:
                view = inflater.inflate(R.layout.welcome_view, null);
                break;
            case 1:
                view = inflater.inflate(R.layout.pick_marker_view, null);
                break;
            default:
                view = inflater.inflate(R.layout.welcome_view, null);
                TextView tvPage = (TextView) view.findViewById(R.id.tvPage);
                tvPage.setText("Page " + pageNumber);
                tvPage.setBackgroundColor(backColor);
                break;
        }

        return view;
    }*/
}
