package org.sfy.ttrip.presentation.live

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentDoodleBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class DoodleFragment : BaseFragment<FragmentDoodleBinding>(R.layout.fragment_doodle) {

    private var arFragment: ArFragment? = null
    private var userPhotoUrl = "http://k8d104.p.ssafy.io:8081/images/profileImg/070ba99a-c7d8-44e4-a18d-547b05ca14f8_20230511_125718.jpg" // 사용자가 등록한 사진의 URL


    override fun initView() {
        arFragment = childFragmentManager.findFragmentById(R.id.arContentFragment) as ArFragment?

        arFragment?.setOnTapArPlaneListener { hitResult, _, _ ->
            ViewRenderable.builder()
                .setView(requireContext(), R.layout.your_image_layout) // 프리뷰 이미지에 사용할 XML 레이아웃
                .build()
                .thenAccept { renderable ->
                    val imageView = renderable.view.findViewById<ImageView>(R.id.your_image_view_id) // 프리뷰 이미지에 사용할 ImageView
                    Glide.with(requireContext())
                        .asBitmap()
                        .load(userPhotoUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                                imageView.setImageBitmap(bitmap)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    placeImageOnArSurface(renderable, hitResult.createAnchor())
                }
        }
    }

    private fun placeImageOnArSurface(renderable: ViewRenderable, anchor: Anchor) {
        val anchorNode = AnchorNode(anchor)
        val imageNode = Node()
        imageNode.setParent(anchorNode)

        val quaternion = Quaternion.axisAngle(Vector3(0f, 1f, 0f), 90f) // 특정 각도에 회전한 객체를 등록하려면 이 행을 수정하세요.
        renderable.isShadowCaster = false
        imageNode.renderable = renderable
        imageNode.localRotation = quaternion

        // 위치 정보 및 사진을 서버에 전송
        val position = anchorNode.worldPosition
        sendPositionAndImageToServer(position, userPhotoUrl)

        // 사용자 인터페이스를 업데이트
        arFragment?.arSceneView?.scene?.addChild(anchorNode)
    }

    private fun sendPositionAndImageToServer(position: Vector3, imageUrl: String) {
        // 이 위치에서 위치 정보를 서버에 전송하려면 이를 구현하세요.
        // 여기에 서버 연결 및 데이터 전송 코드가 필요합니다.
    }
}