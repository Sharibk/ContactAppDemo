package com.example.contactappdemo;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetails extends AppCompatActivity {

    private String name;
    private TextView tName;
    private CircleImageView pP;
    private TextView num;
    private TextView email;
    private TextView location;




    public void id_return(String name) {
        String id =null;

        Uri resultUri = ContactsContract.Contacts.CONTENT_URI;

        Cursor cont = getContentResolver().query(resultUri, null, null, null, null);

        String selection = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?" ;

        String[] sectionargs = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,name};

        Cursor nameCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, selection,sectionargs , ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

        while (nameCur.moveToNext()) {
            id  = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID));}
        nameCur.close();
        cont.close();
        nameCur.close();

        showinformation(id);
    }


    public void showinformation(String  id) {

        String tphone = "";
        String temail = "";
        String tpostal = "";


        Uri uri1 = ContactsContract.Contacts.CONTENT_URI;
        Uri uri2 = ContactsContract.Data.CONTENT_URI;
        Cursor cont = getContentResolver().query(uri1, null, null, null, null);
        String selection = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID+  " = ? " ;
if(selection!=null){
    try{



        String[] sarg2 = new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,id};
        if(sarg2!=null){
            Cursor nameCur2 = getContentResolver().query(uri2, null, selection, sarg2, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
            while (nameCur2.moveToNext()) {
                tphone = nameCur2.getString(nameCur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            nameCur2.close();
            cont.close();
            nameCur2.close();
        }
        String[] sarg3 = new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,id};

        if(sarg3!=null){
            Cursor nameCur3 = getContentResolver().query(uri2, null, selection, sarg3, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
            while (nameCur3.moveToNext()) {
                temail = nameCur3.getString(nameCur3.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));}
            nameCur3.close();
            cont.close();
            nameCur3.close();
        }
        String[] sarg4 = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,id};

        if(sarg4!=null){
            Cursor nameCur4 = getContentResolver().query(uri2, null, selection, sarg4, ContactsContract.CommonDataKinds.StructuredPostal.DATA);
            while (nameCur4.moveToNext()) {
                tpostal = nameCur4.getString(nameCur4.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));}
            nameCur4.close();
            cont.close();
            nameCur4.close();
        }

        String[] sarg5 = new String[] { ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,id};

        if(sarg5!=null ){
            Cursor nameCur5 = getContentResolver().query(uri2, null, selection, sarg5, ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            while (nameCur5.moveToNext()) {
                String imageuri  = nameCur5.getString(nameCur5
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                try{
                    if (imageuri != null){
                        Log.i("Exception",imageuri);
                        pP.setImageURI(Uri.parse(imageuri));
                    }else {

                    }


                }catch (Exception e){
                    pP.setImageDrawable(getResources().getDrawable(R.drawable.user));
                    Log.i("Exception",e.getLocalizedMessage());
                }
            }
            nameCur5.close();
            cont.close();
            nameCur5.close();

        }

    }catch (Exception e){
        Log.i("Exception",e.getLocalizedMessage());
    }

}




        tName.setText(name.toUpperCase());
        num.setText(tphone);
        email.setText(temail);
        location.setText(tpostal);



    }




    public void getdata(){
        Intent get = getIntent();
        name = get.getStringExtra("name");
        if(name.matches("")){
            Toast.makeText(this, "Kindly Add The Name To Contact", Toast.LENGTH_SHORT).show();
        }else {
            id_return(name);
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_contact_details);

        tName = findViewById(R.id.name);
        pP = findViewById(R.id.imageView);
        num = findViewById(R.id.num);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);

        getdata();


    }
}
