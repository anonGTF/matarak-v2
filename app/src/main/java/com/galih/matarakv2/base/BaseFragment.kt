package com.galih.matarakv2.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.galih.matarakv2.utils.Resource
import com.github.florent37.runtimepermission.PermissionResult
import com.github.florent37.runtimepermission.kotlin.askPermission

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun <T> setObserver(
        onSuccess: (Resource<T>) -> Unit,
        onError: (Resource<T>) -> Unit = {},
        onLoading: (Resource<T>) -> Unit = {}
    ): Observer<in Resource<T>> {
        return Observer<Resource<T>> { data ->
            when(data) {
                is Resource.Success -> onSuccess(data)
                is Resource.Error -> onError(data)
                is Resource.Loading -> onLoading(data)
            }
        }
    }

    protected fun askPermissions(vararg permissions: String, onAccepted: (PermissionResult) -> Unit) {
        askPermission(*permissions) {
            if (it.isAccepted) {
                onAccepted.invoke(it)
            }
        }.onDeclined { e ->
            if (e.hasDenied()){
                e.denied.forEach { _ ->
                    AlertDialog.Builder(requireContext())
                        .setMessage("Mohon menyetujui permintaan kami")
                        .setPositiveButton("Yes"){ _, _ ->
                            e.askAgain()
                        }
                        .setNegativeButton("No"){ dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }

            if (e.hasForeverDenied()){
                e.foreverDenied.forEach { _ ->
                    e.goToSettings()
                }
            }
        }
    }

    protected fun showToast(message: String) =
        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()

    fun View.visible() = run { this.visibility = View.VISIBLE }
    fun View.gone() = run { this.visibility = View.GONE }
}