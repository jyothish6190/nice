package com.nice.nice.report

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nice.nice.R
import com.nice.nice.report.ReportListFragment.OnListFragmentInteractionListener
import com.nice.nice.report.models.Report
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ReportListAdapter(private val context: Context, private val mValues: List<Report>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.report_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mItem = mValues[position]
        holder.nameTxt!!.text = mItem.name
        holder.descTxt!!.text = mItem.desc

        if(!mItem.file.isNullOrBlank() && !mItem.file.isEmpty()) {
            Picasso.with(context).load(mItem.file).fit().centerCrop().into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var nameTxt: TextView? = null
        var descTxt: TextView? = null
        var imageView: ImageView? = null

        init {
            nameTxt = mView.findViewById(R.id.reportName)
            descTxt = mView.findViewById(R.id.reportDesc)
            imageView = mView.findViewById(R.id.reportImage)

        }
    }
}
