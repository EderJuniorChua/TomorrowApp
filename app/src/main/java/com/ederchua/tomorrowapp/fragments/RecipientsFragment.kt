package com.ederchua.tomorrowapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ederchua.tomorrowapp.R
import com.ederchua.tomorrowapp.Recipient
import com.ederchua.tomorrowapp.RecipientAdapter
import com.ederchua.tomorrowapp.SQLHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecipientsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipientsFragment : Fragment() {

    lateinit var recipients: ArrayList<Recipient>

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var filterSent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private fun refreshRecipientList() {
        val rvRecipient: RecyclerView = requireView().findViewById(R.id.rvRecipient)
        if (filterSent) {
            recipients = SQLHelper(requireView().context).getSentRecipients()
        } else if (!filterSent){
            recipients = SQLHelper(requireView().context).getUnsentRecipients()
        }

        val adapter = RecipientAdapter(recipients)
        rvRecipient.adapter = adapter
        rvRecipient.layoutManager = LinearLayoutManager(requireView().context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var rgRecipientsFilter = requireView().findViewById<RadioGroup>(R.id.rgRecipientsFilter)
        rgRecipientsFilter.check(R.id.rbUnsentRecipients)

        rgRecipientsFilter.setOnCheckedChangeListener { _, i ->
            if (i == R.id.rbSentRecipients) {
                filterSent = true
            } else if (i == R.id.rbUnsentRecipients){
                filterSent = false;
            }
            refreshRecipientList()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipientsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecipientsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        refreshRecipientList()
    }
}