package com.project.eugene.imgurapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Remember to implement  GalleryAdapter.GalleryAdapterCallBacks to activity  for communication of Activity and Gallery Adapter
public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<GalleryItem> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    GalleryAdapter mGalleryAdapter;

    public void post(String path) {


        List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);
        postContent.add(new BasicNameValuePair("key", DEV_KEY));
        postContent.add(new BasicNameValuePair("image", path));

        // Great! Now you can get started with the API!
        //       For public read-only and anonymous resources, such as getting image info, looking up user comments, etc.
        //       all you need to do is send an authorization header with your client_id in your requests.
        //      This also works if you'd like to upload images anonymously (without the image being tied to an account),
        //  or if you'd like to create an anonymous album. This lets us know which application is accessing the API.

        //  Authorization: Client-ID YOUR_CLIENT_ID

       // For accessing a user's account, please visit the OAuth2 section of the docs.

        //Client ID:
        //
        //fa8fafb2c20e039
        //
        //Client secret:
        //
        //077a5e4255eb9cd2eed35e56554cc7637f93a30c

        String url = "imgur.com/api/upload.xml"; //its tru URl. its fact
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < postContent.size(); index++) {
                if(postContent.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(postContent.get(index).getName(), new FileBody(new File(postContent.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(postContent.get(index).getName(), new StringBody(postContent.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            mImgurResponse = parseResponse (response);


            Iterator it = mImgurResponse.entrySet().iterator();
            while(it.hasNext()){
                HashMap.Entry pairs = (HashMap.Entry)it.next();

                Log.i("INFO",pairs.getKey().toString());
                if(pairs.getValue()!=null){
                    reviewEdit.setText(pairs.getValue().toString());

                    Log.i("INFO",pairs.getValue().toString());
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String,String> parseResponse(HttpResponse response) {
        String xmlResponse = null;

        try {
            xmlResponse = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (xmlResponse == null) return null;

        HashMap<String, String> ret = new HashMap<String, String>();
        ret.put("error", getXMLElementValue(xmlResponse, "error_msg"));
        ret.put("delete", getXMLElementValue(xmlResponse, "delete_page"));
        ret.put("original", getXMLElementValue(xmlResponse, "original_image"));

        return ret;
    }

    private String getXMLElementValue(String xml, String elementName) {
        if (xml.indexOf(elementName) >= 0)
            return xml.substring(xml.indexOf(elementName) + elementName.length() + 1,
                    xml.lastIndexOf(elementName) - 2);
        else
            return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //This is toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Album");
        setSupportActionBar(toolbar);



        //setup RecyclerView
        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));

        recyclerViewGallery.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        //Create RecyclerView Adapter
        mGalleryAdapter = new GalleryAdapter(this);

        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);

        //check for read storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Get images
            galleryItems = GalleryUtils.getImages(this);
            // add images to gallery recyclerview using adapter
            mGalleryAdapter.addGalleryItems(galleryItems);
        } else {
            //request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }


    }


    @Override
    public void onItemSelected(int position) {
        //create fullscreen SlideShowFragment dialog
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        //setUp style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally show dialogue
        slideShowFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Get images
                galleryItems = GalleryUtils.getImages(this);
                // add images to gallery recyclerview using adapter
                mGalleryAdapter.addGalleryItems(galleryItems);
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
