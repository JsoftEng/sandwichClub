package com.github.jsofteng.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.github.jsofteng.sandwichclub.model.Sandwich;
import com.github.jsofteng.sandwichclub.utils.JsonUtils;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    TextView mSandwichOrigin;
    TextView mSandwichAlsoKnownAs;
    TextView mSandwichIngredients;
    TextView mSandwichDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mSandwichOrigin = findViewById(R.id.origin_tv);
        mSandwichDescription = findViewById(R.id.description_tv);
        mSandwichAlsoKnownAs = findViewById(R.id.also_known_tv);
        mSandwichIngredients = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        try {
            Sandwich sandwich = JsonUtils.parseSandwichJson(json);

            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            populateUI(sandwich);
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .into(ingredientsIv);

            setTitle(sandwich.getMainName());
        }catch(IOException ioe){
            closeOnError();
            return;
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    //Populate views, set invisible if data does not exist
    private void populateUI(Sandwich sandwich) {
        if (sandwich.getPlaceOfOrigin() != null){
            mSandwichOrigin.setText(sandwich.getPlaceOfOrigin());
            mSandwichOrigin.setVisibility(View.VISIBLE);
        }else{
            mSandwichOrigin.setVisibility(View.INVISIBLE);
        }

        if(sandwich.getDescription() != null){
            mSandwichDescription.setText(sandwich.getDescription());
            mSandwichDescription.setVisibility(View.VISIBLE);
        }else{
            mSandwichDescription.setVisibility(View.INVISIBLE);
        }

        if (sandwich.getIngredients() != null){
            mSandwichIngredients.setVisibility(View.VISIBLE);
            mSandwichIngredients.setText(android.text.TextUtils.join(",",sandwich.getIngredients()));
        }else{
            mSandwichIngredients.setVisibility(View.INVISIBLE);
        }
        if (sandwich.getAlsoKnownAs() != null){
            mSandwichAlsoKnownAs.setVisibility(View.VISIBLE);
            mSandwichAlsoKnownAs.setText(android.text.TextUtils.join(",",sandwich.getAlsoKnownAs()));
        }else{
            mSandwichAlsoKnownAs.setVisibility(View.INVISIBLE);
        }
    }
}
