package com.muzaaz.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chatroom.*

class ChatroomActivity : AppCompatActivity() {

    var user_name : String? = null
    var room_name : String? = null
    lateinit var ref : DatabaseReference
    var keyDb : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        user_name = intent.extras!!.get("user_name").toString()
        room_name = intent.extras!!.get("room_name").toString()

        title = "room - $room_name"

        ref = FirebaseDatabase.getInstance().reference.child(room_name!!)

        btnChat.setOnClickListener {
            val map = HashMap<String, Any> ()
            keyDb = ref.push().key
            ref.updateChildren(map)

            val messageRoot = ref.child(keyDb!!)
            val map2 = HashMap<String, Any>()
            map2["name"] = user_name!!
            map2["msg"] = edtChatroom.text.toString()

            messageRoot.updateChildren(map2)

            edtChatroom.setText("")
        }
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                chat_conversation(p0)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                chat_conversation(p0)
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun chat_conversation(p0: DataSnapshot) {
        val i = p0.children.iterator()
        while (i.hasNext()) {
            var chat_msg = (i.next() as DataSnapshot).value as String
            var chat_username =(i.next() as DataSnapshot).value as String

            txtChat.append("$chat_username : $chat_msg \n")
        }
    }
}
