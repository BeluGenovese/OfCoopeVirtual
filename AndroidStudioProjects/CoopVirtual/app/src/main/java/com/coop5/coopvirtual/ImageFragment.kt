package com.coop5.coopvirtual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment


class ImageFragment : Fragment() {

        companion object {
            private const val ARG_IMAGE_RESOURCE = "image_resource"

            fun newInstance(imageResource: Int): ImageFragment {
                val fragment = ImageFragment()
                val args = Bundle()
                args.putInt(ARG_IMAGE_RESOURCE, imageResource)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_with_image, container, false)
            val imageView: ImageView = view.findViewById(R.id.logo_image)

            val imageResource = arguments?.getInt(ARG_IMAGE_RESOURCE) ?: return view
            imageView.setImageResource(imageResource)
            return view
        }
    }



