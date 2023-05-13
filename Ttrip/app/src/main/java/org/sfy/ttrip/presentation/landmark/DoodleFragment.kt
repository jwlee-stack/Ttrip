package org.sfy.ttrip.presentation.landmark

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentDoodleBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class DoodleFragment : BaseFragment<FragmentDoodleBinding>(R.layout.fragment_doodle), DrawDoodleDialogListener {

    private var arFragment: ArFragment? = null
    private var isObjectPlaced = false
    private var bitmap: Bitmap? = null

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        binding.ivAddDoodle.setOnClickListener {
            val dialog = DrawDoodleDialog(requireContext(), this)
            dialog.show()
        }
        arFragment = childFragmentManager.findFragmentById(R.id.arContentFragment) as ArFragment?

        arFragment?.setOnTapArPlaneListener { hitResult, _, _ ->
            // 추가적인 배치가 불가능한 상태일 경우
            if (isObjectPlaced) {
                return@setOnTapArPlaneListener
            }
            ViewRenderable.builder()
                .setView(requireContext(), R.layout.item_doodle)
                .build()
                .thenAccept { renderable ->
                    val imageView = renderable.view.findViewById<ImageView>(R.id.iv_doodle_view)
                    imageView.setImageBitmap(bitmap)
                    placeImageOnArSurface(renderable, hitResult.createAnchor())
                }
            // 사물 배치 상태 변수 변경
            isObjectPlaced = true
            binding.ivAddDoodle.isEnabled = false
        }
    }

    private fun placeImageOnArSurface(viewRenderable: ViewRenderable, anchor: Anchor) {
        val anchorNode = AnchorNode(anchor)
        viewRenderable.isShadowCaster = false
        val transformableNode = TransformableNode(arFragment!!.transformationSystem)
        transformableNode.apply {
            setParent(anchorNode)
            scaleController.maxScale = 0.5f
            scaleController.minScale = 0.05f
            renderable = viewRenderable
        }
        arFragment!!.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()

        // 위치 정보 및 사진을 서버에 전송
        val position = transformableNode.worldPosition
        // sendPositionAndImageToServer(position, userPhotoUrl)
    }

    private fun sendPositionAndImageToServer(position: Vector3, imageUrl: String) {

    }

    override fun onConfirmButtonClicked(bitmap: Bitmap) {
        this.bitmap = bitmap
        showToast("낙서가 생성되었습니다.\n낙서를 배치해보세요!")
    }
}