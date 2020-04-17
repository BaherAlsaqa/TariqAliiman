package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Models.books.Book;
import com.tariqaliiman.tariqaliiman.Models.books.BooksData;
import com.tariqaliiman.tariqaliiman.R;

import java.io.Console;

public class BookDetails extends AppCompatActivity {

    TextView bookTitle, autherName, bookDescription;
    ImageView bookImage, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        bookTitle = findViewById(R.id.booktitle);
        bookDescription = findViewById(R.id.bookdescription);
        autherName = findViewById(R.id.authername);
        bookImage = findViewById(R.id.bookimage);
        back = findViewById(R.id.back);

        final Intent intent = getIntent();

        if (intent != null){
//            Book book = intent.getParcelableExtra(Constants.item);
            Picasso.get().load(Constants.imageURL+intent.getStringExtra(Constants.bimage)).into(bookImage);
            bookTitle.setText(intent.getStringExtra(Constants.btitle));
            bookDescription.setText(intent.getStringExtra(Constants.bdescription));
            autherName.setText(intent.getStringExtra(Constants.bauther));

            bookImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BookDetails.this, ZoomInZoomOut.class)
                    .putExtra(Constants.imageViewZoom, Constants.imageURL+intent.getStringExtra(Constants.bimage)));
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }
}
