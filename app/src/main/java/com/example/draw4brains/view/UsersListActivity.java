package com.example.draw4brains.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.AdminController;
import com.example.draw4brains.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UsersListActivity extends AppCompatActivity {
    ArrayList<com.example.draw4brains.model.User> User=new ArrayList<User>();

    AdminController mAdminController;
    private ListView lvUsers;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    String newtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initialize the views
        lvUsers = (ListView) findViewById(R.id.lv_users);
        lvUsers.setEmptyView(findViewById(R.id.tv_empty));

        int score = 1;
        User.add(new User("Ostrich","gender","phone","email1","caretaker",false,score));
        User.add(new User("Flamingo","gender","phone","email2","caretaker",false,score));
        User.add(new User("Sparrow","gender","phone","email3","caretaker",false,score));
        User.add(new User("Rooster","gender","phone","email4","caretaker",false,score));
        User.add(new User("Rtest","gender","phone","email4","wrong",false,score));
        //TODO can also filter as you add to User
        for(int i=0;i<User.size();i++){
            if(User.get(i).getCaretaker_email()!="caretaker"){
                User.remove(i);
            }
        }



        mAdminController = new AdminController(UsersListActivity.this,User);
                lvUsers.setAdapter(mAdminController);
                lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent = new Intent(UsersListActivity.this, StatisticsPageActivity.class);
                        intent.putExtra("Name",mAdminController.getItem(position).getUserName());
                        intent.putExtra("Score",String.valueOf(mAdminController.getItem(position).getscore()));
                        Log.d("intent", String.valueOf(intent.getStringExtra("Name")));
                        Log.d("intent", String.valueOf(intent.getStringExtra("Score")));
                        startActivity(intent);
                        //showEnableDialog(mAdminController.getItemId(position));

                    }
                });

//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference usersdRef = rootRef.child("Users");
//        //get list of users, making sure that they aren't already disabled, an admin, or a clinic admin
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//TODO: filter out all the older people not related to this caretaker, which means do not have to implement filter out caretaking seniors
        //TODO already filtered at the first start

//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String uid = ds.getKey();
//                    Log.d("TAG", uid);
//                    String email=ds.child("email").getValue(String.class);
//                    String name=ds.child("fullName").getValue(String.class);

        //       //TODO             String caretakeremail=ds.child("caretaker").getValue(String.class);
        //      //   TODO  if(caretakeremail == authenticated one){
//                        User.add(new User());
//                    }

//                    Boolean isDisabled = ds.child("disabled").getValue(Boolean.class);
//                    Boolean isAdmin = ds.child("admin").getValue(Boolean.class);
//                    if(isDisabled==null){
//                        isDisabled = ds.child("Disabled").getValue(Boolean.class);
//                    }
//                    if(isAdmin==null){
//                        isAdmin = ds.child("Admin").getValue(Boolean.class);
//                    }
//
//                    if(isAdmin==false && isDisabled==true){
//                        User.add(new User(name,email,0, "nil",uid,true,false,false,"nil","nil"));
//                    }
//
//                }
//
//                mAdminController = new AdminController(AdminLoginActivity.this,User);
//                listView.setAdapter(mAdminController);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//                        showEnableDialog(mAdminController.getItemId(position));
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
        //usersdRef.addListenerForSingleValueEvent(eventListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();

        MenuItem alph = menu.findItem(R.id.arrangebyalphabetical);
        alph.setVisible(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newtext=newText;

                Log.e("Main"," data search"+newText);

                mAdminController.getFilter().filter(newText);



                return true;
            }
        });


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.searchView){

            return true;
        }
        if(id==R.id.arrangebyalphabetical) {

            Collections.sort(User, (p1, p2) -> p1.getUserName().compareTo(p2.getUserName()));
            //mAdminController.clear();
           // mAdminController.addAll(User);
            mAdminController.notifyDataSetChanged();

           return true;
        }
        else{
//            Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
//            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * This function is used to shows a confirmation dialog to ask admin if he really wants to enable user
     * if yes , system will find user in database and enable the account
     * An email notification will be sent to user
     * @param position index of user to be deleted
     */
    private void showEnableDialog(final long position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("implement Tag user and statistics page");
        //finds user in database and enables account
        builder.setPositiveButton("Tag", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //enable user and refresh page
//                DatabaseReference UserToUpdate = FirebaseDatabase.getInstance().getReference("Users").child((User.get(((int)position))).getUid());
//                HashMap<String, Object> map = new HashMap<>();
//                FirebaseUser firebaseUser = fAuth.getCurrentUser();
//                String caretaker_email = fAuth.getCurrentUser().getEmail();
//                map.put("caretaker_email", caretaker_email );
//
//
//
//
//                String username =User.get(((int)position)).getUserName();
//                Log.d("username", username);
//                String useremail=User.get(((int)position)).getEmailAddress();
//                Log.d("email", useremail);
////                EnableDeletedUser enableDeletedUser = new EnableDeletedUser();
////                enableDeletedUser.sendEnableEmail( useremail, username);
//                UserToUpdate.updateChildren(map);
//                mAdminController.remove(User.get((int) position));
//                mAdminController.getFilter().filter(newtext);



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *  Return back to Admin homepage
     */
//    @Override
//    public void onBackPressed() {
//        Intent myIntent = new Intent(getApplicationContext(), AdminHomeActivity.class);
//        startActivity(myIntent);
//        super.onBackPressed();
//    }


}
