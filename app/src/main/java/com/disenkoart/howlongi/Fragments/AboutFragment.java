package com.disenkoart.howlongi.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.disenkoart.howlongi.CustomView.AboutView;
import com.disenkoart.howlongi.Data.GradientColor;
import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 16.08.2016.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.developer_about_view)
    AboutView developerView;

    @BindView(R.id.designer_about_view)
    AboutView designerView;

    @BindView(R.id.about_view_share_button)
    Button shareButton;



    @BindView(R.id.profession_title_about_view)
    TextView developerProfessionTitle;

    @BindView(R.id.name_title_about_view)
    TextView develoerNameTitle;

    @BindView(R.id.image_about_view)
    ImageView developerImage;

    @BindView(R.id.rectangle_image_about_view)
    ImageView developerRectangleImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(rootView);
        setListeners();
        setDeveloperView();
        return rootView;
    }

    public void setListeners(){
        developerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent action = new Intent(Intent.ACTION_VIEW);
                action.setData(Uri.parse(getResources().getString(R.string.developer_url)));
                startActivity(action);
            }
        });
        designerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent action = new Intent(Intent.ACTION_VIEW);
                action.setData(Uri.parse(getResources().getString(R.string.designer_url)));
                startActivity(action);
            }
        });

        shareButton.setTypeface(FontManager.UNI_SANS);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.share_url)));
                startActivity(intent);
            }
        });
    }

    private void setDeveloperView(){
        developerView.setDeveloperImage(R.mipmap.ic_developer);
        developerView.setProfessionTitle(getResources().getString(R.string.profession_title_developer));
        developerView.setNameTitle(getResources().getString(R.string.name_title_developer));
        developerView.setRectangleColor(new GradientColor(-1, getResources().getColor(R.color.developer_view_start_color), getResources().getColor(R.color.developer_view_end_color)));
    }
}
