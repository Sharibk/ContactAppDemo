package com.example.contactappdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView lisyView;
    private CAdapter adapter;
    private Cursor mCursor;
    private TextView textView;
    public static final int RequestPermissionCode = 1;


    public void askPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                textView.setText("Grant Permission To Read Contacts");


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        RequestPermissionCode);
                textView.setText("Grant Permission To Read Contacts");
            }

        } else {
            getSupportLoaderManager().initLoader(0, null, this);

        }




    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                    getSupportLoaderManager().initLoader(0, null, this);
                } else {
                    textView.setText("Grant Permission To Read Contacts");

                    Toast.makeText(MainActivity.this, "Grant Permission To Read Contacts", Toast.LENGTH_LONG).show();

                }
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lisyView = findViewById(R.id.contactlist);
        adapter = new CAdapter(this, mCursor);
        textView = findViewById(R.id.textView2);

        lisyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent contactdetail = new Intent(getApplicationContext(),ContactDetails.class);

                String name = ((TextView) view.findViewById(R.id.textView)).getText().toString();

             contactdetail.putExtra("name",name);
             startActivity(contactdetail);
            }
        });



        askPermission();


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {


        return new CursorLoader(
                getApplicationContext(),

                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC"
        );
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        cursor.moveToFirst();
        adapter = new CAdapter(this, cursor);
        lisyView.setAdapter(adapter);
        textView.setText("Local Contacts : " +mCursor.getCount());


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    public class CAdapter extends BaseAdapter {
        Cursor cursor;
        Context  mContext;
        LayoutInflater inflater;

        public CAdapter (Context context, Cursor cursor) {
            mContext = context;
            this.cursor = cursor;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            cursor.moveToPosition(position);
            if (view == null) {
                view = inflater.inflate(R.layout.contactslistui, parent,
                        false);
                holder = new Holder();
                holder.contactName = view
                        .findViewById(R.id.textView);
                holder.pP = view.findViewById(R.id.profile_image);

                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            String nme = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            holder.contactName.setText(nme);

            String imageuri  = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));


           try{
               if(imageuri!=null){
                   holder.pP.setImageURI(Uri.parse(imageuri));
               }else {
                   holder.pP.setImageDrawable(getResources().getDrawable(R.drawable.user));
               }

           }catch (Exception e){
               holder.pP.setImageDrawable(getResources().getDrawable(R.drawable.user));
               Log.i("Exception",e.toString());
           }



            return view;
        }

        class Holder {
            TextView contactName;
            CircleImageView pP;

        }


    }

}


