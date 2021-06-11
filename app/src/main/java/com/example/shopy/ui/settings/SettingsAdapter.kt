package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.DevByItemBinding
import com.example.shopy.datalayer.entity.settings.DeveloperModel

class SettingsAdapter(var developerList: List<DeveloperModel>): RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {
    class SettingsViewHolder(val binding: DevByItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        return SettingsViewHolder(DevByItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.binding.nameTV.text=developerList.get(position).name
        holder.binding.githubBtn.setOnClickListener {
            val url = developerList.get(position).github
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(holder.itemView.context,i,null)
        }
        holder.binding.linkedinBtn.setOnClickListener {
            val url = developerList.get(position).LinkedIn
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(holder.itemView.context,i,null)
        }
    }

    override fun getItemCount(): Int {
        return developerList.size
    }
}