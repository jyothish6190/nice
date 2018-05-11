package com.nice.nice.report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.report.dummy.DummyContent.DummyItem
import com.nice.nice.report.models.Report
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.report_list_item.*
import java.util.*

class ReportListFragment : Fragment() {
    // TODO: Customize parameters
    private var mListener: OnListFragmentInteractionListener? = null
    private var reportsArray: ArrayList<Report> = ArrayList()

    private var mAuth: FirebaseAuth? = null


    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        fetchReports()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val list = inflater!!.inflate(R.layout.report_list_item, container, false)

        if (list is RecyclerView) {
            val context =  list.getContext()

            list.layoutManager = LinearLayoutManager(context)
            list.adapter = ReportListAdapter(activity, reportsArray, mListener)
        }
        return list
    }

    private fun fetchReports() {
        reportsArray.clear()
        var user = mAuth!!.currentUser

        firestore.document(user!!.uid).collection(Report.COLLECTION_KEY).addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("ERROR", e.message)
                documentSnapshot != null  && !documentSnapshot.isEmpty && documentSnapshot.size() > 0 -> {
                    with(documentSnapshot) {
                        for (item in documentSnapshot.documents) {
                            if(item.exists()) {
                                var report = Report(item.getString(Report.NAME_KEY)!!, item.getString(Report.DESC_KEY)!!, item.getString(Report.FILE_KEY)!!)
                                reportsArray.add(report)
                            }
                        }
                        list?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): ReportListFragment {
            val fragment = ReportListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
