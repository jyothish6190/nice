package com.nice.nice.todo

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nice.nice.R
import com.nice.nice.todo.TaskListFragment.OnListFragmentInteractionListener
import com.nice.nice.todo.models.Task
import com.nice.nice.user.models.User


class TaskListAdapter(private val mValues: List<Task>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {
    private var mAuth: FirebaseAuth? = null

    private val firestore by lazy {
        FirebaseFirestore.getInstance().collection(User.COLLECTION_KEY)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mAuth = FirebaseAuth.getInstance()
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mItem = mValues[position]
        holder.taskView!!.text = mItem.task
        holder.descTxt!!.text = mItem.desc
        holder.dueTxt!!.text = mItem.due.toString()

        holder.done!!.setOnClickListener{
            updateTask(mItem)
        }
    }

    private fun updateTask(mItem: Task){
        var user = mAuth!!.currentUser

        val taskMap = mapOf(
                Task.TASK_KEY to mItem.task,
                Task.DESC_KEY to mItem.desc,
                Task.COMPLETED_KEY to mItem.completed,
                Task.CREATED_KEY to mItem.createdAt,
                Task.DUE_KEY to mItem.due,
                Task.DONE_KEY to true
        )
        firestore.document(user!!.uid).collection(Task.COLLECTION_KEY).document(mItem.id)
                .set(taskMap)
                .addOnSuccessListener(OnSuccessListener<Void> {
                    Log.d("tasklist adapter", "DocumentSnapshot successfully written!")
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.w("tasklist adapter", "Error writing document", e)
                })
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var taskView: TextView? = null
        var descTxt: TextView? = null
        var dueTxt: TextView? = null
        var done: CheckBox? = null

        init {
            taskView = mView.findViewById(R.id.taskName)
            descTxt = mView.findViewById(R.id.desc)
            dueTxt = mView.findViewById(R.id.dueDate)
            done = mView.findViewById(R.id.done)
        }
    }
}
