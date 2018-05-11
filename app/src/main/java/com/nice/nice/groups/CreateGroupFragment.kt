package com.nice.nice.groups

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nice.nice.R


class CreateGroupFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_group_layout, container, false)
    }


    companion object {
        fun newInstance(): CreateGroupFragment =
                CreateGroupFragment()
    }

}

