package com.example.draw4brains.main.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.model.Score;
import com.example.draw4brains.games.connectthedots.view.StatisticsPageActivity;
import com.example.draw4brains.main.controller.AdminController;
import com.example.draw4brains.main.controller.DatabaseMgr;
import com.example.draw4brains.main.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UsersListActivity extends AppCompatActivity {

    ArrayList<User> usersList = new ArrayList<User>();

    AdminController mAdminController;
    private ListView lvUsers;
    String newtext;
    FirebaseAuth fAuth;
    String admin_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();

        admin_email = fAuth.getCurrentUser().getEmail();
        Log.d("Admin's uidddddddddd", admin_email);

        lvUsers = (ListView) findViewById(R.id.lv_users);
        lvUsers.setEmptyView(findViewById(R.id.tv_empty));

        DatabaseMgr databaseMgr = new DatabaseMgr();

        databaseMgr.getUserScoreForList(UsersListActivity.this, lvUsers, new DatabaseMgr.callbackUserScoreForAdmin() {
            @Override
            public void onCallback(Score score, ArrayList<User> usersList, int position) {
                Intent intent = new Intent(UsersListActivity.this, StatisticsPageActivity.class);
                intent.putExtra("User Score", score);
                intent.putExtra("Name", usersList.get(position).getUserName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

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
                newtext = newText;
                Log.e("Main", " data search" + newText);
                mAdminController.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchView) {
            return true;
        }
        if (id == R.id.arrangebyalphabetical) {
            Collections.sort(usersList, (p1, p2) -> p1.getUserName().compareTo(p2.getUserName()));
            //mAdminController.clear();
            // mAdminController.addAll(User);
            mAdminController.notifyDataSetChanged();
            return true;
        } else {
//            Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
//            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * This function is used to shows a confirmation dialog to ask admin if he really wants to enable user
     * if yes , system will find user in database and enable the account
     * An email notification will be sent to user
     *
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
