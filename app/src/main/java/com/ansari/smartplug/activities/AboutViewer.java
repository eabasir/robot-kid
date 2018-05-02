package com.ansari.smartplug.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ansari.smartplug.R;
import com.bumptech.glide.Glide;


public class AboutViewer extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, AboutViewer.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavUtils.navigateUpFromSameTask(AboutViewer.this);
                finish();
            }
        });


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.app_title_short));


        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.about_backdrop).centerCrop().into(imageView);
    }

    public void openWeb(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.temp.ir"));
        startActivity(browserIntent);
    }

    public void openAparat(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aparat.com/TGMPCO"));
        startActivity(browserIntent);
    }
    public void openGplus(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com/+TechParsm"));
        startActivity(browserIntent);
    }
    public void openFacbook(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tarashe.gostar"));
        startActivity(browserIntent);
    }
    public void openInstagram(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tgmp_robot/"));
        startActivity(browserIntent);
    }
    public void openTelegram(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/TGMPCO"));
        startActivity(browserIntent);
    }

    public void sendEmail(View view)
    {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"TGMPCO@gmail.com"});

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

}
