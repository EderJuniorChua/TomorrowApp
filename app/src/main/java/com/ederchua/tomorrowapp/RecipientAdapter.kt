package com.ederchua.tomorrowapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RecipientAdapter (private val recipients: MutableList<Recipient>) : RecyclerView.Adapter<RecipientAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvFullName = itemView.findViewById<TextView>(R.id.tvFullName)
        val tvPriorityGroup = itemView.findViewById<TextView>(R.id.tvPriorityGroup)
        val tvPhoneNumber = itemView.findViewById<TextView>(R.id.tvPhoneNumber)
//        val tvDateTarget = itemView.findViewById<TextView>(R.id.tvDateSchedule)
//        val tvTimeTarget = itemView.findViewById<TextView>(R.id.tvTimeSchedule)
//        val cbMessageSent = itemView.findViewById<CheckBox>(R.id.cbMessageSent)
        val btnAdaptive = itemView.findViewById<Button>(R.id.btnAdaptive)
        val btnDelete = itemView.findViewById<Button>(R.id.btnRemove)
        val layoutRecipientInfo = itemView.findViewById<LinearLayout>(R.id.layoutRecipientInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val recipient = inflater.inflate(R.layout.recipient_item, parent, false)
        // Return a new holder instance
        return ViewHolder(recipient)
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecipientAdapter.ViewHolder, position: Int) {
        val recipient: Recipient = recipients.get(position)
        val fullName = holder.tvFullName
        fullName.setText(recipient.fullName)
        val priorityGroup = holder.tvPriorityGroup
        priorityGroup.setText(recipient.priorityGroup)
        val phoneNumber = holder.tvPhoneNumber
        phoneNumber.setText(recipient.phoneNumber)
//        val dateTarget = holder.tvDateTarget
//        dateTarget.setText(recipient.dateTarget)
//        val timeTarget = holder.tvTimeTarget
//        timeTarget.setText(recipient.timeTarget)
//
//        val messageSent = holder.cbMessageSent
//        messageSent.isChecked = recipient.messageSent.toBoolean()
//
        val btnAdaptive = holder.btnAdaptive
        if (recipient.messageSent==0) {
            btnAdaptive.text = "Send one"
            btnAdaptive.setOnClickListener(View.OnClickListener {
                SMSHelper().sendMessage(recipient, holder.itemView.context)
                moveToSent(recipient, position, holder.itemView.context)
            })
        } else if (recipient.messageSent == 1) {
            btnAdaptive.text = "Move to unsent"
            btnAdaptive.setOnClickListener(View.OnClickListener { moveToUnsent(recipient, position, holder.itemView.context) })
        }

        val btnDeleteRecipient = holder.btnDelete
        btnDeleteRecipient.setOnClickListener(View.OnClickListener { val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Delete recipient?")
                .setCancelable(false)
                .setPositiveButton("Delete") { dialog, id ->
                    // Delete selected note from database
                    removeRecipient(holder, position, holder.itemView.context)
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
             })

        //messageSent.setOnCheckedChangeListener(null)
        //messageSent.isChecked = recipient.messageSent == 1

//        messageSent.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
//            run {
//                isChecked != isChecked
//                updateMessageSent(recipient, isChecked, holder.itemView.context)
//            }
//        })

        val layoutRecipientInfo = holder.layoutRecipientInfo
        layoutRecipientInfo.setOnClickListener {
            updateRecipient(holder, position, holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return recipients.size
    }

    private fun moveToUnsent(recipient: Recipient, position: Int, context: Context){
        recipient.messageSent = 0
        recipients.removeAt(position)
        notifyItemRemoved(position)
        SQLHelper(context).updateRecipient(recipient)
    }

    private fun moveToSent(recipient: Recipient, position: Int, context: Context){
        recipient.messageSent = 1
        recipients.removeAt(position)
        notifyItemRemoved(position)
        SQLHelper(context).updateRecipient(recipient)
    }

    private fun updateRecipient(holder: RecipientAdapter.ViewHolder, position: Int, context: Context) {
        val intent = Intent(context, AddRecipientActivity::class.java)
        intent.putExtra("id", recipients[position].id)
        intent.putExtra("fullName", recipients[position].fullName)
        intent.putExtra("priorityGroup", recipients[position].priorityGroup)
        intent.putExtra("phoneNumber", recipients[position].phoneNumber)
        intent.putExtra("dateTarget", recipients[position].dateTarget)
        intent.putExtra("timeTarget", recipients[position].timeTarget)
        intent.putExtra("messageSent", recipients[position].messageSent)
        ContextCompat.startActivity(context, intent, null)
    }

    private fun removeRecipient(holder: RecipientAdapter.ViewHolder, position: Int, context: Context) {
        val recipient: Recipient = recipients[position]
        SQLHelper(context).deleteRecipient(recipient)
        recipients.removeAt(position)
        notifyItemRemoved(position)
    }

}

private fun Int.toBoolean(): Boolean {
    if(this==0) return false
    return true
}
