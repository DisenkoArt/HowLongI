package com.disenkoart.howlongi.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.disenkoart.howlongi.customView.AboutView;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setListeners();
        setDeveloperView();
        shareButton.setTypeface(FontManager.UNI_SANS);
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
                String appPackageName = getContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    private void setDeveloperView(){
        developerView.setDeveloperImage(R.mipmap.ic_developer);
        developerView.setProfessionTitle(getResources().getString(R.string.profession_title_developer));
        developerView.setNameTitle(getResources().getString(R.string.name_title_developer));
        developerView.setRectangle(R.drawable.developer_rectangle);
    }
}
