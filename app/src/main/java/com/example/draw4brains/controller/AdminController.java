package com.example.draw4brains.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.model.User;

import java.util.ArrayList;
public class AdminController extends ArrayAdapter<User> implements Filterable {


    ArrayList<User> UsersTotal;
    ArrayList<User> Usersfiltered;
    LayoutInflater inflater;


    /**
     * initialize the ArrayAdapter's internal storage for the context and the list.
     * @param context current state of the application
     * @param Users array list of users
     */
    public AdminController(Activity context, ArrayList<User> Users) {
        // initialize the ArrayAdapter's internal storage for the context and the list.
        super(context, 0, Users);


        Usersfiltered = Users;
        UsersTotal = Users;
        inflater = LayoutInflater.from(context);




    }

    @Override
    public int getCount() {
        return Usersfiltered.size();
    }

    @Override
    public User getItem(int position) {
        return Usersfiltered.get(position);
    }

    @Override
    public long getItemId(int position) {

        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (UsersTotal == null)
        {
            itemID = position;
        }
        else
        {
            itemID = UsersTotal.indexOf(Usersfiltered.get(position));
        }
        return itemID;
    }

    @Override
    public View getView (final int position,
                         View convertView,
                         ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout_in_list,parent, false);
        }

        ((TextView) convertView.findViewById(R.id.textView_userEmail)).setText(getItem(position).getEmailAddress());
        ((TextView) convertView.findViewById(R.id.textView_userName)).setText(getItem(position).getUserName());

        return convertView;

    }








//implement filter logic

    /**
     * This function is used to search for the string entered by the user among the list of users.
     * If this string is a substring of any of the users in the array list,
     * the system will return all the users that match this string in a list
     * @return all users that are a match with the entered string in the search bar
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<User> results = new ArrayList<User>();
                if (UsersTotal == null) {
                    UsersTotal = new ArrayList<User>(Usersfiltered); // saves the original data in mOriginalValues
                }
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = UsersTotal.size();
                    filterResults.values = UsersTotal;

                }else{

                    String searchStr = constraint.toString().toLowerCase();

                    for(User user:UsersTotal){
                        if(user.getUserName().toLowerCase().contains(searchStr) || user.getEmailAddress().toLowerCase().contains(searchStr)){
                            results.add(user);

                        }
                    }
                    filterResults.count = results.size();
                    filterResults.values = results;


                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Usersfiltered = (ArrayList<User>) results.values;

                notifyDataSetChanged();

            }
        };
        return filter;
    }



}
