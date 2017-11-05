package com.bank.bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;
import com.bank.message.Message;
import com.bank.message.MessageImpl;

import java.util.List;

public class MessageSelectActivity extends AppCompatActivity {
  int id;
  MessageImpl[] messageArray;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // get the id from the intent, and set initial value to -1 for errors
    setContentView(R.layout.activity_message_select);
    Intent context = getIntent();
    id = context.getIntExtra("id", -1);
  }

  @Override
  protected void onResume() {
    super.onResume();
    // create a list of messages and message implementations
    List<Message> myList = DatabaseDriverAndroidHelper.getInstance().getOneUserAllMessages(id);
    messageArray = new MessageImpl[myList.size()];
    int i = 0;
    for (Message m : myList) {
      // interate through the messages and add them to the array
      messageArray[i] = (MessageImpl) m;
      i++;
    }
    // set up a list view to display all messages, making them clickable to interact with
    ListView lst = (ListView) findViewById(R.id.messageSelectListView);
    final CustomListView customListView = new CustomListView(this, messageArray);
    lst.setAdapter(customListView);
    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // create a new activity that sends in an extra with the chosen message
        Intent intent = new Intent(MessageSelectActivity.this, ReadMessageActivity.class);
        intent.putExtra("message", messageArray[position]);
        startActivity(intent);
      }
    });
  }

  private void updateMessages() {
    // iterate through the messages updating them with any changes that occured
    List<Message> myList = DatabaseDriverAndroidHelper.getInstance().getOneUserAllMessages(id);
    messageArray = new MessageImpl[myList.size()];
    int i = 0;
    for (Message m : myList) {
      messageArray[i] = (MessageImpl) m;
      i++;
    }
  }

  class CustomListView extends ArrayAdapter<Message> {
    // class for the custom list view that is used for the messages
    private Message[] messages;
    private Activity context;

    // change the messages with a new set
    public void swapItems(Message[] messages) {
      this.messages = messages;
      notifyDataSetChanged();
    }

    public CustomListView(Activity context, Message[] messages) {
      super(context, R.layout.custom_listview_layout, messages);
      this.context = context;
      this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      // setting up the custom listview
      View r = convertView;
      ViewHolder viewHolder = null;
      if (r == null) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        r = layoutInflater.inflate(R.layout.custom_listview_layout, null, true);
        viewHolder = new ViewHolder(r);
        r.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) r.getTag();
      }
      viewHolder.tv.setText("message id: "  + messages[position].getMessageId());
      int visibility = messages[position].getViewedState() == 0 ? View.VISIBLE : View.INVISIBLE;
      viewHolder.ivw.setVisibility(visibility);

      return r;
    }

    class ViewHolder {
      TextView tv;
      ImageView ivw;

      ViewHolder(View v) {
        tv = (TextView) v.findViewById(R.id.customListviewMessageDescription);
        ivw = (ImageView) v.findViewById(R.id.customListviewImageView);
      }
    }
  }
}
