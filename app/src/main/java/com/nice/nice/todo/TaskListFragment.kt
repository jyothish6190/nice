package com.nice.nice.todo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.todo.dummy.DummyContent.DummyItem
import com.nice.nice.todo.models.Task
import com.nice.nice.user.models.User
import kotlinx.android.synthetic.main.fragment_item_list.*
import java.util.*


class TaskListFragment : Fragment() {
    private var mListener: OnListFragmentInteractionListener? = null

    private var tasksArray: ArrayList<Task> = ArrayList()

    private var mAuth: FirebaseAuth? = null


    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()

            view.layoutManager = LinearLayoutManager(context)
            view.adapter = TaskListAdapter(tasksArray, mListener)
            val itemDecor = DividerItemDecoration(context, LinearLayout.VERTICAL)
            view.addItemDecoration(itemDecor)
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        fetchTasks()
    }


    private fun fetchTasks() {

        var user = mAuth!!.currentUser

        firestore.document(user!!.uid).collection(Task.COLLECTION_KEY).addSnapshotListener { documentSnapshot, e ->
            when {
                e != null -> Log.e("ERROR", e.message)
                documentSnapshot != null && !documentSnapshot.isEmpty && documentSnapshot.size() > 0 -> {
                    with(documentSnapshot) {
                        tasksArray.clear()
                        for (item in documentSnapshot.documents) {
                            if(item.exists() &&!item.getBoolean(Task.DONE_KEY)!!){
                                var task = Task(item.id, item.getString(Task.TASK_KEY)!!, item.getString(Task.DESC_KEY)!!, item.getBoolean(Task.DONE_KEY)!!, item.getDate(Task.CREATED_KEY)!!, item.getDate(Task.DUE_KEY)!!, item.getDate(Task.COMPLETED_KEY)!!)
                                tasksArray.add(task)
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


    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: DummyItem)
    }

    companion object {

        private val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): TaskListFragment {
            val fragment = TaskListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
